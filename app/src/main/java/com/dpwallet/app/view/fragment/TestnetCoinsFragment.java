package com.dpwallet.app.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.dpwallet.app.R;
import com.dpwallet.app.api.faucet.ApiException;
import com.dpwallet.app.api.faucet.model.FaucetTransactionSummaryResponse;
import com.dpwallet.app.asynctask.faucet.FaucetTransactionRestTask;
import com.dpwallet.app.utils.GlobalMethods;

public class TestnetCoinsFragment extends Fragment  {

    private static final String TAG = "TestnetCoinsFragment";

    private LinearLayout linerLayoutOffline;
    private ImageView imageViewRetry;
    private TextView textViewTitleRetry;
    private TextView textViewSubTitleRetry;

    private OnTestnetCoinsCompleteListener mTestnetCoinsListener;

    public static TestnetCoinsFragment newInstance() {
        TestnetCoinsFragment fragment = new TestnetCoinsFragment();
        return fragment;
    }

    public TestnetCoinsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.testnet_coins_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView skip = (TextView) getView().findViewById(R.id.textView_testnet_coins_skip);
        Button testnetCoins = (Button) getView().findViewById(R.id.button_testnet_coins_request);

        ProgressBar progressBar = (ProgressBar) getView().findViewById(R.id.progress_loader_testnet_coins);

        linerLayoutOffline = (LinearLayout) getView().findViewById(R.id.linerLayout_testnet_offline);
        imageViewRetry = (ImageView) getView().findViewById(R.id.image_retry);
        textViewTitleRetry = (TextView) getView().findViewById(R.id.textview_title_retry);
        textViewSubTitleRetry = (TextView) getView().findViewById(R.id.textview_subtitle_retry);
        Button buttonRetry = (Button) getView().findViewById(R.id.button_retry);

        String walletAddress = getArguments().getString("walletAddress");

        skip.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mTestnetCoinsListener.onTestnetCoinsComplete();
            }
        });

        testnetCoins.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (progressBar.getVisibility() == View.VISIBLE) {
                    String message = getResources().getString(R.string.testnet_coins_message_exits);
                    GlobalMethods.ShowToast(getContext(), message);
                } else {
                    RequestTestnetCoins(walletAddress, progressBar);
                }
            }
        });

        buttonRetry.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if (progressBar.getVisibility() == View.VISIBLE) {
                    String message = getResources().getString(R.string.testnet_coins_message_exits);
                    GlobalMethods.ShowToast(getContext(), message);
                } else {
                    RequestTestnetCoins(walletAddress, progressBar);
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

    public static interface OnTestnetCoinsCompleteListener {
        public abstract void onTestnetCoinsComplete();
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mTestnetCoinsListener = (OnTestnetCoinsCompleteListener)context;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " ");
        }
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
                    public void onFinished(FaucetTransactionSummaryResponse faucetTransactionSummaryResponse)  {
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
                                mTestnetCoinsListener.onTestnetCoinsComplete();
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
                                    TAG + " : FaucetTransactionRestTask : " + e.toString());
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
