package com.dpwallet.app.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

import java.io.IOException;
import java.security.InvalidKeyException;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.pm.PackageManager;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class SendFragment extends Fragment  {
    private static final String TAG = "SendFragment";

    private int retryStatus = 0;
    private int sendButtonStatus = 0;

    private LinearLayout linerLayoutOffline;
    private ImageView imageViewRetry;
    private TextView textViewTitleRetry;
    private TextView textViewSubTitleRetry;

    private SendFragment.OnSendCompleteListener mSendListener;
    private KeyViewModel keyViewModel = new KeyViewModel();
    private JsonViewModel jsonViewModel;

    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;

    private final int CAMERA_PERMISSION_CODE = 1;
    private final int CAMERA_REQUEST_CODE = 2;

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
            String languageKey = getArguments().getString("languageKey");
            String walletAddress = getArguments().getString("walletAddress");
            String sendPassword = getArguments().getString("sendPassword");

            jsonViewModel = new JsonViewModel(getContext(), languageKey);

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

            ImageButton qrCodeImageButton = (ImageButton) getView().findViewById(R.id.imageButton_scan_qr_code);

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

            qrCodeImageButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    QRCodeDialogFragment(view, addressToSendEditText);
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
                                                    walletAddress, toAddress, dp_wei, languageKey);
                                        }
                                    } else {
                                        sendTransaction(getContext(), progressBarSendCoins,
                                                walletAddress, toAddress, dp_wei, sendPassword, languageKey);
                                    }
                                    return;
                                }
                                message = jsonViewModel.getEnterAmount();
                            }
                        }
                        messageDialogFragment(languageKey, message);
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
                            messageDialogFragment(languageKey, message);
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

    private void messageDialogFragment(String languageKey, String message) {
        try {
            Bundle bundleRoute = new Bundle();
            bundleRoute.putString("languageKey", languageKey);
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
                                     String walletAddress, String toAddress, String dp_wei, String languageKey) {
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
                        messageDialogFragment(languageKey, jsonViewModel.getEnterApasswordByLangValues());
                        return;
                    }
                    if(sendButtonStatus == 1){
                        dialog.dismiss();
                        sendTransaction(getContext(), progressBarSendCoins,
                                walletAddress, toAddress, dp_wei, password.trim(), languageKey);
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
                                 String fromAddress, String toAddress, String dp_wei, String password, String languageKey) {
        try {
            if (progressBar.getVisibility() == View.VISIBLE) {
                String message = getResources().getString(R.string.send_transaction_message_exits);
                GlobalMethods.ShowToast(getContext(), message);
            } else {
                progressBar.setVisibility(View.VISIBLE);
                String[] keyData = keyViewModel.decryptDataByAccount(context, fromAddress, password);
                int[] SK_KEY =  GlobalMethods.GetIntDataArrayByString(keyData[0]);

                getBalanceNonceByAccount(context, progressBar, fromAddress, toAddress, dp_wei, SK_KEY, password, languageKey);
            }
            return;
        } catch (KeyServiceException e){

        }catch (InvalidKeyException e){

        }
        progressBar.setVisibility(View.GONE);
        sendButtonStatus = 0;
        messageDialogFragment(languageKey, jsonViewModel.getWalletPasswordMismatchByErrors());
    }

    private  void getBalanceNonceByAccount(Context context, ProgressBar progressBar, String fromAddress, String toAddress,
                                           String dp_wei, int[] SK_KEY, String password, String languageKey) {
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
                                dp_wei, SK_KEY, password, languageKey);
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
                            int[] SK_KEY, String password, String languageKey){
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
                    toAddress, dp_wei, GlobalMethods.GAS_QCN_LIMIT,  GlobalMethods.NETWORK_ID);
            int[] SIGN = GlobalMethods.GetIntDataArrayByString(keyViewModel.signAccount(message, SK_KEY));
            int verify = (int) keyViewModel.verifyAccount(message, SIGN, PK_KEY);

            if(verify==0){
                //String txHash = (String) keyViewModel.getTxHash(fromAddress,  NONCE, toAddress, dp_wei,
                //        GlobalMethods.GAS, GlobalMethods.GAS_PRICE,  GlobalMethods.NETWORK_ID, PK_KEY, SIGN);

                String txData = (String) keyViewModel.getTxData(fromAddress,  NONCE, toAddress, dp_wei,
                        GlobalMethods.GAS_QCN_LIMIT,  GlobalMethods.NETWORK_ID, PK_KEY, SIGN);

                transactionByAccount(context, progressBar, txData, password);
            } else {
                messageDialogFragment(languageKey, getResources().getString(R.string.unlock_message_description));
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


    private void QRCodeDialogFragment(View view, EditText walletAddressEditText) {
        try {

            AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setTitle((CharSequence) "").setView((int)
                            R.layout.qrcode_dialog_fragment).create();
            dialog.dismiss();
            dialog.setCancelable(false);
            dialog.show();

            SurfaceView qrCodeSurfaceView = dialog.findViewById(R.id.surfaceView_qrcode);
            TextView qrcodeTextView = dialog.findViewById(R.id.textView_qrcode);

            Button okButton = (Button) dialog.findViewById(R.id.button_qrcode_langValues_ok);
            Button closeButton = (Button) dialog.findViewById(R.id.button_qrcode_langValues_close);

            okButton.setText(jsonViewModel.getOkByLangValues());
            closeButton.setText(jsonViewModel.getCloseByLangValues());

            barcodeDetector = new BarcodeDetector.Builder(getContext())
                    .setBarcodeFormats(Barcode.ALL_FORMATS)
                    .build();

            cameraSource = new CameraSource.Builder(getContext(), barcodeDetector)
                    .setRequestedPreviewSize(1920, 1080)
                    .setAutoFocusEnabled(true)
                    .build();

            qrCodeSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {
                        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            cameraSource.start(qrCodeSurfaceView.getHolder());
                        } else {
                            ActivityCompat.requestPermissions(getActivity(), new
                                    String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                            dialog.dismiss();
                            dialog.setCancelable(false);
                            dialog.show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();
                }
            });


            barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
                @Override
                public void release() {
                    //Toast.makeText(getActivity(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void receiveDetections(@NonNull Detector.Detections<Barcode> detections) {
                    final SparseArray<Barcode> barCode = detections.getDetectedItems();
                    if (barCode.size() > 0) {
                        qrcodeTextView.post(new Runnable() {
                            @Override
                            public void run() {
                                String intentData = barCode.valueAt(0).displayValue;
                                qrcodeTextView.setText(intentData);
                            }
                        });
                    }
                }
            });

            okButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    cameraSource.release();
                    walletAddressEditText.setText(qrcodeTextView.getText());
                    dialog.dismiss();
                }
            });

            closeButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    cameraSource.release();
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            GlobalMethods.ExceptionError(getContext(), TAG, e);
        }
    }

}
