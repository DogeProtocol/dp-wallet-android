package com.dpwallet.app.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dpwallet.app.R;
import com.dpwallet.app.entity.ServiceException;
import com.dpwallet.app.utils.GlobalMethods;
import com.dpwallet.app.utils.PrefConnect;
import com.dpwallet.app.viewmodel.KeyViewModel;

public class HomeWalletFragment extends Fragment  {

    private static final String TAG = "HomeWalletFragment";

    private OnHomeWalletCompleteListener mHomeWalletListener;

    public static HomeWalletFragment newInstance() {
        HomeWalletFragment fragment = new HomeWalletFragment();
        return fragment;
    }

    public HomeWalletFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_wallet_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton backImageButton = (ImageButton) getView().findViewById(R.id.imageButton_home_wallet_back_arrow);

        LinearLayout setWalletLinearLayout = (LinearLayout) getView().findViewById(R.id.linear_layout_home_set_wallet);
        EditText setWalletPasswordEditText = (EditText) getView().findViewById(R.id.editText_home_set_wallet_password);
        EditText setWalletRetypePasswordEditText = (EditText) getView().findViewById(R.id.editText_home_set_wallet_retype_password);
      //  Button setWalletButton = (Button) getView().findViewById(R.id.button_home_set_wallet_next);

        LinearLayout createRestoreWalletLinearLayout = (LinearLayout) getView().findViewById(R.id.linear_layout_home_create_restore_wallet);
        RadioButton createRestoreWalletRadioButton_1 = (RadioButton) getView().findViewById(R.id.radioButton_create_restore_wallet_1);
        RadioButton createRestoreWalletRadioButton_2 = (RadioButton) getView().findViewById(R.id.radioButton_create_restore_wallet_2);
        RadioButton createRestoreWalletRadioButton_3 = (RadioButton) getView().findViewById(R.id.radioButton_create_restore_wallet_3);
        Button createRestoreWalletButton = (Button) getView().findViewById(R.id.button_create_restore_next);



      //  ProgressBar progressBar = (ProgressBar) getView().findViewById(R.id.progress_loader_home_set_wallet);

        /*
        backImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    switch (View.VISIBLE) {
                        case createRestoreWalletLinearLayout.VISIBLE:
                            createRestoreWalletLinearLayout.setVisibility(View.GONE);
                            setWalletLinearLayout.setVisibility(View.VISIBLE);
                            break;
                        default:
                            setWalletLinearLayout.setVisibility(View.VISIBLE);
                            break;
                    }

                }catch (ServiceException e) {
                    GlobalMethods.ExceptionError(getContext(), TAG, e);
                }
            }
        });
        */

/*
        setWalletButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    String message = getResources().getString(R.string.home_set_wallet_message_exits);
                    if (progressBar.getVisibility() == View.VISIBLE) {
                        GlobalMethods.ShowToast(getContext(), message);
                        return;
                    }
*/
                    /*
                    String message = getResources().getString(R.string.home_set_wallet_message_exits);
                    if (progressBar.getVisibility() == View.VISIBLE) {
                        GlobalMethods.ShowToast(getContext(), message);
                        return;
                    }

                    message = getResources().getString(R.string.home_set_wallet_password_minimum_message);
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

                            mHomeSetWalletListener.onHomeSetWalletComplete();

                            return;
                        }
                         message = getResources().getString(R.string.home_set_wallet_password_mismatch_message);
                    }

                    Bundle bundleRoute = new Bundle();
                    bundleRoute.putString("message", message);
                    FragmentManager fragmentManager  = getFragmentManager();
                    MessageInformationDialogFragment messageDialogFragment = MessageInformationDialogFragment.newInstance();
                    messageDialogFragment.setCancelable(false);
                    messageDialogFragment.setArguments(bundleRoute);
                    messageDialogFragment.show(fragmentManager, "");
                    */

/*

                } catch (ServiceException e) {
                    GlobalMethods.ExceptionError(getContext(), TAG, e);
                }
            }
        });
*/
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    public static interface OnHomeWalletCompleteListener {
        public abstract void onHomeSetWalletComplete();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mHomeWalletListener = (OnHomeWalletCompleteListener)context;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " ");
        }
    }
}
