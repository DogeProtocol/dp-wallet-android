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

public class HomeNewWalletFragment extends Fragment  {

    private static final String TAG = "HomeNewWalletFragment";

    private OnHomeNewWalletCompleteListener mHomeNewWalletListener;

    public static HomeNewWalletFragment newInstance() {
        HomeNewWalletFragment fragment = new HomeNewWalletFragment();
        return fragment;
    }

    public HomeNewWalletFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_new_wallet_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton backWallet = (ImageButton) getView().findViewById(R.id.imageButton_home_new_wallet_back_arrow);
        EditText newWalletPassword = (EditText) getView().findViewById(R.id.editText_home_new_wallet_password);
        EditText newWalletRetypePassword = (EditText) getView().findViewById(R.id.editText_home_new_wallet_retype_password);
        Button newWallet = (Button) getView().findViewById(R.id.button_home_new_wallet_create);
        ProgressBar progressBar = (ProgressBar) getView().findViewById(R.id.progress_loader_home_new_wallet);

        backWallet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mHomeNewWalletListener.onHomeNewWalletComplete(0);
            }
        });

        newWallet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    String message = getResources().getString(R.string.home_new_wallet_message_exits);
                    if (progressBar.getVisibility() == View.VISIBLE) {
                        GlobalMethods.ShowToast(getContext(), message);
                        return;
                    }

                    message = getResources().getString(R.string.home_new_wallet_password_minimum_message);
                    if (newWalletPassword.getText().length() > GlobalMethods.MINIMUM_PASSWORD_LENGTH) {
                        if (newWalletPassword.getText().toString().equals(newWalletRetypePassword.getText().toString())) {

                            progressBar.setVisibility(View.VISIBLE);

                            KeyViewModel keyViewModel = new KeyViewModel();
                            int[] SK_KEY = (int[]) keyViewModel.newAccount();
                            int[] PK_KEY = (int[]) keyViewModel.publicKeyFromPrivateKey(SK_KEY);
                            String address = (String) keyViewModel.getAccountAddress(PK_KEY);
                            String password = (String) newWalletPassword.getText().toString();
                            keyViewModel.encryptDataByAccount(getActivity(), address, password, SK_KEY, PK_KEY);

                            PrefConnect.writeString(getActivity(), PrefConnect.walletAddress, address);

                            progressBar.setVisibility(View.GONE);

                            mHomeNewWalletListener.onHomeNewWalletComplete(1);

                            return;
                        }
                         message = getResources().getString(R.string.home_new_wallet_password_mismatch_message);
                    }

                    Bundle bundleRoute = new Bundle();
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

    public static interface OnHomeNewWalletCompleteListener {
        public abstract void onHomeNewWalletComplete(int status);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mHomeNewWalletListener = (OnHomeNewWalletCompleteListener)context;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " ");
        }
    }
}
