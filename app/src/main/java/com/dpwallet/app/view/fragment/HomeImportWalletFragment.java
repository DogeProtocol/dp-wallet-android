package com.dpwallet.app.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dpwallet.app.R;
import com.dpwallet.app.entity.ServiceException;
import com.dpwallet.app.utils.GlobalMethods;
import com.dpwallet.app.utils.PrefConnect;
import com.dpwallet.app.viewmodel.KeyViewModel;

public class HomeImportWalletFragment extends Fragment  {

    private static final String TAG = "HomeImportWalletFragment";

    private OnHomeImportWalletCompleteListener mHomeImportWalletListener;

    public static HomeImportWalletFragment newInstance() {
        HomeImportWalletFragment fragment = new HomeImportWalletFragment();
        return fragment;
    }

    public HomeImportWalletFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_import_wallet_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton backWallet = (ImageButton) getView().findViewById(R.id.imageButton_home_import_wallet_back_arrow);
        EditText walletPassword = (EditText) getView().findViewById(R.id.editText_home_import_wallet_password);
        Button importWallet = (Button) getView().findViewById(R.id.button_home_import_wallet_import);

        ProgressBar progressBar = (ProgressBar) getView().findViewById(R.id.progress_loader_home_import_wallet);

        backWallet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mHomeImportWalletListener.onHomeImportWalletComplete(0);
            }
        });

        importWallet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Bundle bundleRoute = new Bundle();
                    String message = getResources().getString(R.string.home_import_wallet_password_minimum_message);
                    if (walletPassword.getText().length() > GlobalMethods.MINIMUM_PASSWORD_LENGTH) {

                        progressBar.setVisibility(View.VISIBLE);
                        KeyViewModel keyViewModel = new KeyViewModel();
                        String password = (String) walletPassword.getText().toString();

                        String jsonString = "";

                        int[] SK_KEY = (int[]) keyViewModel.importKeyByAccount(getActivity(), jsonString, password);
                        int[] PK_KEY = (int[]) keyViewModel.publicKeyFromPrivateKey(SK_KEY);
                        String address = (String) keyViewModel.getAccountAddress(PK_KEY);
                        keyViewModel.encryptDataByAccount(getActivity(), address, password, SK_KEY, PK_KEY);

                        PrefConnect.writeString(getActivity(), PrefConnect.walletAddress, address);
                        progressBar.setVisibility(View.GONE);

                        mHomeImportWalletListener.onHomeImportWalletComplete(1);

                        return;
                    }
                    bundleRoute.putString("message", message);
                    FragmentManager fragmentManager  = getFragmentManager();
                    MessageInformationDialogFragment messageDialogFragment = MessageInformationDialogFragment.newInstance();
                    messageDialogFragment.setCancelable(false);
                    messageDialogFragment.setArguments(bundleRoute);
                    messageDialogFragment.show(fragmentManager, "");
                } catch (ServiceException e) {
                    GlobalMethods.ExceptionError(getContext(), TAG, e);
                }
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

    public static interface OnHomeImportWalletCompleteListener {
        public abstract void onHomeImportWalletComplete(int status);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mHomeImportWalletListener = (OnHomeImportWalletCompleteListener)context;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " ");
        }
    }

}
