package com.dpwallet.app.view.fragment;

import static android.content.Context.CLIPBOARD_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dpwallet.app.R;
import com.dpwallet.app.entity.ServiceException;
import com.dpwallet.app.utils.GlobalMethods;
import com.dpwallet.app.utils.PrefConnect;
import com.dpwallet.app.viewmodel.JsonViewModel;
import com.dpwallet.app.viewmodel.KeyViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import com.dpwallet.app.view.adapter.SeedWordAutoCompleteAdapter;

public class HomeWalletFragment extends Fragment {

    private static final String TAG = "HomeWalletFragment";

    private int homeCreateRestoreWalletRadio = -1;

    private JsonViewModel jsonViewModel;

    private KeyViewModel keyViewModel;
    private  int[] tempSeedArrayByCreate;
    private SeedWordAutoCompleteAdapter seedWordAutoCompleteAdapter;
   // private TextView[] homeSeedWordsViewTextViews;
    private AutoCompleteTextView[] homeSeedWordsViewAutoCompleteTextViews;
    private boolean autoCompleteIndexStatus = false;
    private int autoCompleteCurrentIndex = 0;
    private  String  walletPassword = null;
    private String walletIndexKey = "0";
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

        walletPassword = getArguments().getString("walletPassword");
        String languageKey = getArguments().getString("languageKey");

        tempSeedArrayByCreate = null;
        jsonViewModel = new JsonViewModel(getContext(), languageKey);
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

        ////homeSetWalletPasswordEditText.setText("Test123$$Test123$$");
        ////homeSetWalletRetypePasswordEditText.setText("Test123$$Test123$$");

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
        TextView[] homeSeedWordsViewCaptionTextViews = HomeSeedWordsViewCaptionTextViews();
        TextView[] homeSeedWordsViewTextViews = HomeSeedWordsViewTextViews();
        ImageButton homeSeedWordsViewCopyClipboardImageButton = (ImageButton) getView().findViewById(R.id.imageButton_home_seed_words_view_copy_clipboard);
        Button homeSeedWordsViewNextButton = (Button) getView().findViewById(R.id.button_home_seed_words_view_next);

        LinearLayout homeSeedWordsEditLinearLayout = (LinearLayout) getView().findViewById(R.id.linear_layout_home_seed_words_edit);
        TextView homeSeedWordsEditTitleTextView = (TextView) getView().findViewById(R.id.textView_home_seed_words_edit_title);
        homeSeedWordsViewAutoCompleteTextViews = HomeSeedWordsViewAutoCompleteTextView();

