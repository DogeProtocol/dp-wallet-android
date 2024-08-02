package com.dpwallet.app.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
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
import com.dpwallet.app.seedwords.SeedWords;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HomeWalletFragment extends Fragment {

    private static final String TAG = "HomeWalletFragment";

    private int homeCreateRestoreWalletRadio = -1;

    private JsonViewModel jsonViewModel;

    private KeyViewModel keyViewModel;
    private  int[] tempSeedArray;

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

        tempSeedArray = null;
        jsonViewModel = new JsonViewModel(getContext(), getArguments().getString("languageKey"));
        keyViewModel = new KeyViewModel();

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

        homeSetWalletPasswordEditText.setText("Test123$$Test123$$");
        homeSetWalletRetypePasswordEditText.setText("Test123$$Test123$$");

        LinearLayout homeCreateRestoreWalletLinearLayout = (LinearLayout) getView().findViewById(R.id.linear_layout_home_create_restore_wallet);
        TextView homeCreateRestoreWalletTitleTextView = (TextView) getView().findViewById(R.id.textView_home_create_restore_wallet_title);
        TextView homeCreateRestoreWalletDescriptionTextView = (TextView) getView().findViewById(R.id.textView_home_create_restore_wallet_description);

        RadioGroup homeCreateRestoreWalletRadioGroup = (RadioGroup) getView().findViewById(R.id.radioGroup_home_create_restore_wallet);
        RadioButton homeCreateRestoreWalletRadioButton_0 = (RadioButton) getView().findViewById(R.id.radioButton_home_create_restore_wallet_0);
        RadioButton homeCreateRestoreWalletRadioButton_1 = (RadioButton) getView().findViewById(R.id.radioButton_home_create_restore_wallet_1);
        Button homeCreateRestoreWalletNextButton = (Button) getView().findViewById(R.id.button_home_create_restore_wallet_next);

        LinearLayout homeSeedWordsLinearLayout = (LinearLayout) getView().findViewById(R.id.linear_layout_home_seed_words);
        TextView homeSeedWordsTitleTextView = (TextView) getView().findViewById(R.id.textView_home_seed_words_title);
        TextView homeSeedWords1 = (TextView) getView().findViewById(R.id.textView_home_seed_words_1);
        TextView homeSeedWords2 = (TextView) getView().findViewById(R.id.textView_home_seed_words_2);
        TextView homeSeedWords3 = (TextView) getView().findViewById(R.id.textView_home_seed_words_3);
        TextView homeSeedWords4 = (TextView) getView().findViewById(R.id.textView_home_seed_words_4);
        TextView homeSeedWordsShow = (TextView) getView().findViewById(R.id.textView_home_seed_words_show);

        LinearLayout homeSeedWordsViewLinearLayout = (LinearLayout) getView().findViewById(R.id.linear_layout_home_seed_words_view);
        TextView homeSeedWordsViewTitleTextView = (TextView) getView().findViewById(R.id.textView_home_seed_words_view_title);
        TextView[] homeSeedWordsViewTextViews = HomeSeedWordsViewTextViews();
        Button homeSeedWordsViewNextButton = (Button) getView().findViewById(R.id.button_home_seed_words_view_next);

        LinearLayout homeSeedWordsEditLinearLayout = (LinearLayout) getView().findViewById(R.id.linear_layout_home_seed_words_edit);
        TextView homeSeedWordsEditTitleTextView = (TextView) getView().findViewById(R.id.textView_home_seed_words_edit_title);
        EditText[] homeSeedWordsEditTextViews = HomeSeedWordsViewEditText();
        Button homeSeedWordsEditNextButton = (Button) getView().findViewById(R.id.button_home_seed_words_edit_next);

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
                                homeCreateRestoreWalletRadioButton_1, homeCreateRestoreWalletNextButton);
                        return;
                    }
                    message = jsonViewModel.getRetypePasswordMismatchByErrors();
                }
                Bundle bundleRoute = new Bundle();
                bundleRoute.putString("message", message);
                FragmentManager fragmentManager = getFragmentManager();
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
                if (homeSeedWordsLinearLayout.getVisibility() == View.VISIBLE) {
                    homeSeedWordsLinearLayout.setVisibility(View.GONE);
                    homeCreateRestoreWalletLinearLayout.setVisibility(View.VISIBLE);
                }
                if (homeSeedWordsViewLinearLayout.getVisibility() == View.VISIBLE) {
                    homeSeedWordsViewLinearLayout.setVisibility(View.GONE);
                    homeSeedWordsLinearLayout.setVisibility(View.VISIBLE);
                }
                if (homeSeedWordsEditLinearLayout.getVisibility() == View.VISIBLE) {
                    homeSeedWordsEditLinearLayout.setVisibility(View.GONE);
                    homeSeedWordsViewLinearLayout.setVisibility(View.VISIBLE);
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

                if (homeCreateRestoreWalletRadioButton_0.isChecked() == true) {
                    homeCreateRestoreWalletLinearLayout.setVisibility(View.GONE);
                    homeSeedWordsLinearLayout.setVisibility(View.VISIBLE);

                    tempSeedArray = null;

                    SeedWordsView(homeSeedWordsTitleTextView, homeSeedWords1, homeSeedWords2, homeSeedWords3, homeSeedWords4, homeSeedWordsShow);
                } else if (homeCreateRestoreWalletRadioButton_1.isChecked() == true) {

                } else {
                    message = jsonViewModel.getSelectOptionByErrors();
                    bundleRoute.putString("message", message);
                    FragmentManager fragmentManager = getFragmentManager();
                    MessageInformationDialogFragment messageDialogFragment = MessageInformationDialogFragment.newInstance();
                    messageDialogFragment.setCancelable(false);
                    messageDialogFragment.setArguments(bundleRoute);
                    messageDialogFragment.show(fragmentManager, "");
                }
            }
        });

        homeSeedWordsShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    public void run() {

                        while (true) {
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    if (GlobalMethods.seedLoaded) {
                                        try {
                                            ShowNewSeedScreen(homeSeedWordsViewTitleTextView, homeSeedWordsViewTextViews, homeSeedWordsViewNextButton);
                                            homeSeedWordsLinearLayout.setVisibility(View.GONE);
                                            homeSeedWordsViewLinearLayout.setVisibility(View.VISIBLE);
                                            progressBar.setVisibility(View.GONE);
                                        } catch (ServiceException e) {
                                            progressBar.setVisibility(View.GONE);
                                            GlobalMethods.ExceptionError(getContext(), TAG, e);
                                        }
                                    }
                                }
                            });
                            try {
                                if(homeSeedWordsViewLinearLayout.getVisibility() == View.VISIBLE){
                                   return;
                                }
                                Thread.sleep(1000);
                            } catch (Exception e) {
                                progressBar.setVisibility(View.GONE);
                                GlobalMethods.ExceptionError(getContext(), TAG, e);                            }
                        }
                    }
                }).start();
            }
        });


        homeSeedWordsViewNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeSeedWordsViewLinearLayout.setVisibility(View.GONE);
                homeSeedWordsEditLinearLayout.setVisibility(View.VISIBLE);
                ShowEditSeedScreen(homeSeedWordsEditTitleTextView, homeSeedWordsEditNextButton);

                for (int i = 0; i < homeSeedWordsViewTextViews.length; i++) {
                    homeSeedWordsEditTextViews[i].setText(homeSeedWordsViewTextViews[i].getText());
                }
            }
        });

        homeSeedWordsEditNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                String[] stringArray = Arrays.toString(tempSeedArray).split("[\\[\\]]")[1].split(", ");

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
                */

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
    public void onStop() {
        super.onStop();
    }

    public static interface OnHomeWalletCompleteListener {
        public abstract void OnHomeWalletComplete();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mHomeWalletListener = (OnHomeWalletCompleteListener) context;
        } catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " ");
        }
    }

    private void SetWalletView(TextView setWalletTitleTextView, TextView setWalletDescriptionTextView,
                               TextView setWalletPasswordTitleTextView, EditText setWalletPasswordEditText,
                               TextView setWalletRetypePasswordTitleTextView, EditText setWalletRetypePasswordEditText,
                               Button setWalletNextButton) {

        setWalletTitleTextView.setText(jsonViewModel.getSetWalletPasswordByLangValues());
        setWalletDescriptionTextView.setText(jsonViewModel.getUseStrongPasswordByLangValues());

        setWalletPasswordTitleTextView.setText(jsonViewModel.getPasswordByLangValues());
        setWalletPasswordEditText.setHint(jsonViewModel.getEnterApasswordByLangValues());

        setWalletRetypePasswordTitleTextView.setText(jsonViewModel.getRetypePasswordByLangValues());
        setWalletRetypePasswordEditText.setHint(jsonViewModel.getRetypeThePasswordByLangValues());

        setWalletNextButton.setText(jsonViewModel.getNextByLangValues());
    }

    private void CreateRestoreWalletView(TextView createRestoreWalletTitleTextView, TextView createRestoreWalletDescriptionTextView,
                                         RadioButton createRestoreWalletRadioButton_0, RadioButton createRestoreWalletRadioButton_1,
                                         Button createRestoreWalletNextButton) {

        createRestoreWalletTitleTextView.setText(jsonViewModel.getCreateRestoreWalletByLangValues());
        createRestoreWalletDescriptionTextView.setText(jsonViewModel.getSelectAnOptionByLangValues());

        //createRestoreWalletRadioButton_0.setChecked(false);
        //createRestoreWalletRadioButton_1.setChecked(false);

        createRestoreWalletRadioButton_0.setText(jsonViewModel.getCreateNewWalletByLangValues());
        createRestoreWalletRadioButton_1.setText(jsonViewModel.getRestoreWalletFromSeedByLangValues());

        createRestoreWalletRadioButton_0.setTag(0);
        createRestoreWalletRadioButton_1.setTag(1);

        createRestoreWalletNextButton.setText(jsonViewModel.getNextByLangValues());
    }

    private void SeedWordsView(TextView seedWordsTitleTextView, TextView seedWords1TextView,
                               TextView seedWords2TextView, TextView seedWords3TextView, TextView seedWords4TextView, TextView seedWordsShowTextView) {

        seedWordsTitleTextView.setText(jsonViewModel.getSeedWordsByLangValues());
        seedWords1TextView.setText(jsonViewModel.getSeedWordsInfo1ByLangValues());
        seedWords2TextView.setText(jsonViewModel.getSeedWordsInfo2ByLangValues());
        seedWords3TextView.setText(jsonViewModel.getSeedWordsInfo3ByLangValues());
        seedWords4TextView.setText(jsonViewModel.getSeedWordsInfo4ByLangValues());

        SpannableString content = new SpannableString(jsonViewModel.getSeedWordsShowByLangValues());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        seedWordsShowTextView.setText(content);
    }

    private TextView[] HomeSeedWordsViewTextViews() {
        TextView[] homeSeedWordsViewTextViews = new TextView[]{
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_a1),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_a2),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_a3),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_a4),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_b1),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_b2),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_b3),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_b4),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_c1),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_c2),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_c3),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_c4),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_d1),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_d2),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_d3),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_d4),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_e1),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_e2),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_e3),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_e4),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_f1),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_f2),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_f3),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_f4),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_g1),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_g2),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_g3),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_g4),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_h1),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_h2),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_h3),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_h4),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_i1),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_i2),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_i3),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_i4),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_j1),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_j2),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_j3),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_j4),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_k1),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_k2),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_k3),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_k4),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_l1),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_l2),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_l3),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_l4),

        };
        return homeSeedWordsViewTextViews;
    }

    private void ShowNewSeedScreen(TextView homeSeedWordsViewTitleTextView, TextView[] textViews,
                                   Button homeSeedWordsViewNextButton) throws ServiceException {

        homeSeedWordsViewTitleTextView.setText(jsonViewModel.getSeedWordsByLangValues());

        if(tempSeedArray == null) {
            tempSeedArray = cryptoNewSeed();
        }

        String[] stringArray = Arrays.toString(tempSeedArray).split("[\\[\\]]")[1].split(", ");
        //ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(stringArray));

        String[] wordList = GlobalMethods.seedWords.getWordListFromSeedArray(stringArray);

        if(wordList.length>0) {
            for (int i = 0; i < wordList.length; i++) {
               textViews[i].setText(wordList[i].toUpperCase());

            }
        }

        homeSeedWordsViewNextButton.setText(jsonViewModel.getNextByLangValues());
    }

    private EditText[] HomeSeedWordsViewEditText() {
        EditText[] homeSeedWordsViewEditText = new EditText[]{
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_a1),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_a2),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_a3),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_a4),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_b1),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_b2),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_b3),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_b4),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_c1),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_c2),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_c3),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_c4),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_d1),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_d2),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_d3),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_d4),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_e1),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_e2),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_e3),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_e4),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_f1),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_f2),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_f3),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_f4),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_g1),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_g2),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_g3),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_g4),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_h1),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_h2),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_h3),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_h4),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_i1),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_i2),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_i3),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_i4),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_j1),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_j2),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_j3),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_j4),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_k1),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_k2),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_k3),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_k4),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_l1),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_l2),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_l3),
                (EditText) getView().findViewById(R.id.editText_home_seed_words_edit_l4),
        };
        return homeSeedWordsViewEditText;
    }

    private void ShowEditSeedScreen(TextView homeSeedWordsEditTitleTextView, Button homeSeedWordsEditNextButton) {
        homeSeedWordsEditTitleTextView.setText(jsonViewModel.getVerifySeedWordsByLangValues());
        homeSeedWordsEditNextButton.setText(jsonViewModel.getNextByLangValues());
    }

    private boolean CheckSeed(TextView[] textViews, EditText[] editTexts, int index) {
        if (textViews[index].getText() != editTexts[index].getText()) {
            return false;
        }
        return true;
    }

    //Wallet
    private void walletCreateNewWallet() throws ServiceException {
        int[] seedArray = cryptoNewSeed();
        int[] wallet = walletKeyPairFromSeed(seedArray);

        //PrefConnect.writeString(getActivity(), PrefConnect.walletAddress, address);
        //progressBar.setVisibility(View.GONE);

      //  return wallet;
    }

    //private void walletCreateNewWalletFromSeed(int ) throws ServiceException {

    //}


    private void walletSave(int[] wallet, String password) throws ServiceException {
        int[] PK_KEY = (int[]) keyViewModel.publicKeyFromPrivateKey(wallet);
        String address = (String) keyViewModel.getAccountAddress(PK_KEY);
        keyViewModel.encryptDataByAccount(getActivity(), address, password, wallet, PK_KEY);
    }

    private int[] walletKeyPairFromSeed(int[] seedArray) throws ServiceException{
        int[] expandedSeedArray = cryptoExpandSeed(seedArray);
        return cryptoNewKeyPairFromSeed(expandedSeedArray);
    }

    //Crypto
    private int[] cryptoNewSeed() throws ServiceException {
        return (int[]) keyViewModel.random();
    }

    private int[] cryptoExpandSeed(int[] seedArray) throws ServiceException {
        return (int[]) keyViewModel.seedExpander(seedArray);
    }

    private int[] cryptoNewKeyPairFromSeed(int[] expandedSeedArray) throws ServiceException {
        return (int[]) keyViewModel.newAccountFromSeed(expandedSeedArray);
    }
}