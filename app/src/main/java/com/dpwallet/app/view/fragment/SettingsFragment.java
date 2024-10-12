package com.dpwallet.app.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.dpwallet.app.R;
import com.dpwallet.app.viewmodel.JsonViewModel;
import com.dpwallet.app.viewmodel.KeyViewModel;

public class SettingsFragment extends Fragment  {

    private static final String TAG = "SettingsFragment";

    private LinearLayout linerLayoutOffline;
    private ImageView imageViewRetry;
    private TextView textViewTitleRetry;
    private TextView textViewSubTitleRetry;


    private OnSettingsCompleteListener mSettingsListener;

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    public SettingsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String languageKey = getArguments().getString("languageKey");

        JsonViewModel jsonViewModel = new JsonViewModel(getContext(), languageKey);

        ImageButton backArrowImageButton = (ImageButton) getView().findViewById(R.id.imageButton_setting_back_arrow);
        TextView settings = (TextView) getView().findViewById(R.id.textview_settings_langValues_settings);
        Button buttonGetCoins = (Button) getView().findViewById(R.id.button_settings_langValues_get_coins_for_dogep_tokens);
        Button buttonNetworks = (Button) getView().findViewById(R.id.button_settings_langValues_networks);

        settings.setText(jsonViewModel.getSettingsByLangValues());
        buttonGetCoins.setText(jsonViewModel.getGetCoinsForDogePTokensByLangValues());
        buttonNetworks.setText(jsonViewModel.getNetworksByLangValues());

        buttonGetCoins.setVisibility(View.GONE);

        linerLayoutOffline = (LinearLayout) getView().findViewById(R.id.linerLayout_setting_offline);
        imageViewRetry = (ImageView) getView().findViewById(R.id.image_retry);
        textViewTitleRetry = (TextView) getView().findViewById(R.id.textview_title_retry);
        textViewSubTitleRetry = (TextView) getView().findViewById(R.id.textview_subtitle_retry);
        Button buttonRetry = (Button) getView().findViewById(R.id.button_retry);

        backArrowImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mSettingsListener.onSettingsCompleteCompleteByBackArrow();
            }
        });

        buttonGetCoins.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                mSettingsListener.onSettingsCompleteByGetCoin();
            }
        });

        buttonNetworks.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                mSettingsListener.onSettingsCompleteByNetwork();
            }
        });

        buttonRetry.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                mSettingsListener.onSettingsCompleteCompleteByBackArrow();
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

    public static interface OnSettingsCompleteListener {
        public abstract void onSettingsCompleteCompleteByBackArrow();
        public abstract void onSettingsCompleteByNetwork();
        public abstract void onSettingsCompleteByGetCoin();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mSettingsListener = (OnSettingsCompleteListener)context;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " ");
        }
    }
}