        int index=0;
        for (AutoCompleteTextView homeSeedWordsViewAutoCompleteTextView : homeSeedWordsViewAutoCompleteTextViews) {
            homeSeedWordsViewAutoCompleteTextView.addTextChangedListener(GetTextWatcher(homeSeedWordsViewAutoCompleteTextView, index));
            index = index + 1;
            homeSeedWordsViewAutoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void  onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                        if(autoCompleteIndexStatus == true){
                            if(homeSeedWordsViewAutoCompleteTextViews[autoCompleteCurrentIndex].getText().length() < 3){
                                homeSeedWordsViewAutoCompleteTextViews[autoCompleteCurrentIndex].requestFocus();
                            }
                        }
                        autoCompleteIndexStatus = false;
                    } else {
                        if(homeSeedWordsViewAutoCompleteTextView.length()>2) {
                            if(tempSeedArrayByCreate != null) {
                                if (!homeSeedWordsViewAutoCompleteTextView.getText().toString().equalsIgnoreCase(homeSeedWordsViewTextViews[autoCompleteCurrentIndex].getText().toString())) {
                                    homeSeedWordsViewAutoCompleteTextView.setText("");
                                    autoCompleteIndexStatus = true;
                                }
                            }
                        }
                    }
                }
            });
        }

        Button homeSeedWordsAutoCompleteNextButton = (Button) getView().findViewById(R.id.button_home_seed_words_autoComplete_next);

        ProgressBar progressBar = (ProgressBar) getView().findViewById(R.id.progress_loader_home_wallet);

        if (walletPassword==null || walletPassword.isEmpty()) {
            homeSetWalletLinearLayout.setVisibility(View.VISIBLE);
            SetWalletView(homeSetWalletTitleTextView, homeSetWalletDescriptionTextView, homeSetWalletPasswordTitleTextView,
                    homeSetWalletPasswordEditText, homeSetWalletRetypePasswordTitleTextView, homeSetWalletRetypePasswordEditText, homeSetWalletNextButton);
        } else {
            homeSetWalletLinearLayout.setVisibility(View.GONE);
            homeSetWalletTopLinearLayout.setVisibility(View.VISIBLE);
            homeCreateRestoreWalletLinearLayout.setVisibility(View.VISIBLE);
            CreateRestoreWalletView(homeCreateRestoreWalletTitleTextView, homeCreateRestoreWalletDescriptionTextView, homeCreateRestoreWalletRadioButton_0,
                    homeCreateRestoreWalletRadioButton_1, homeCreateRestoreWalletNextButton);
        }

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
                bundleRoute.putString("languageKey",languageKey);
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
                    if (homeCreateRestoreWalletLinearLayout.getVisibility()==View.VISIBLE) {
                        if (walletPassword==null || walletPassword.isEmpty()) {
                            homeCreateRestoreWalletLinearLayout.setVisibility(View.GONE);
                            homeSetWalletTopLinearLayout.setVisibility(View.GONE);
                            homeSetWalletLinearLayout.setVisibility(View.VISIBLE);
                        } else {
                            mHomeWalletListener.onHomeWalletCompleteByWallets();
                        }
                    }
                    if (homeSeedWordsLinearLayout.getVisibility()==View.VISIBLE) {
                        homeSeedWordsLinearLayout.setVisibility(View.GONE);
                        homeCreateRestoreWalletLinearLayout.setVisibility(View.VISIBLE);
                    }
                    if (homeSeedWordsViewLinearLayout.getVisibility()==View.VISIBLE) {
                        homeSeedWordsViewLinearLayout.setVisibility(View.GONE);
                        homeSeedWordsLinearLayout.setVisibility(View.VISIBLE);
                    }
                    if (homeSeedWordsEditLinearLayout.getVisibility()==View.VISIBLE) {
                        if (homeCreateRestoreWalletRadioButton_0.isChecked()==true){
                            homeSeedWordsEditLinearLayout.setVisibility(View.GONE);
                            homeSeedWordsViewLinearLayout.setVisibility(View.VISIBLE);
                        }
                        if (homeCreateRestoreWalletRadioButton_1.isChecked()==true){
                            homeSeedWordsEditLinearLayout.setVisibility(View.GONE);
                            homeCreateRestoreWalletLinearLayout.setVisibility(View.VISIBLE);
                        }
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
                    tempSeedArrayByCreate = null;
                    homeCreateRestoreWalletLinearLayout.setVisibility(View.GONE);
                    homeSeedWordsLinearLayout.setVisibility(View.VISIBLE);
                    SeedWordsView(homeSeedWordsTitleTextView, homeSeedWords1, homeSeedWords2, homeSeedWords3, homeSeedWords4, homeSeedWordsShow);
                } else if (homeCreateRestoreWalletRadioButton_1.isChecked() == true) {
                    tempSeedArrayByCreate = null;
                    homeCreateRestoreWalletLinearLayout.setVisibility(View.GONE);
                    homeSeedWordsEditLinearLayout.setVisibility(View.VISIBLE);
                    ShowEditSeedScreen(homeSeedWordsEditTitleTextView, homeSeedWordsAutoCompleteNextButton);

                    homeSeedWordsViewAutoCompleteTextViews[autoCompleteCurrentIndex].requestFocus();

                    ArrayList<String> seedWordsList = GlobalMethods.seedWords.getAllSeedWords();
                    seedWordAutoCompleteAdapter = new SeedWordAutoCompleteAdapter(getContext(), android.R.layout.simple_dropdown_item_1line,
                            android.R.id.text1, seedWordsList);

                    ////
                    /*
                    for (int index = 0; index < 48; index++){
                        homeSeedWordsViewAutoCompleteTextViews[index].setText(seedWordsList.get(index));
                    }*/

                } else {
                    message = jsonViewModel.getSelectOptionByErrors();
                    bundleRoute.putString("languageKey",languageKey);
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
                if(progressBar.getVisibility() == View.VISIBLE){
                    return;
                }
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
                                GlobalMethods.ExceptionError(getContext(), TAG, e);
                            }
                        }
                    }
                }).start();
            }
        });

        homeSeedWordsViewCopyClipboardImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String clipboardCopyData = ClipboardCopyData(homeSeedWordsViewCaptionTextViews, homeSeedWordsViewTextViews);
                ClipboardManager clipBoard = (ClipboardManager) getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("walletSeed", clipboardCopyData);
                clipBoard.setPrimaryClip(clipData);
                progressBar.setVisibility(View.GONE);
            }
        });

        homeSeedWordsViewNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeSeedWordsViewLinearLayout.setVisibility(View.GONE);
                homeSeedWordsEditLinearLayout.setVisibility(View.VISIBLE);
                ShowEditSeedScreen(homeSeedWordsEditTitleTextView, homeSeedWordsAutoCompleteNextButton);

                homeSeedWordsViewAutoCompleteTextViews[autoCompleteCurrentIndex].requestFocus();

                ArrayList<String> seedWordsList = GlobalMethods.seedWords.getAllSeedWords();
                seedWordAutoCompleteAdapter = new SeedWordAutoCompleteAdapter(getContext(), android.R.layout.simple_dropdown_item_1line,
                        android.R.id.text1, seedWordsList);

