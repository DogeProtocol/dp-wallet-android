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
import com.dpwallet.app.viewmodel.JsonViewModel;
import com.dpwallet.app.viewmodel.KeyViewModel;
import com.google.android.gms.common.util.Strings;

import java.security.InvalidKeyException;

public class SendFragment extends Fragment  {
    private static final String TAG = "SendFragment";

    private int retryStatus = 0;
    private int sendButtonStatus = 0;

    private LinearLayout linerLayoutOffline;
    private ImageView imageViewRetry;
    private TextView textViewTitleRetry;
    private TextView textViewSubTitleRetry;

    private SendFragment.OnSendCompleteListener mSendListener;
    private  KeyViewModel keyViewModel = new KeyViewModel();
    private JsonViewModel jsonViewModel;

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
            jsonViewModel = new JsonViewModel(getContext(), getArguments().getString("languageKey"));
            String walletAddress = getArguments().getString("walletAddress");
            String sendPassword = getArguments().getString("sendPassword");

            ImageButton backArrowImageButton = (ImageButton) getView().findViewById(R.id.imageButton_send_back_arrow);

            TextView sendTextView = (TextView) getView().findViewById(R.id.textView_send_langValue_send);
            sendTextView.setText(jsonViewModel.getSendByLangValues());

            TextView balanceTextView = (TextView) getView().findViewById(R.id.textView_send_langValue_balance);
            balanceTextView.setText(jsonViewModel.getBalanceByLangValues());

            TextView balanceValueTextView = (TextView) getView().findViewById(R.id.textView_send_balance_value);
            ProgressBar progressBar = (ProgressBar)  getView().findViewById(R.id.progress_send_loader);

            TextView addressToSendTextView = (TextView) getView().findViewById(R.id.textView_send_address_to_send);
            addressToSendTextView.setText(jsonViewModel.getAddressToSendByLangValues());

            EditText addressToSendEditText = (EditText) getView().findViewById(R.id.editText_send_address_to_send);
            addressToSendEditText.setHint(jsonViewModel.getAddressToSendByLangValues());

            TextView quantityToSendTextView = (TextView) getView().findViewById(R.id.textView_send_quantity_to_send);
            quantityToSendTextView.setText(jsonViewModel.getQuantityToSendByLangValues());

            EditText quantityToSendEditText = (EditText) getView().findViewById(R.id.editText_send_quantity_to_send);
            quantityToSendEditText.setHint(jsonViewModel.getQuantityToSendByLangValues());

            Button sendButton = (Button) getView().findViewById(R.id.button_send_send);
            sendButton.setText(jsonViewModel.getSendByLangValues());

            ProgressBar progressBarSendCoins = (ProgressBar)  getView().findViewById(R.id.progress_loader_send_coins);

            linerLayoutOffline = (LinearLayout) getView().findViewById(R.id.linerLayout_send_offline);
            imageViewRetry = (ImageView) getView().findViewById(R.id.image_retry);
            textViewTitleRetry = (TextView) getView().findViewById(R.id.textview_title_retry);
            textViewSubTitleRetry = (TextView) getView().findViewById(R.id.textview_subtitle_retry);

            Button buttonRetry = (Button) getView().findViewById(R.id.button_retry);

            getBalanceByAccount(walletAddress, balanceValueTextView, progressBar);

            backArrowImageButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mSendListener.onSendComplete(sendPassword);
                }
            });

            sendButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        if(sendButtonStatus == 1){
                            return;
                        }
                        sendButtonStatus = 1;
                        String message = jsonViewModel.getQuantumAddr();
                        if (addressToSendEditText.getText().toString().startsWith(GlobalMethods.ADDRESS_START_PREFIX)) {
                            if (addressToSendEditText.getText().toString().length() == GlobalMethods.ADDRESS_LENGTH) {
                                if (quantityToSendEditText.getText().toString().length() > 0) {
                                    String toAddress = addressToSendEditText.getText().toString();
                                    String quantity = quantityToSendEditText.getText().toString();
                                    String dp_wei = (String) keyViewModel.getDogeProtocolToWei(quantity);
                                    if (sendPassword == null || sendPassword.isEmpty()) {
                                        if (progressBarSendCoins.getVisibility() == View.VISIBLE) {
                                            message = getResources().getString(R.string.send_transaction_message_exits);
                                            GlobalMethods.ShowToast(getContext(), message);
                                        } else {
                                            unlockDialogFragment(view, progressBarSendCoins,
                                                    walletAddress, toAddress, dp_wei);
                                        }
                                    } else {
                                        sendTransaction(getContext(), progressBarSendCoins,
                                                walletAddress, toAddress, dp_wei, sendPassword);
                                    }
                                    return;
                                }
                                message = jsonViewModel.getEnterAmount();
                            }
                        }
                        messageDialogFragment(message);
                        sendButtonStatus = 0;
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
        public abstract void onSendComplete(String sendPassword);
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
            dialog.dismiss();
            dialog.setCancelable(false);
            dialog.show();

            TextView unlockWalletTextView = (TextView) dialog.findViewById(R.id.textView_unlock_langValues_unlock_wallet);
            unlockWalletTextView.setText(jsonViewModel.getUnlockWalletByLangValues());

            TextView unlockPasswordTextView = (TextView) dialog.findViewById(R.id.textView_unlock_langValues_enter_wallet_password);
            unlockPasswordTextView.setText(jsonViewModel.getEnterQuantumWalletPasswordByLangValues());

            EditText passwordEditText = (EditText) dialog.findViewById(R.id.editText_unlock_langValues_enter_a_password);
            passwordEditText.setHint(jsonViewModel.getEnterApasswordByLangValues());

            Button unlockButton = (Button) dialog.findViewById(R.id.button_unlock_langValues_unlock);
            unlockButton.setText(jsonViewModel.getUnlockByLangValues());

            Button closeButton = (Button) dialog.findViewById(R.id.button_unlock_langValues_close);
            closeButton.setText(jsonViewModel.getCloseByLangValues());

            unlockButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //Check password
                    String password = passwordEditText.getText().toString();
                    if (password==null || password.isEmpty()) {
                        messageDialogFragment(jsonViewModel.getEnterApasswordByLangValues());
                        return;
                    }
                    if(sendButtonStatus == 1){
                        dialog.dismiss();
                        sendTransaction(getContext(), progressBarSendCoins,
                                walletAddress, toAddress, dp_wei, password.trim());
                    }
                    sendButtonStatus = 2;
                }
            });

            closeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                    sendButtonStatus = 0;
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
                        if(check==true) {
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
            return;
        } catch (KeyServiceException e){

        }catch (InvalidKeyException e){

        }
        progressBar.setVisibility(View.GONE);
        sendButtonStatus = 0;
        messageDialogFragment(jsonViewModel.getWalletPasswordMismatchByErrors());
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
                        getTxData(context, progressBar, balanceResponse, fromAddress, toAddress,
                                dp_wei, SK_KEY, password);
                    }
                    @Override
                    public void onFailure(com.dpwallet.app.api.read.ApiException e) {
                        progressBar.setVisibility(View.GONE);
                        sendButtonStatus = 0;

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
                sendButtonStatus = 0;

                GlobalMethods.OfflineOrExceptionError(getContext(),
                        linerLayoutOffline, imageViewRetry, textViewTitleRetry,
                        textViewSubTitleRetry, false);
            }
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            sendButtonStatus = 0;

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
            int[] message  = (int[]) keyViewModel.getTxnSigningHash(fromAddress,  NONCE,
                    toAddress, dp_wei, GlobalMethods.GAS_LIMIT,  GlobalMethods.CHAIN_ID);
            int[] SIGN = GlobalMethods.GetIntDataArrayByString(keyViewModel.signAccount(message, SK_KEY));
            int verify = (int) keyViewModel.verifyAccount(message, SIGN, PK_KEY);

            if(verify==0){
                //String txHash = (String) keyViewModel.getTxHash(fromAddress,  NONCE, toAddress, dp_wei,
                //        GlobalMethods.GAS, GlobalMethods.GAS_PRICE,  GlobalMethods.CHAIN_ID, PK_KEY, SIGN);

                String txData = (String) keyViewModel.getTxData(fromAddress,  NONCE, toAddress, dp_wei,
                        GlobalMethods.GAS_LIMIT,  GlobalMethods.CHAIN_ID, PK_KEY, SIGN);

                transactionByAccount(context, progressBar, txData, password);
            } else {
                messageDialogFragment(getResources().getString(R.string.unlock_message_description));
                progressBar.setVisibility(View.GONE);
                sendButtonStatus = 0;
            }
        } catch (ServiceException e) {
            progressBar.setVisibility(View.GONE);
            sendButtonStatus = 0;
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
                        sendCompletedDialogFragment(context,password);
                    }
                    @Override
                    public void onFailure(com.dpwallet.app.api.write.ApiException e) {
                        progressBar.setVisibility(View.GONE);
                        sendButtonStatus = 0;

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
                sendButtonStatus = 0;

                GlobalMethods.OfflineOrExceptionError(getContext(),
                        linerLayoutOffline, imageViewRetry, textViewTitleRetry,
                        textViewSubTitleRetry, false);
            }
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            sendButtonStatus = 0;
            GlobalMethods.ExceptionError(getContext(), TAG, e);
        }
    }

    private void sendCompletedDialogFragment(Context context, String password) {
        try {
            AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setTitle((CharSequence) "").setView((int)
                            R.layout.send_completed_dialog_fragment).create();
            dialog.setCancelable(false);
            dialog.show();
            TextView textViewOk = (TextView) dialog.findViewById(
                    R.id.textView_send_completed_alert_dialog_ok);
            textViewOk.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    sendButtonStatus = 0;
                    dialog.dismiss();
                    mSendListener.onSendComplete(password);
                }
            });
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getContext(), TAG, e);
        }
    }
}
