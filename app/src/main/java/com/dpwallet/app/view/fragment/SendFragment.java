package com.dpwallet.app.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dpwallet.app.R;
import com.dpwallet.app.api.read.model.BalanceResponse;
import com.dpwallet.app.api.write.model.TransactionSummaryResponse;
import com.dpwallet.app.asynctask.read.AccountBalanceRestTask;
import com.dpwallet.app.asynctask.write.TransactionRestTask;
import com.dpwallet.app.entity.KeyServiceException;
import com.dpwallet.app.entity.ServiceException;
import com.dpwallet.app.utils.GlobalMethods;
import com.dpwallet.app.viewmodel.KeyViewModel;
import com.google.android.gms.common.util.Strings;

import java.security.InvalidKeyException;

public class SendFragment extends Fragment  {

    private static final String TAG = "SendFragment";

    private int retryStatus = 0;

    private LinearLayout linerLayoutOffline;
    private ImageView imageViewRetry;
    private TextView textViewTitleRetry;
    private TextView textViewSubTitleRetry;

    private SendFragment.OnSendCompleteListener mSendListener;

    private  KeyViewModel keyViewModel = new KeyViewModel();

    public static SendFragment newInstance() {
        SendFragment fragment = new SendFragment();
        return fragment;
    }

    public SendFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.send_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            ImageButton backArrowImageButton = (ImageButton) getView().findViewById(R.id.imageButton_send_back_arrow);

            TextView balanceTextView = (TextView) getView().findViewById(R.id.textView_send_balance);
            EditText toAddressEditText = (EditText) getView().findViewById(R.id.editText_send_to_address);
            EditText quantityEditText = (EditText) getView().findViewById(R.id.editText_send_quantity);
            Button sendButton = (Button) getView().findViewById(R.id.button_send_send);

            ProgressBar progressBar = (ProgressBar)  getView().findViewById(R.id.progress_send_loader);
            ProgressBar progressBarSendCoins = (ProgressBar)  getView().findViewById(R.id.progress_loader_send_coins);

            TextView address1TextView = (TextView) getView().findViewById(R.id.textView_send_sample_address1);
            TextView address2TextView = (TextView) getView().findViewById(R.id.textView_send_sample_address2);

            linerLayoutOffline = (LinearLayout) getView().findViewById(R.id.linerLayout_send_offline);
            imageViewRetry = (ImageView) getView().findViewById(R.id.image_retry);
            textViewTitleRetry = (TextView) getView().findViewById(R.id.textview_title_retry);
            textViewSubTitleRetry = (TextView) getView().findViewById(R.id.textview_subtitle_retry);

            Button buttonRetry = (Button) getView().findViewById(R.id.button_retry);

            String walletAddress = getArguments().getString("walletAddress");
            String password = getArguments().getString("password");

            getBalanceByAccount(walletAddress, balanceTextView, progressBar);

            backArrowImageButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mSendListener.onSendComplete(password);
                }
            });

            sendButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        String message = getResources().getString(R.string.send_address_message_description);
                        if (toAddressEditText.getText().toString().startsWith(GlobalMethods.ADDRESS_START_PREFIX)) {
                            if (toAddressEditText.getText().toString().length() == GlobalMethods.ADDRESS_LENGTH) {
                                if (quantityEditText.getText().toString().length() > 0) {
                                    String toAddress = toAddressEditText.getText().toString();
                                    String quantity = quantityEditText.getText().toString();
                                    String dp_wei = (String) keyViewModel.getDogeProtocolToWei(quantity);
                                    if (password == null || password.isEmpty()) {
                                        if (progressBarSendCoins.getVisibility() == View.VISIBLE) {
                                            message = getResources().getString(R.string.send_transaction_message_exits);
                                            GlobalMethods.ShowToast(getContext(), message);
                                        } else {
                                            unlockDialogFragment(view, progressBarSendCoins,
                                                    walletAddress, toAddress, dp_wei);
                                        }
                                    } else {
                                        sendTransaction(getContext(), progressBarSendCoins,
                                                walletAddress, toAddress, dp_wei, password);
                                    }
                                    return;
                                }
                                message = getResources().getString(R.string.send_quantity_message_description);
                            }
                        }
                        messageDialogFragment(message);
                    }catch (Exception e){
                        GlobalMethods.ExceptionError(getContext(), TAG, e);
                    }
                }
            });

            buttonRetry.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    switch (retryStatus) {
                        case 0:
                            getBalanceByAccount(walletAddress, balanceTextView, progressBar);
                            break;
                        case 1:
                            String  message = getResources().getString(R.string.send_transaction_message_description);
                            messageDialogFragment(message);
                            break;
                    }
                }
            });

            address1TextView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    toAddressEditText.setText(address1TextView.getText());
                }
            });

            address2TextView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    toAddressEditText.setText(address2TextView.getText());
                }
            });

        } catch (Exception e) {
            GlobalMethods.ExceptionError(getContext(), TAG, e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    public static interface OnSendCompleteListener {
        public abstract void onSendComplete(String password);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            this.mSendListener = (SendFragment.OnSendCompleteListener)context;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " ");
        }
    }

    private void messageDialogFragment(String message) {
        try {
            Bundle bundleRoute = new Bundle();
            bundleRoute.putString("message", message);
            FragmentManager fragmentManager  = getFragmentManager();
            MessageInformationDialogFragment messageDialogFragment = MessageInformationDialogFragment.newInstance();
            messageDialogFragment.setCancelable(false);
            messageDialogFragment.setArguments(bundleRoute);
            messageDialogFragment.show(fragmentManager, "");
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getContext(), TAG, e);
        }
    }

    private void unlockDialogFragment(View view, ProgressBar progressBarSendCoins,
                                     String walletAddress, String toAddress, String dp_wei) {
        try {
            //Alert unlock dialog
            AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setTitle((CharSequence) "").setView((int)
                            R.layout.unlock_dialog_fragment).create();
            dialog.setCancelable(false);
            dialog.show();

            EditText passwordEditText = (EditText) dialog.findViewById(R.id.editText_unlock_password);
            Button unlockButton = (Button) dialog.findViewById(R.id.button_unlock_unlock);
            Button closeButton = (Button) dialog.findViewById(R.id.button_unlock_close);

            unlockButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //Check Password
                    String password = passwordEditText.getText().toString();
                    if(password.trim().length() < GlobalMethods.MINIMUM_PASSWORD_LENGTH) {
                        messageDialogFragment(getResources().getString(
                                R.string.unlock_password_empty_message));
                        return;
                    }
                        dialog.dismiss();
                        sendTransaction(getContext(), progressBarSendCoins,
                                walletAddress, toAddress, dp_wei, password);
                }
            });

            closeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            GlobalMethods.ExceptionError(getContext(), TAG, e);
        }
    }

    private void getBalanceByAccount(String address, TextView balanceTextView, ProgressBar progressBar) {
        try{
            retryStatus = 0;

            linerLayoutOffline.setVisibility(View.GONE);

            //Internet connection check
            if (GlobalMethods.IsNetworkAvailable(getContext())) {

                progressBar.setVisibility(View.VISIBLE);

                String[] taskParams = { address };

                KeyViewModel keyViewModel = new KeyViewModel();

                AccountBalanceRestTask task = new AccountBalanceRestTask(
                    getContext(), new AccountBalanceRestTask.TaskListener() {
                    @Override
                    public void onFinished(BalanceResponse balanceResponse) throws ServiceException {
                        if (balanceResponse.getResult().getBalance() != null) {
                            String value = balanceResponse.getResult().getBalance().toString();
                            String quantity = (String) keyViewModel.getWeiToDogeProtocol(value);
                            balanceTextView.setText(quantity);
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                    @Override
                    public void onFailure(com.dpwallet.app.api.read.ApiException e) {
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

    private void sendTransaction(Context context, ProgressBar progressBar,
                                 String fromAddress, String toAddress, String dp_wei, String password) {
        try {
            if (progressBar.getVisibility() == View.VISIBLE) {
                String message = getResources().getString(R.string.send_transaction_message_exits);
                GlobalMethods.ShowToast(getContext(), message);
            } else {
                progressBar.setVisibility(View.VISIBLE);
                int[] SK_KEY = keyViewModel.decryptDataByAccount(context, fromAddress, password);
                getBalanceNonceByAccount(context, progressBar, fromAddress, toAddress, dp_wei, SK_KEY, password);
            }
        } catch (KeyServiceException e){
            progressBar.setVisibility(View.GONE);
            GlobalMethods.ExceptionError(getContext(), TAG, e);
        }catch (InvalidKeyException e){
            progressBar.setVisibility(View.GONE);
            GlobalMethods.ExceptionError(getContext(), TAG, e);
        }
    }

    private  void getBalanceNonceByAccount(Context context, ProgressBar progressBar, String fromAddress, String toAddress,
                                           String dp_wei, int[] SK_KEY, String password) {
        try{
            linerLayoutOffline.setVisibility(View.GONE);

            //Internet connection check
            if (GlobalMethods.IsNetworkAvailable(getContext())) {

                String[] taskParams = { fromAddress };

                AccountBalanceRestTask task = new AccountBalanceRestTask(
                        context, new AccountBalanceRestTask.TaskListener() {
                    @Override
                    public void onFinished(BalanceResponse balanceResponse) {
                        getTxData(context, progressBar, balanceResponse, fromAddress, toAddress, dp_wei, SK_KEY, password);
                    }
                    @Override
                    public void onFailure(com.dpwallet.app.api.read.ApiException e) {
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
                progressBar.setVisibility(View.GONE);
                GlobalMethods.OfflineOrExceptionError(getContext(),
                        linerLayoutOffline, imageViewRetry, textViewTitleRetry,
                        textViewSubTitleRetry, false);
            }
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            GlobalMethods.ExceptionError(getContext(), TAG, e);
        }
    }

    private  void getTxData(Context context, ProgressBar progressBar, BalanceResponse balanceResponse,
                            String fromAddress, String toAddress, String dp_wei,
                            int[] SK_KEY, String password){

        try {
            String NONCE = "0";

            //Nonce
            if(balanceResponse.getResult().getNonce() != null){
                String nonce = (String)balanceResponse.getResult().getNonce();
                if(!Strings.isEmptyOrWhitespace(nonce)){
                    NONCE = nonce;
                }
            }

            int[] PK_KEY = (int[]) keyViewModel.publicKeyFromPrivateKey(SK_KEY);
            int[] message  = (int[]) keyViewModel.getTxMessage(fromAddress,  NONCE,
                    toAddress, dp_wei, GlobalMethods.GAS, GlobalMethods.GAS_PRICE,  GlobalMethods.CHAIN_ID);
            int[] SIGN = (int[]) keyViewModel.signAccount(message, SK_KEY);
            int verify = (int) keyViewModel.verifyAccount(message, SIGN, PK_KEY);

            if(verify==0){
                //String txHash = (String) keyViewModel.getTxHash(fromAddress,  NONCE, toAddress, dp_wei,
                //        GlobalMethods.GAS, GlobalMethods.GAS_PRICE,  GlobalMethods.CHAIN_ID, PK_KEY, SIGN);
                String txData = (String) keyViewModel.getTxData(fromAddress,  NONCE, toAddress, dp_wei,
                        GlobalMethods.GAS, GlobalMethods.GAS_PRICE,  GlobalMethods.CHAIN_ID, PK_KEY, SIGN);

                transactionByAccount(context, progressBar, txData, password);
            } else {
                Bundle bundleRoute = new Bundle();
                String information = getResources().getString(R.string.unlock_message_description);
                bundleRoute.putString("message", information);
                FragmentManager fragmentManager  = getFragmentManager();
                MessageInformationDialogFragment messageDialogFragment = MessageInformationDialogFragment.newInstance();
                messageDialogFragment.setCancelable(false);
                messageDialogFragment.setArguments(bundleRoute);
                messageDialogFragment.show(fragmentManager, "");
            }

        } catch (ServiceException e) {
            progressBar.setVisibility(View.GONE);
            GlobalMethods.ExceptionError(getContext(), TAG, e);
        }
    }

    private  void transactionByAccount(Context context, ProgressBar progressBar, String txData, String password) {
        try{

            retryStatus = 1;

            linerLayoutOffline.setVisibility(View.GONE);

            //Internet connection check
            if (GlobalMethods.IsNetworkAvailable(getContext())) {

                String[] taskParams = { txData };

                TransactionRestTask task = new TransactionRestTask(
                        context, new TransactionRestTask.TaskListener() {
                    @Override
                    public void onFinished(TransactionSummaryResponse transactionSummaryResponse) {
                        mSendListener.onSendComplete(password);
                    }
                    @Override
                    public void onFailure(com.dpwallet.app.api.write.ApiException e) {
                        progressBar.setVisibility(View.GONE);
                        int code = e.getCode();
                        boolean check = GlobalMethods.ApiExceptionSourceCodeBoolean(code);
                        if(check == true) {
                            GlobalMethods.ApiExceptionSourceCodeRoute(getContext(), code,
                                    getString(R.string.apierror),
                                    TAG + " : TransactionRestTask : " + e.toString());
                        } else {
                            GlobalMethods.OfflineOrExceptionError(getContext(),
                                    linerLayoutOffline, imageViewRetry, textViewTitleRetry,
                                    textViewSubTitleRetry, true);
                        }
                    }
                });
                task.execute(taskParams);
            } else {
                progressBar.setVisibility(View.GONE);
                GlobalMethods.OfflineOrExceptionError(getContext(),
                        linerLayoutOffline, imageViewRetry, textViewTitleRetry,
                        textViewSubTitleRetry, false);
            }
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            GlobalMethods.ExceptionError(getContext(), TAG, e);
        }
    }

}