////
                //for (int index = 0; index < 48; index++){
                //    homeSeedWordsViewAutoCompleteTextViews[index].setText(homeSeedWordsViewTextViews[index].getText());
                //}
            }
        });


        homeSeedWordsAutoCompleteNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    progressBar.setVisibility(View.VISIBLE);

                    if(walletPassword==null || walletPassword.isEmpty()) {
                        walletPassword = homeSetWalletPasswordEditText.getText().toString();
                    }

                    String[] seedWords=new String[homeSeedWordsViewAutoCompleteTextViews.length];
                    int index=0;
                    for (AutoCompleteTextView homeSeedWordsViewAutoCompleteTextView : homeSeedWordsViewAutoCompleteTextViews) {
                        if(homeSeedWordsViewAutoCompleteTextView.getText().length()==0){
                            homeSeedWordsViewAutoCompleteTextView.requestFocus();
                            progressBar.setVisibility(View.GONE);
                            return;
                        }
                        if(!GlobalMethods.seedWords.doesSeedWordExist(homeSeedWordsViewAutoCompleteTextView.getText().toString())){
                            homeSeedWordsViewAutoCompleteTextView.setText("");
                            progressBar.setVisibility(View.GONE);
                            return;
                        }
                        if(!homeSeedWordsViewAutoCompleteTextView.getText().toString().equals(homeSeedWordsViewTextViews[index].getText().toString()) &&
                            tempSeedArrayByCreate != null){
                            homeSeedWordsViewAutoCompleteTextView.setText("");
                            progressBar.setVisibility(View.GONE);
                            return;
                        }
                        seedWords[index] = homeSeedWordsViewAutoCompleteTextView.getText().toString().toLowerCase();
                        index = index +1;
                    }

                    int[] seed = GlobalMethods.GetIntDataArrayByStringArray(GlobalMethods.seedWords.getSeedArrayFromSeedWordList(seedWords));

                    for(int i=0; i<seedWords.length;i++) {
                        if (!GlobalMethods.seedWords.verifySeedWord(i, seedWords[i], seed)) {
                            progressBar.setVisibility(View.GONE);
                            return;
                        }
                        if(tempSeedArrayByCreate != null) {
                            if(tempSeedArrayByCreate[i] != seed[i]){
                                progressBar.setVisibility(View.GONE);
                                return;
                            }
                        }
                    }

                    String[] stringArray = Arrays.toString(seed).split("[\\[\\]]")[1].split(", ");

                    String jsonText = TextUtils.join(",",stringArray);

                    //Expand seed array
                    int[] expandSeed = GlobalMethods.GetIntDataArrayByString(keyViewModel.cryptoExpandSeed(seed));
                    String[] keyPair = keyViewModel.cryptoNewKeyPairFromSeed(expandSeed);
                    keyPair[2]= jsonText;

                    int[] PK_KEY =  GlobalMethods.GetIntDataArrayByString(keyPair[1]);

                    String address =  keyViewModel.getAccountAddress(PK_KEY);

                    String passwordSHA256 = PrefConnect.getSha256Hash(walletPassword);
                    keyViewModel.encryptDataByString(getContext(), PrefConnect.WALLET_KEY_PASSWORD, walletPassword, passwordSHA256);
                    keyViewModel.encryptDataByAccount(getContext(), address, walletPassword, keyPair);

                    if(PrefConnect.WALLET_ADDRESS_TO_INDEX_MAP != null){
                        walletIndexKey = String.valueOf(PrefConnect.WALLET_ADDRESS_TO_INDEX_MAP.size());
                    }
                    PrefConnect.WALLET_ADDRESS_TO_INDEX_MAP.put(address, walletIndexKey);

                    PrefConnect.WALLET_INDEX_TO_ADDRESS_MAP.put(walletIndexKey, address);

                    PrefConnect.saveHasMap(getContext(),
                            PrefConnect.WALLET_KEY_PREFIX + PrefConnect.WALLET_KEY_ADDRESS_INDEX, PrefConnect.WALLET_ADDRESS_TO_INDEX_MAP);

                    PrefConnect.saveHasMap(getContext(),
                            PrefConnect.WALLET_KEY_PREFIX + PrefConnect.WALLET_KEY_INDEX_ADDRESS, PrefConnect.WALLET_INDEX_TO_ADDRESS_MAP);

                    progressBar.setVisibility(View.GONE);

                    mHomeWalletListener.onHomeWalletCompleteByHomeMain(walletIndexKey);

                } catch (ServiceException e) {
                    progressBar.setVisibility(View.GONE);
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
    public void onStop() {
        super.onStop();
    }

    public static interface OnHomeWalletCompleteListener {
        public abstract void onHomeWalletCompleteByHomeMain(String indexKey);
        public abstract void onHomeWalletCompleteByWallets();

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

    private TextView[] HomeSeedWordsViewCaptionTextViews() {
        TextView[] homeSeedWordsViewCaptionTextViews = new TextView[]{
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_a1),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_a2),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_a3),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_a4),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_b1),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_b2),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_b3),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_b4),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_c1),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_c2),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_c3),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_c4),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_d1),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_d2),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_d3),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_d4),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_e1),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_e2),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_e3),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_e4),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_f1),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_f2),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_f3),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_f4),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_g1),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_g2),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_g3),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_g4),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_h1),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_h2),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_h3),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_h4),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_i1),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_i2),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_i3),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_i4),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_j1),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_j2),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_j3),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_j4),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_k1),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_k2),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_k3),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_k4),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_l1),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_l2),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_l3),
                (TextView) getView().findViewById(R.id.textView_home_seed_words_view_caption_l4),

        };
        return homeSeedWordsViewCaptionTextViews;
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

        if(tempSeedArrayByCreate == null) {
            tempSeedArrayByCreate = GlobalMethods.GetIntDataArrayByString(keyViewModel.cryptoNewSeed());
        }

        String[] stringArray = Arrays.toString(tempSeedArrayByCreate).split("[\\[\\]]")[1].split(", ");

        String[] wordList = GlobalMethods.seedWords.getWordListFromSeedArray(stringArray);

        if(wordList.length>0) {
            for (int i = 0; i < wordList.length; i++) {
               textViews[i].setText(wordList[i].toUpperCase());
            }
        }

        homeSeedWordsViewNextButton.setText(jsonViewModel.getNextByLangValues());
    }

    private AutoCompleteTextView[] HomeSeedWordsViewAutoCompleteTextView() {
        return new AutoCompleteTextView[]{
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_a1),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_a2),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_a3),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_a4),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_b1),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_b2),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_b3),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_b4),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_c1),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_c2),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_c3),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_c4),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_d1),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_d2),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_d3),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_d4),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_e1),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_e2),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_e3),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_e4),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_f1),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_f2),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_f3),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_f4),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_g1),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_g2),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_g3),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_g4),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_h1),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_h2),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_h3),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_h4),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_i1),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_i2),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_i3),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_i4),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_j1),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_j2),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_j3),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_j4),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_k1),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_k2),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_k3),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_k4),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_l1),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_l2),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_l3),
                (AutoCompleteTextView) getView().findViewById(R.id.autoComplete_home_seed_words_textView_l4)
        };
    }

    private String ClipboardCopyData(TextView[] homeSeedWordsViewCaptionTextViews, TextView[] homeSeedWordsViewTextViews){
        String copyData = "";
        for (int i=0; i<homeSeedWordsViewCaptionTextViews.length; i++) {
            copyData = copyData + homeSeedWordsViewCaptionTextViews[i].getText() + " = " +  homeSeedWordsViewTextViews[i].getText() + "\n";
        }
        return copyData.toString();
    }


    private TextWatcher GetTextWatcher(final AutoCompleteTextView autoCompleteTextView, int index) {
        return new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //autoCompleteTextView.setDropDownWidth(getResources().getDisplayMetrics().widthPixels);
                autoCompleteTextView.setAdapter(seedWordAutoCompleteAdapter);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                autoCompleteCurrentIndex = index;
            }
            @Override
            public void afterTextChanged(Editable s) {
                //if(autoCompleteTextView.getId() == R.id.autoComplete_home_seed_words_textView_a1){
                //}
            }
        };
    }

    private void ShowEditSeedScreen(TextView homeSeedWordsEditTitleTextView, Button homeSeedWordsEditNextButton) {
        homeSeedWordsEditTitleTextView.setText(jsonViewModel.getVerifySeedWordsByLangValues());
        homeSeedWordsEditNextButton.setText(jsonViewModel.getNextByLangValues());
    }

    private boolean CheckSeed(@NonNull TextView[] textViews, EditText[] editTexts, int index) {
        if (textViews[index].getText() != editTexts[index].getText()) {
            return false;
        }
        return true;
    }
}