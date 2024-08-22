package com.dpwallet.app.view.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dpwallet.app.R;
import com.dpwallet.app.api.read.model.AccountPendingTransactionSummary;
import com.dpwallet.app.api.read.model.AccountPendingTransactionSummaryResponse;
import com.dpwallet.app.api.read.model.AccountTransactionSummary;
import com.dpwallet.app.api.read.model.AccountTransactionSummaryResponse;
import com.dpwallet.app.asynctask.read.AccountPendingTxnRestTask;
import com.dpwallet.app.asynctask.read.AccountTxnRestTask;
import com.dpwallet.app.entity.KeyServiceException;
import com.dpwallet.app.utils.GlobalMethods;
import com.dpwallet.app.utils.GridAutoFitLayoutManager;
import com.dpwallet.app.utils.PrefConnect;
import com.dpwallet.app.utils.Utility;
import com.dpwallet.app.view.adapter.AccountPendingTransactionAdapter;
import com.dpwallet.app.view.adapter.AccountTransactionAdapter;
import com.dpwallet.app.view.adapter.WalletAdapter;
import com.dpwallet.app.viewmodel.JsonViewModel;
import com.dpwallet.app.viewmodel.KeyViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import org.apache.commons.lang3.ObjectUtils;
import org.w3c.dom.Text;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WalletsFragment extends Fragment  {
    private static final String TAG = "WalletsFragment";

    private WalletAdapter walletAdapter;
    Unbinder unbinder;
    @BindView(R.id.recycler_wallets)  RecyclerView recycler;
    private JsonViewModel jsonViewModel;
    private KeyViewModel keyViewModel;
    private OnWalletsCompleteListener mWalletsListener;

    public static WalletsFragment newInstance() {
        WalletsFragment fragment = new WalletsFragment();
        return fragment;
    }

    public WalletsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.wallet_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.unbinder = ButterKnife.bind((Object) this, view);

        keyViewModel = new KeyViewModel();

        jsonViewModel = new JsonViewModel(getContext(), getArguments().getString("languageKey"));
        String walletPassword = getArguments().getString("walletPassword");

        ImageButton backArrowImageButton = (ImageButton) getView().findViewById(R.id.imageButton_wallets_back_arrow);
        TextView walletTitleTextView = (TextView) getView().findViewById(R.id.textview_wallets_langValues_wallets);

        TextView walletHeaderAddressTextView = (TextView) getView().findViewById(R.id.textView_wallet_header_langValues_address);
        TextView walletHeaderScanTextView = (TextView) getView().findViewById(R.id.textView_wallet_header_langValues_scan);
        TextView walletHeaderSeedTextView = (TextView) getView().findViewById(R.id.textView_wallet_header_langValues_reveal_seed);

        TextView walletCreateOrRestoreTextView = (TextView) getView().findViewById(R.id.textview_wallet_langValues_create_or_restore);

        ProgressBar progressBar = (ProgressBar) getView().findViewById(R.id.progress_wallets);

        walletTitleTextView.setText(jsonViewModel.getWalletsByLangValues());

        walletHeaderAddressTextView.setText(jsonViewModel.getAddressByLangValues());
        walletHeaderScanTextView.setText(jsonViewModel.getDpscanByLangValues());
        walletHeaderSeedTextView.setText(jsonViewModel.getRevealSeedByLangValues());

        walletCreateOrRestoreTextView.setText(jsonViewModel.getCreateRestoreWalletByLangValues());

        progressBar.setVisibility(View.VISIBLE);

        this.recycler.removeAllViewsInLayout();

        int mNoOfColumns = Utility.calculateNoOfColumns(getContext(), R.id.recycler_wallets);

        GridAutoFitLayoutManager mLayoutManager = new GridAutoFitLayoutManager(getContext(),
                mNoOfColumns, 1, false);

        this.recycler.setLayoutManager(mLayoutManager);

        this.walletAdapter = new WalletAdapter(getContext(), PrefConnect.WALLET_INDEX_TO_ADDRESS_MAP);

        this.recycler.setAdapter(walletAdapter);

        this.walletAdapter.notifyDataSetChanged();

        progressBar.setVisibility(View.GONE);

        backArrowImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mWalletsListener.onWalletsComplete(1, walletPassword, null);
            }
        });

        walletCreateOrRestoreTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (walletPassword==null || walletPassword.isEmpty()) {
                     unlockDialogFragment(view, progressBar);
                } else {
                    VerifyPassword(walletPassword);
                }
            }
        });

        walletAdapter.SetOnWalletItemClickListener(new WalletAdapter.OnWalletItemClickListener() {
            @Override
            public void onWalletItemClick(View view, int position) {
                   mWalletsListener.onWalletsComplete(3, null, String.valueOf(position));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    public static interface OnWalletsCompleteListener {
        public abstract void onWalletsComplete(int status, String password, String indexKey);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mWalletsListener = (OnWalletsCompleteListener)context;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " ");
        }
    }

    private void unlockDialogFragment(View view, ProgressBar progressBarSendCoins) {
        try {
            //Alert unlock dialog
            AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setTitle((CharSequence) "").setView((int)
                            R.layout.unlock_dialog_fragment).create();
            dialog.dismiss();
            dialog.setCancelable(false);
            dialog.show();

            TextView unlockWalletTextView = (TextView) dialog.findViewById(R.id.textView_unlock_langValues_unlock_wallet);
            TextView unlockPasswordTextView = (TextView) dialog.findViewById(R.id.textView_unlock_langValues_enter_wallet_password);

            EditText passwordEditText = (EditText) dialog.findViewById(R.id.editText_unlock_langValues_enter_a_password);
            Button unlockButton = (Button) dialog.findViewById(R.id.button_unlock_langValues_unlock);
            Button closeButton = (Button) dialog.findViewById(R.id.button_unlock_langValues_close);

            unlockWalletTextView.setText(jsonViewModel.getUnlockWalletByLangValues());
            unlockPasswordTextView.setText(jsonViewModel.getEnterQuantumWalletPasswordByLangValues());
            passwordEditText.setHint(jsonViewModel.getEnterApasswordByLangValues());
            unlockButton.setText(jsonViewModel.getUnlockByLangValues());
            closeButton.setText(jsonViewModel.getCloseByLangValues());
            unlockButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String walletPassword = passwordEditText.getText().toString();
                    if (walletPassword==null || walletPassword.isEmpty()) {
                        messageDialogFragment(jsonViewModel.getEnterApasswordByLangValues());
                    } else {
                        VerifyPassword(walletPassword);
                        dialog.dismiss();
                    }
                }
            });
            closeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            GlobalMethods.ExceptionError(getContext(), TAG, e);
        }
    }

    private void messageDialogFragment(String message) {
        try {
            Bundle bundleRoute = new Bundle();
            bundleRoute.putString("message", message);

            FragmentManager fragmentManager  = getFragmentManager();
            MessageInformationDialogFragment messageDialogFragment = MessageInformationDialogFragment.newInstance();
            messageDialogFragment.setCancelable(false);
            messageDialogFragment.setArguments(bundleRoute);
            messageDialogFragment.show(fragmentManager, "");
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getContext(), TAG, e);
        }
    }

    private void VerifyPassword(String walletPassword)  {
        try {
            String passwordSHA256 = PrefConnect.getSha256Hash(walletPassword);
            String password= keyViewModel.decryptDataByString(getContext(), PrefConnect.WALLET_KEY_PASSWORD, walletPassword);

            if (passwordSHA256.equalsIgnoreCase(password)) {
                mWalletsListener.onWalletsComplete(2, walletPassword, null);
            } else {
                messageDialogFragment(jsonViewModel.getEnterApasswordByLangValues());
            }
        } catch (Exception e) {
           // GlobalMethods.ExceptionError(getContext(), TAG, e);
            messageDialogFragment(jsonViewModel.getWalletOpenErrorByErrors());
        }
    }
}
