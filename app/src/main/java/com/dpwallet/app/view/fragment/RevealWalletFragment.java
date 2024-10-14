package com.dpwallet.app.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dpwallet.app.R;
import com.dpwallet.app.entity.KeyServiceException;
import com.dpwallet.app.entity.ServiceException;
import com.dpwallet.app.seedwords.SeedWords;
import com.dpwallet.app.utils.GlobalMethods;
import com.dpwallet.app.utils.PrefConnect;
import com.dpwallet.app.view.adapter.SeedWordAutoCompleteAdapter;
import com.dpwallet.app.viewmodel.JsonViewModel;
import com.dpwallet.app.viewmodel.KeyViewModel;

import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Arrays;

public class RevealWalletFragment extends Fragment {

    private static final String TAG = "RevealSeedWalletFragment";

    private boolean ThreadStop = false;

    private OnRevealWalletCompleteListener mRevealWalletListener;

    public static RevealWalletFragment newInstance() {
        RevealWalletFragment fragment = new RevealWalletFragment();
        return fragment;
    }

    public RevealWalletFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reveal_wallet_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String languageKey = getArguments().getString("languageKey");
        String walletAddress  = getArguments().getString("walletAddress");
        String walletPassword = getArguments().getString("walletPassword");

        JsonViewModel jsonViewModel = new JsonViewModel(getContext(), languageKey);

        //LinearLayout revealSetWalletTopLinearLayout = (LinearLayout) getView().findViewById(R.id.top_linear_layout_reveal_seed_wallet_id);
        ImageButton homeWalletBackArrowImageButton = (ImageButton) getView().findViewById(R.id.imageButton_reveal_seed_wallet_back_arrow);

        //LinearLayout revealSeedWordsViewLinearLayout = (LinearLayout) getView().findViewById(R.id.linear_layout_reveal_seed_words_view);
        TextView revealSeedWordsViewTitleTextView = (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_title);
        TextView[] revealSeedWordsViewTextViews = RevealSeedWordsViewTextViews();

        ProgressBar progressBar = (ProgressBar) getView().findViewById(R.id.progress_loader_reveal_seed_wallet);

        try {
            ShowRevealSeedScreen(jsonViewModel,
                    revealSeedWordsViewTitleTextView, revealSeedWordsViewTextViews, progressBar, walletAddress, walletPassword);

        } catch (KeyServiceException e) {
            progressBar.setVisibility(View.GONE);
            GlobalMethods.ExceptionError(getContext(), TAG, e);
        } catch (InvalidKeyException e) {
            progressBar.setVisibility(View.GONE);
            GlobalMethods.ExceptionError(getContext(), TAG, e);
        }
        homeWalletBackArrowImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    ThreadStop = true;
                    Thread.sleep(1000);
                    mRevealWalletListener.onRevealWalletComplete();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
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

    public static interface OnRevealWalletCompleteListener {
        public abstract void onRevealWalletComplete();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mRevealWalletListener = (OnRevealWalletCompleteListener) context;
        } catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " ");
        }
    }

    private void ShowRevealSeedScreen(JsonViewModel jsonViewModel,
                                     TextView revealSeedWordsViewTitleTextView, TextView[] textViews,
                                   ProgressBar progressBar,String walletAddress, String walletPassword) throws
            KeyServiceException, InvalidKeyException {

        revealSeedWordsViewTitleTextView.setText(jsonViewModel.getSeedWordsByLangValues());

        KeyViewModel keyViewModel = new KeyViewModel();
        String[] wallet = keyViewModel.decryptDataByAccount(getContext(),walletAddress, walletPassword);

        String[] stringArray = Arrays.toString(GlobalMethods.GetIntDataArrayByString(wallet[2])).split("[\\[\\]]")[1].split(", ");
        loadRevealSeedsThread(stringArray, textViews, progressBar);
    }

    private void loadRevealSeedsThread(String[] stringArray, TextView[] textViews, ProgressBar progressBar) {
            progressBar.setVisibility(View.VISIBLE);
            new Thread(new Runnable() {
                public void run() {
                    while (true) {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                if (GlobalMethods.seedLoaded) {
                                    try {
                                        String[] wordList = GlobalMethods.seedWords.getWordListFromSeedArray(stringArray);
                                        for (int i = 0; i < wordList.length; i++) {
                                            textViews[i].setText(wordList[i].toUpperCase());
                                        }
                                        progressBar.setVisibility(View.GONE);
                                    } catch (Exception e) {
                                        progressBar.setVisibility(View.GONE);
                                        GlobalMethods.ExceptionError(getContext(), TAG, e);
                                    }
                                }
                            }
                        });
                        try {
                            if(progressBar.getVisibility() == View.GONE){
                                return;
                            }
                            if(ThreadStop){
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

    private TextView[]RevealSeedWordsViewTextViews() {
        TextView[] revealSeedWordsViewTextViews = new TextView[]{
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_a1),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_a2),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_a3),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_a4),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_b1),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_b2),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_b3),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_b4),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_c1),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_c2),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_c3),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_c4),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_d1),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_d2),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_d3),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_d4),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_e1),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_e2),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_e3),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_e4),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_f1),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_f2),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_f3),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_f4),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_g1),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_g2),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_g3),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_g4),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_h1),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_h2),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_h3),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_h4),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_i1),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_i2),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_i3),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_i4),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_j1),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_j2),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_j3),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_j4),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_k1),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_k2),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_k3),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_k4),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_l1),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_l2),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_l3),
                (TextView) getView().findViewById(R.id.textView_reveal_seed_words_view_l4),

        };
        return revealSeedWordsViewTextViews;
    }

}