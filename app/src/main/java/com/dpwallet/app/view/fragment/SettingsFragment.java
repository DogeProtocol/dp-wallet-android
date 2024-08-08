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
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dpwallet.app.R;
import com.dpwallet.app.api.faucet.ApiException;
import com.dpwallet.app.api.faucet.model.FaucetTransactionSummaryResponse;
import com.dpwallet.app.asynctask.faucet.FaucetTransactionRestTask;
import com.dpwallet.app.utils.GlobalMethods;
import com.dpwallet.app.viewmodel.KeyViewModel;

public class SettingsFragment extends Fragment  {

    private static final String TAG = "SettingsFragment";

    private KeyViewModel keyViewModel = new KeyViewModel();

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

        ImageButton backArrowImageButton = (ImageButton) getView().findViewById(R.id.imageButton_setting_back_arrow);
        Button exportWallet = (Button) getView().findViewById(R.id.button_settings_export_wallet);
        Button requestTestCoins = (Button) getView().findViewById(R.id.button_settings_request_test_coins);
        Button deleteWallet = (Button) getView().findViewById(R.id.button_settings_delete_wallet);

        ProgressBar progressBarRequestTestCoins = (ProgressBar) getView().findViewById(R.id.progress_settings_request_test_loader);
        ProgressBar progressBarDeleteWallet = (ProgressBar) getView().findViewById(R.id.progress_settings_delete_wallet_loader);

        linerLayoutOffline = (LinearLayout) getView().findViewById(R.id.linerLayout_setting_offline);
        imageViewRetry = (ImageView) getView().findViewById(R.id.image_retry);
        textViewTitleRetry = (TextView) getView().findViewById(R.id.textview_title_retry);
        textViewSubTitleRetry = (TextView) getView().findViewById(R.id.textview_subtitle_retry);
        Button buttonRetry = (Button) getView().findViewById(R.id.button_retry);

        String walletAddress = getArguments().getString("walletAddress");

        backArrowImageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(walletAddress.trim().length() != 0) {
                    mSettingsListener.onSettingsComplete(0);
                } else {
                    mSettingsListener.onSettingsComplete(3);
                }
            }
        });

        exportWallet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(walletAddress.trim().length() != 0)
                {
                    mSettingsListener.onSettingsComplete(1);
                    return;
                }
                showInformationDialog();
            }
        });

        requestTestCoins.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(walletAddress.trim().length() != 0)
                {
                    RequestTestnetCoins(walletAddress, progressBarRequestTestCoins);
                    return;
                }
                showInformationDialog();
            }
        });

        buttonRetry.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                RequestTestnetCoins(walletAddress, progressBarRequestTestCoins);
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
        public abstract void onSettingsComplete(int status);
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

    private void showInformationDialog(){
        Bundle bundleRoute = new Bundle();
        bundleRoute.putString("message", getResources().getString(R.string.setting_message_description));
        FragmentManager fragmentManager  = getFragmentManager();
        MessageInformationDialogFragment messageDialogFragment = MessageInformationDialogFragment.newInstance();
        messageDialogFragment.setCancelable(false);
        messageDialogFragment.setArguments(bundleRoute);
        messageDialogFragment.show(fragmentManager, "");
    }

    private void RequestTestnetCoins(String address, ProgressBar progressBar){
        try{
            linerLayoutOffline.setVisibility(View.GONE);

            if (progressBar.getVisibility() == View.VISIBLE) {
                String message = getResources().getString(R.string.setting_dialog_message_exits);
                GlobalMethods.ShowToast(getContext(), message);
                return;
            }

            //Internet connection check
            if (GlobalMethods.IsNetworkAvailable(getContext())) {
                progressBar.setVisibility(View.VISIBLE);
                String[] taskParams = { address };

                FaucetTransactionRestTask task = new FaucetTransactionRestTask(
                        getContext(), new FaucetTransactionRestTask.TaskListener() {

                    @Override
                    public void onFinished(FaucetTransactionSummaryResponse faucetTransactionSummaryResponse) {
                        AlertDialog dialog = new AlertDialog.Builder(getContext())
                                .setTitle((CharSequence) "").setView((int)
                                        R.layout.request_coins_alert_dialog_fragment).create();
                        dialog.setCancelable(false);
                        dialog.show();

                        TextView textViewOk = (TextView) dialog.findViewById(
                                R.id.textView_request_coins_alert_dialog_ok);

                        textViewOk.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                progressBar.setVisibility(View.GONE);
                                dialog.dismiss();
                                mSettingsListener.onSettingsComplete(2);
                            }
                        });
                    }

                    @Override
                    public void onFailure(ApiException e) {
                        progressBar.setVisibility(View.GONE);
                        int code = e.getCode();
                        boolean check = GlobalMethods.ApiExceptionSourceCodeBoolean(code);
                        if(check == true) {
                            GlobalMethods.ApiExceptionSourceCodeRoute(getContext(), code,
                                    getString(R.string.apierror),
                                    TAG + " : AccountBalanceRestTask : " + e.toString());
                        } else {
                            GlobalMethods.OfflineOrExceptionError(getContext(),
                                    linerLayoutOffline, imageViewRetry, textViewTitleRetry,
                                    textViewSubTitleRetry, true);
                        }
                    }

                });

                task.execute(taskParams);
            } else {
                GlobalMethods.OfflineOrExceptionError(getContext(),
                        linerLayoutOffline, imageViewRetry, textViewTitleRetry,
                        textViewSubTitleRetry, false);
            }
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getContext(), TAG, e);
        }
    }
}
