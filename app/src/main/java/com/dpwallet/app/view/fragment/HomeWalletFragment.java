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
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dpwallet.app.R;
import com.dpwallet.app.entity.ServiceException;
import com.dpwallet.app.utils.GlobalMethods;
import com.dpwallet.app.utils.PrefConnect;
import com.dpwallet.app.viewmodel.JsonViewModel;
import com.dpwallet.app.viewmodel.KeyViewModel;

public class HomeWalletFragment extends Fragment  {

    private static final String TAG = "HomeWalletFragment";

    private int homeCreateRestoreWalletRadio = -1;

    private JsonViewModel jsonViewModel;

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

        jsonViewModel = new JsonViewModel(getContext(),getArguments().getString("languageKey"));

        LinearLayout homeSetWalletTopLinearLayout = (LinearLayout) getView().findViewById(R.id.top_linear_layout_home_wallet_id);
        ImageButton homeWalletBackArrowImageButton = (ImageButton) getView().findViewById(R.id.imageButton_home_wallet_back_arrow);

        LinearLayout homeSetWalletLinearLayout = (LinearLayout) getView().findViewById(R.id.linear_layout_home_set_wallet);
        TextView homeSetWalletTitleTextView = (TextView) getView().findViewById(R.id.textView_home_set_wallet_title);
        TextView homeSetWalletDescriptionTextView = (TextView) getView().findViewById(R.id.textView_home_set_wallet_description);
        TextView homeSetWalletPasswordTitleTextView = (TextView) getView().findViewById(R.id.textView_home_set_wallet_password_title);
        EditText homeSetWalletPasswordEditText = (EditText) getView().findViewById(R.id.editText_home_set_wallet_password);
        TextView homeSetWalletRetypePasswordTitleTextView = (TextView) getView().findViewById(R.id.textView_home_set_wallet_retype_password_title);
        EditText homeSetWalletRetypePasswordEditText = (EditText) getView().findViewById(R.id.editText_home_set_wallet_retype_password);
        Button homeSetWalletNextButton = (Button) getView().findViewById(R.id.button_home_set_wallet_next);

        LinearLayout homeCreateRestoreWalletLinearLayout = (LinearLayout) getView().findViewById(R.id.linear_layout_home_create_restore_wallet);
        TextView homeCreateRestoreWalletTitleTextView = (TextView) getView().findViewById(R.id.textView_home_create_restore_wallet_title);
        TextView homeCreateRestoreWalletDescriptionTextView = (TextView) getView().findViewById(R.id.textView_home_create_restore_wallet_description);

        RadioGroup homeCreateRestoreWalletRadioGroup = (RadioGroup) getView().findViewById(R.id.radioGroup_home_create_restore_wallet);
        RadioButton homeCreateRestoreWalletRadioButton_0 = (RadioButton) getView().findViewById(R.id.radioButton_home_create_restore_wallet_0);
        RadioButton homeCreateRestoreWalletRadioButton_1 = (RadioButton) getView().findViewById(R.id.radioButton_home_create_restore_wallet_1);
        Button homeCreateRestoreWalletNextButton = (Button) getView().findViewById(R.id.button_home_create_restore_wallet_next);

        ProgressBar progressBar = (ProgressBar) getView().findViewById(R.id.progress_loader_home_wallet);

        homeSetWalletLinearLayout.setVisibility(View.VISIBLE);

        SetWalletView(homeSetWalletTitleTextView, homeSetWalletDescriptionTextView, homeSetWalletPasswordTitleTextView,
                homeSetWalletPasswordEditText, homeSetWalletRetypePasswordTitleTextView, homeSetWalletRetypePasswordEditText, homeSetWalletNextButton);

        homeSetWalletNextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String message = jsonViewModel.getPasswordSpecByErrors();
                if (homeSetWalletPasswordEditText.getText().length() > GlobalMethods.MINIMUM_PASSWORD_LENGTH) {
                    if (homeSetWalletPasswordEditText.getText().toString().equals(homeSetWalletRetypePasswordEditText.getText().toString())) {
                        homeSetWalletLinearLayout.setVisibility(View.GONE);
                        homeSetWalletTopLinearLayout.setVisibility(View.VISIBLE);
                        homeCreateRestoreWalletLinearLayout.setVisibility(View.VISIBLE);
                        CreateRestoreWalletView(homeCreateRestoreWalletTitleTextView, homeCreateRestoreWalletDescriptionTextView, homeCreateRestoreWalletRadioButton_0,
                                homeCreateRestoreWalletRadioButton_1,  homeCreateRestoreWalletNextButton);
                        return;
                    }
                    message = jsonViewModel.getRetypePasswordMismatchByErrors();
                }
                Bundle bundleRoute = new Bundle();
                bundleRoute.putString("message", message);
                FragmentManager fragmentManager  = getFragmentManager();
                MessageInformationDialogFragment messageDialogFragment = MessageInformationDialogFragment.newInstance();
                messageDialogFragment.setCancelable(false);
                messageDialogFragment.setArguments(bundleRoute);
                messageDialogFragment.show(fragmentManager, "");
            }
        });

        homeWalletBackArrowImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (homeCreateRestoreWalletLinearLayout.getVisibility() == View.VISIBLE) {
                    homeCreateRestoreWalletLinearLayout.setVisibility(View.GONE);
                    homeSetWalletTopLinearLayout.setVisibility(View.GONE);
                    homeSetWalletLinearLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        homeCreateRestoreWalletRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                homeCreateRestoreWalletRadio = (int) radioButton.getTag();
            }
        });

        homeCreateRestoreWalletNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundleRoute = new Bundle();
                String message = "";

                if(homeCreateRestoreWalletRadioButton_0.isChecked() == true) {

                } else if(homeCreateRestoreWalletRadioButton_1.isChecked() == true) {

                }  else {
                    message = jsonViewModel.getSelectOptionByErrors();
                    bundleRoute.putString("message", message);
                    FragmentManager fragmentManager  = getFragmentManager();
                    MessageInformationDialogFragment messageDialogFragment = MessageInformationDialogFragment.newInstance();
                    messageDialogFragment.setCancelable(false);
                    messageDialogFragment.setArguments(bundleRoute);
                    messageDialogFragment.show(fragmentManager, "");
                }
            }
        });

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
        public abstract void OnHomeWalletComplete();
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

    private void SetWalletView(TextView setWalletTitleTextView, TextView setWalletDescriptionTextView,
                               TextView  setWalletPasswordTitleTextView, EditText setWalletPasswordEditText,
                               TextView setWalletRetypePasswordTitleTextView, EditText setWalletRetypePasswordEditText,
                               Button setWalletNextButton){

        setWalletTitleTextView.setText(jsonViewModel.getSetWalletPasswordByLangValues());
        setWalletDescriptionTextView.setText(jsonViewModel.getUseStrongPasswordByLangValues());

        setWalletPasswordTitleTextView.setText(jsonViewModel.getPasswordByLangValues());
        setWalletPasswordEditText.setHint(jsonViewModel.getEnterApasswordByLangValues());

        setWalletRetypePasswordTitleTextView.setText(jsonViewModel.getRetypePasswordByLangValues());
        setWalletRetypePasswordEditText.setHint(jsonViewModel.getRetypeThePasswordByLangValues());

        setWalletNextButton.setText(jsonViewModel.getNextByLangValues());
    }

    private void CreateRestoreWalletView(TextView createRestoreWalletTitleTextView, TextView createRestoreWalletDescriptionTextView,
                             RadioButton  createRestoreWalletRadioButton_0, RadioButton createRestoreWalletRadioButton_1,
                                         Button createRestoreWalletNextButton){

        createRestoreWalletTitleTextView.setText(jsonViewModel.getCreateRestoreWalletByLangValues());
        createRestoreWalletDescriptionTextView.setText(jsonViewModel.getSelectAnOptionByLangValues());

        createRestoreWalletRadioButton_0.setChecked(false);
        createRestoreWalletRadioButton_1.setChecked(false);

        createRestoreWalletRadioButton_0.setText(jsonViewModel.getCreateNewWalletByLangValues());
        createRestoreWalletRadioButton_1.setText(jsonViewModel.getRestoreWalletFromSeedByLangValues());

        createRestoreWalletRadioButton_0.setTag(0);
        createRestoreWalletRadioButton_1.setTag(1);

        createRestoreWalletNextButton.setText(jsonViewModel.getNextByLangValues());
    }
}
