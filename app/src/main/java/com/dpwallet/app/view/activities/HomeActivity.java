package com.dpwallet.app.view.activities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.ClipboardManager;

import com.dpwallet.app.R;
import com.dpwallet.app.api.read.model.BalanceResponse;
import com.dpwallet.app.asynctask.download.DownloadingTask;
import com.dpwallet.app.asynctask.read.AccountBalanceRestTask;
import com.dpwallet.app.entity.ServiceException;
import com.dpwallet.app.utils.CheckForSDCard;
import com.dpwallet.app.utils.GlobalMethods;
import com.dpwallet.app.utils.PrefConnect;
import com.dpwallet.app.utils.Utility;
import com.dpwallet.app.view.fragment.ExportWalletFragment;
import com.dpwallet.app.view.fragment.HomeFragment;
import com.dpwallet.app.view.fragment.HomeImportWalletFragment;
import com.dpwallet.app.view.fragment.HomeNewFragment;
import com.dpwallet.app.view.fragment.HomeNewWalletFragment;
import com.dpwallet.app.view.fragment.QrCodeDialogFragment;
import com.dpwallet.app.view.fragment.QrCodeFragment;
import com.dpwallet.app.view.fragment.ReceiveFragment;
import com.dpwallet.app.view.fragment.SendFragment;
import com.dpwallet.app.view.fragment.SettingsFragment;
import com.dpwallet.app.view.fragment.AccountTransactionsFragment;

import com.dpwallet.app.view.fragment.TestnetCoinsFragment;

import com.dpwallet.app.viewmodel.KeyViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeActivity extends FragmentActivity implements
        HomeFragment.OnHomeCompleteListener, HomeNewFragment.OnHomeNewCompleteListener, HomeNewWalletFragment.OnHomeNewWalletCompleteListener,
        HomeImportWalletFragment.OnHomeImportWalletCompleteListener,
        QrCodeDialogFragment.OnQrCodeDialogCompleteListener,
        SendFragment.OnSendCompleteListener, ReceiveFragment.OnReceiveCompleteListener,
        AccountTransactionsFragment.OnAccountTransactionsCompleteListener, ExportWalletFragment.OnExportWalletCompleteListener,
        TestnetCoinsFragment.OnTestnetCoinsCompleteListener, SettingsFragment.OnSettingsCompleteListener {

    private static final String TAG = "HomeActivity";
    private final int notificationRequestCode = 112;

    private LinearLayout topLinearLayout;
    private ViewGroup.LayoutParams topLinearLayoutParams;
    private RelativeLayout centerRelativeLayout;

    private Bundle bundle;

    private String walletAddress = "";

    private TextView walletAddressTextView;
    private TextView balanceTextView;
    private ProgressBar progressBar;
    private BottomNavigationView bottomNavigationView;

    private LinearLayout linerLayoutOffline;
    private ImageView imageViewRetry;
    private TextView textViewTitleRetry;
    private TextView textViewSubTitleRetry;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            //locale language
            String languageKey = getIntent().getStringExtra("languagekey");

            //Bundle
            bundle  = new Bundle();
            bundle.putString("languageKey", languageKey);

            GlobalMethods.LocaleLanguage(getBaseContext(),TAG, languageKey);

            setContentView(R.layout.home_activity);

            //Linear top layout
            topLinearLayout = (LinearLayout) findViewById(R.id.top_linear_layout_home_id);
            topLinearLayoutParams = topLinearLayout.getLayoutParams();

            //Center Relative layout & Image Button
            centerRelativeLayout = (RelativeLayout) findViewById(R.id.center_relative_layout_home_id);
            walletAddressTextView = (TextView) findViewById(R.id.textView_home_wallet_address);
            ImageButton copyClipboardImageButton = (ImageButton) findViewById(R.id.imageButton_home_copy_clipboard);
            ImageButton qrCodeImageButton = (ImageButton) findViewById(R.id.imageButton_home_qr_code);

            balanceTextView = (TextView) findViewById(R.id.textView_home_balance_value);
            ImageButton refreshImageButton = (ImageButton) findViewById(R.id.imageButton_home_refresh);
            progressBar = (ProgressBar) findViewById(R.id.progress_home_loader);

            ImageButton sendImageButton = (ImageButton) findViewById(R.id.imageButton_home_send);
            ImageButton receiveImageButton = (ImageButton) findViewById(R.id.imageButton_home_receive);
            ImageButton transactionsImageButton = (ImageButton) findViewById(R.id.imageButton_home_transactions);
            ImageButton dpScanImageButton = (ImageButton) findViewById(R.id.imageButton_home_dpscan);

            //Bottom navigation
            bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

            linerLayoutOffline = (LinearLayout) findViewById(R.id.linerLayout_home_offline);
            imageViewRetry = (ImageView) findViewById(R.id.image_retry);
            textViewTitleRetry = (TextView) findViewById(R.id.textview_title_retry);
            textViewSubTitleRetry = (TextView) findViewById(R.id.textview_subtitle_retry);
            Button buttonRetry = (Button) findViewById(R.id.button_retry);

            setWalletAddress();

            //Notification permission
            if (Build.VERSION.SDK_INT >= 33) {
                if (ContextCompat.checkSelfPermission(this,  android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS},notificationRequestCode);
                }
                createNotificationChannel();
            }

            //Center buttons setOnClickListener
            copyClipboardImageButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClipboardManager clipBoard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("label", walletAddressTextView.getText());
                    clipBoard.setPrimaryClip(clipData);
                }
            });

            qrCodeImageButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    screenViewType(0);
                    beginTransaction(QrCodeFragment.newInstance(), bundle);
                }
            });

            refreshImageButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    getBalanceByAccount(walletAddress, balanceTextView, progressBar);
                }
            });

            sendImageButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    screenViewType(1);
                    beginTransaction(SendFragment.newInstance(), bundle);
                }
            });

            receiveImageButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    screenViewType(1);
                    beginTransaction(ReceiveFragment.newInstance(), bundle);
                }
            });

            transactionsImageButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    screenViewType(1);
                    beginTransaction(AccountTransactionsFragment.newInstance(), bundle);
                }
            });

            dpScanImageButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                   //Open url with address
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(GlobalMethods.DP_SCAN_ACCOUNT_TRANSACTION_URL.replace(
                                    "{address}", walletAddressTextView.getText())))
                    );
                }
            });

            buttonRetry.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });

            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            if (walletAddress.length()>10) {
                                screenViewType(0);
                                beginTransaction(HomeFragment.newInstance(), bundle);
                            } else {
                                bundle.putString("screenStart", "1");
                                screenViewType(1);
                                beginTransaction(HomeNewFragment.newInstance(), bundle);
                            }
                            return true;
                        case R.id.nav_help:
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(GlobalMethods.DP_DOCS_URL))
                            );
                            return true;
                        case R.id.nav_big_scan:
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(GlobalMethods.DP_SCAN_URL))
                            );
                            return true;
                        case R.id.nav_settings:
                            screenViewType(1);
                            beginTransaction(SettingsFragment.newInstance(), bundle);
                            return true;
                    }
                    return false;
                }
            });

           // if (savedInstanceState == null) {
                if (walletAddress.length()>10) {
                    screenViewType(0);
                    beginTransaction(HomeFragment.newInstance(), bundle);
                    notificationThread(1);
                } else {
                    bundle.putString("screenStart", "1");
                    screenViewType(1);
                    beginTransaction(HomeNewFragment.newInstance(), bundle);
                }
          //  }

        } catch (Exception e) {
            GlobalMethods.ExceptionError(getApplicationContext(), TAG, e);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    @Override
    public void onHomeComplete() {
        try{
            MenuItem item = bottomNavigationView.getMenu().findItem(R.id.nav_home);
            item.setChecked(true);
            getBalanceByAccount(walletAddress, balanceTextView, progressBar);
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getApplicationContext(), TAG, e);
        }
    }

    @Override
    public void onHomeNewComplete(int status) {
        try{
            //0 - Create new wallet, 1- Import wallet
            switch(status) {
                case 0 :
                    screenViewType(1);
                    beginTransaction(HomeNewWalletFragment.newInstance(), bundle);
                    break;
                case 1:
                    screenViewType(1);
                    beginTransaction(HomeImportWalletFragment.newInstance(), bundle);
                    break;
            }
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getApplicationContext(), TAG, e);
        }
    }

    @Override
    public void onHomeNewWalletComplete(int status) {
        try{
            //0 - Back button, 1- New wallet
            switch(status) {
                case 0 :
                    bundle.putString("screenStart", "7");
                    screenViewType(1);
                    beginTransaction(HomeNewFragment.newInstance(), bundle);
                    break;
                case 1:
                    screenViewType(1);
                    setWalletAddress();
                    beginTransaction(TestnetCoinsFragment.newInstance(), bundle);
                    break;
            }
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getApplicationContext(), TAG, e);
        }
    }

    @Override
    public void onHomeImportWalletComplete(int status) {
        try{
            //0 - Back button, 1- New wallet
            switch(status) {
                case 0 :
                    bundle.putString("screenStart", "7");
                    screenViewType(1);
                    beginTransaction(HomeNewFragment.newInstance(), bundle);
                    break;
                case 1:
                   // screenViewType(0);
                   // beginTransaction(HomeFragment.newInstance(), bundle);
                    break;
            }
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getApplicationContext(), TAG, e);
        }
    }

    @Override
    public void onTestnetCoinsComplete() {
        try{
            screenViewType(0);
            beginTransaction(HomeFragment.newInstance(), bundle);
            notificationThread(0);
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getApplicationContext(), TAG, e);
        }
    }

    @Override
    public void onExportWalletComplete(int status, ProgressBar progressBar) {
        try{
            //0 - Skip wallet, 1- Export wallet
            switch(status) {
                case 0 :
                    if (walletAddress.length()>10) {
                        screenViewType(0);
                        beginTransaction(HomeFragment.newInstance(), bundle);
                        return;
                    }
                    bundle.putString("screenStart", "7");
                    screenViewType(1);
                    beginTransaction(HomeNewFragment.newInstance(), bundle);
                    break;
                case 1:
                    ExportKey(walletAddress, progressBar);
                    break;
            }
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getApplicationContext(), TAG, e);
        }
    }

    @Override
    public void onQrCodeDialogComplete() {
        try{
            screenViewType(0);
            beginTransaction(HomeFragment.newInstance(), bundle);
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getApplicationContext(), TAG, e);
        }
    }

    @Override
    public void onSendComplete(String password) {
        try{
            bundle.putString("password", password);
            screenViewType(0);
            beginTransaction(HomeFragment.newInstance(), bundle);
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getApplicationContext(), TAG, e);
        }
    }

    @Override
    public void onReceiveComplete() {
        try{
            screenViewType(0);
            beginTransaction(HomeFragment.newInstance(), bundle);
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getApplicationContext(), TAG, e);
        }
    }

    @Override
    public void onAccountTransactionsComplete() {
        try{
            screenViewType(0);
            beginTransaction(HomeFragment.newInstance(), bundle);
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getApplicationContext(), TAG, e);
        }
    }

    @Override
    public void onSettingsComplete(int status) {
        try{
            //0 and 3 - Back arrow, 1 - Export, 2 - Testnet, 3 - delete account
            switch (status) {
                case 1 :
                    screenViewType(1);
                    beginTransaction(ExportWalletFragment.newInstance(), bundle);
                    break;
                case 3:
                    screenViewType(1);
                    removeWalletAddress();

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                    break;
                default:
                    //0, 2 Button
                    screenViewType(0);
                    beginTransaction(HomeFragment.newInstance(), bundle);
                    break;
            }
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getApplicationContext(), TAG, e);
        }
    }

    private void beginTransaction(Fragment fragment, Bundle bundle) {
        try {
            linerLayoutOffline.setVisibility(View.GONE);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            fragment.setArguments(bundle);
            transaction.replace(R.id.frame_home_container_id, fragment);
            transaction.commit();
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getApplicationContext(), TAG, e);
        }
    }

    private void screenViewType(int status){
        try{
            // 0 - default main screen, 1 - fragment screen
            switch(status) {
                case 0:
                    topLinearLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    topLinearLayout.setLayoutParams(topLinearLayoutParams);
                    centerRelativeLayout.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    int screenHeight = (Utility.calculateScreenWidthDp(getApplicationContext()) * 30 / 100);;
                    topLinearLayoutParams.height = screenHeight;
                    topLinearLayout.setLayoutParams(topLinearLayoutParams);
                    centerRelativeLayout.setVisibility(View.GONE);
                    break;
            }

        } catch (Exception e) {
            GlobalMethods.ExceptionError(getApplicationContext(), TAG, e);
        }
    }

    private void setWalletAddress(){
        walletAddress =  (String)  PrefConnect.readString(getApplicationContext(), PrefConnect.walletAddress, "");
        bundle.putString("walletAddress", walletAddress);
        walletAddressTextView.setText(walletAddress);
    }

    private void removeWalletAddress(){
        PrefConnect.writeString(getApplicationContext(), PrefConnect.walletAddress, "");
        walletAddress =  (String)  PrefConnect.readString(getApplicationContext(), PrefConnect.walletAddress, "");
        bundle.putString("walletAddress", walletAddress);
        walletAddressTextView.setText(walletAddress);
    }

    //Open downloaded folder

    //Get balance task
    private void getBalanceByAccount(String address, TextView balanceTextView, ProgressBar progressBar) {
        try{
            linerLayoutOffline.setVisibility(View.GONE);

            //Internet connection check
            if (GlobalMethods.IsNetworkAvailable(getApplicationContext())) {

                progressBar.setVisibility(View.VISIBLE);

                String[] taskParams = { address };

                AccountBalanceRestTask task = new AccountBalanceRestTask(
                        getApplicationContext(), new AccountBalanceRestTask.TaskListener() {
                    @Override
                    public void onFinished(BalanceResponse balanceResponse) throws ServiceException {
                        if (balanceResponse.getResult().getBalance() != null) {
                            String value = balanceResponse.getResult().getBalance().toString();

                            KeyViewModel keyViewModel = new KeyViewModel();
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
                            GlobalMethods.ApiExceptionSourceCodeRoute(getApplicationContext(), code,
                                    getString(R.string.apierror),
                                    TAG + " : AccountBalanceRestTask : " + e.toString());
                        } else {
                            GlobalMethods.OfflineOrExceptionError(getApplicationContext(),
                                    linerLayoutOffline, imageViewRetry, textViewTitleRetry,
                                    textViewSubTitleRetry, true);
                        }
                    }
                });
                task.execute(taskParams);
            } else {
                GlobalMethods.OfflineOrExceptionError(getApplicationContext(),
                        linerLayoutOffline, imageViewRetry, textViewTitleRetry,
                        textViewSubTitleRetry, false);
            }
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getApplicationContext(), TAG, e);
        }
    }

    //Get Export
     private void ExportKey(String address, ProgressBar progressBar){
         KeyViewModel keyViewModel = new KeyViewModel();
         String jsonDocument = keyViewModel.exportKeyByAccount(getApplicationContext(), address);

         String isoStr  = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(new Date());
         isoStr = isoStr.replaceAll(":", "-");
         String a = address.toLowerCase().substring(2, address.length());
         String fileName = "UTC--" + isoStr + "--" + a + ".wallet";

         String downloadUrl = GlobalMethods.mainUrl + fileName;
         downloadWallet(downloadUrl, jsonDocument, progressBar);

         //Intent sendIntent = new Intent();
         //sendIntent.setAction(Intent.ACTION_SEND);
         //sendIntent.putExtra(Intent.EXTRA_TEXT, jsonDocument);
         //sendIntent.setType("text/json");
         //Intent shareIntent = Intent.createChooser(sendIntent, null);
         //startActivity(shareIntent);

         /*
         ArrayList<String> myList = new ArrayList<>();
         ListView listview;
         listview = findViewById(R.id.list);
         String state = Environment.getExternalStorageState();
         if (Environment.MEDIA_MOUNTED.equals(state)) {
             if (Build.VERSION.SDK_INT >= 23) {
                 if (checkPermission()) {
                     File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
                     if (dir.exists()) {
                         Log.d("path", dir.toString());
                         File list[] = dir.listFiles();
                         for (int i = 0; i < list.length; i++) {
                             myList.add(list[i].getName());
                         }
                         ArrayAdapter arrayAdapter = new ArrayAdapter(HomeActivity.this, android.R.layout.simple_list_item_1, myList);
                         //listview.setAdapter(arrayAdapter);
                     }
                 } else {
                     requestPermission(); // Code for permission
                 }
             } else {
                 File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/");
                 if (dir.exists()) {
                     Log.d("path", dir.toString());
                     File list[] = dir.listFiles();
                     for (int i = 0; i < list.length; i++) {
                         myList.add(list[i].getName());
                     }
                     ArrayAdapter arrayAdapter = new ArrayAdapter(HomeActivity.this, android.R.layout.simple_list_item_1, myList);
                     //listview.setAdapter(arrayAdapter);
                 }
             }
         }

          */
     }

    // download task
    private void downloadWallet(String downloadUrl, String jsonDocument, ProgressBar progressBar) {
        try{
            linerLayoutOffline.setVisibility(View.GONE);

            //Internet connection check
            if (GlobalMethods.IsNetworkAvailable(getApplicationContext())) {

                progressBar.setVisibility(View.VISIBLE);

                String[] taskParams = { downloadUrl, jsonDocument };

                DownloadingTask task = new DownloadingTask(
                        getApplicationContext(), new DownloadingTask.TaskListener() {
                    @Override
                    public void onFinished(boolean status) {
                        openDownloadedFolder();
                        progressBar.setVisibility(View.GONE);
                        GlobalMethods.ShowToast(getApplicationContext(), "File successfully download on download folder");
                    }
                    @Override
                    public void onFailure(Exception e) {
                        progressBar.setVisibility(View.GONE);
                        GlobalMethods.ExceptionError(getApplicationContext(),
                                    TAG + "DownloadingTask : " ,  e);
                    }
                });
                task.execute(taskParams);
            } else {
                GlobalMethods.OfflineOrExceptionError(getApplicationContext(),
                        linerLayoutOffline, imageViewRetry, textViewTitleRetry,
                        textViewSubTitleRetry, false);
            }

        } catch (Exception e) {
            GlobalMethods.ExceptionError(getApplicationContext(), TAG, e);
        }
    }

    private void openDownloadedFolder() {
        if (new CheckForSDCard().isSDCardPresent()) {
            File apkStorage = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"
                            + GlobalMethods.downloadDirectory);
            if (!apkStorage.exists())
                GlobalMethods.ShowToast(getApplicationContext(), "Right now there is no directory. Please download some file first.");
            else {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                Uri uri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()
                        + "/" + GlobalMethods.downloadDirectory + "/");
                intent.setDataAndType(uri, "text/*");
                startActivity(Intent.createChooser(intent, "Open Download Folder"));

                screenViewType(0);
                beginTransaction(HomeFragment.newInstance(), bundle);
            }
        } else {
            GlobalMethods.ShowToast(getApplicationContext(), "Oops!! There is no SD Card.");
        }
    }


    //Notification
    private void notificationThread(int accountStatus) {
        try {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    //boolean threadStop = false;
                    final int TIME_SLEEP = 5000;
                    final String[] previewsQuantity = new String[1];
                    String[] currentQuantity = new String[1];

                    //New account
                    if(accountStatus == 0){
                        previewsQuantity[0] = "0";
                    }

                    try {
                        while (true) {

                            if (walletAddress.length()<10){
                                break;
                            }

                            String[] taskParams = { walletAddress };

                            AccountBalanceRestTask task = new AccountBalanceRestTask(
                                    getApplicationContext(), new AccountBalanceRestTask.TaskListener() {
                                @Override
                                public void onFinished(BalanceResponse balanceResponse) throws ServiceException {
                                    if (balanceResponse.getResult().getBalance() != null) {
                                        String value = balanceResponse.getResult().getBalance().toString();
                                        KeyViewModel keyViewModel = new KeyViewModel();
                                        currentQuantity[0] = (String) keyViewModel.getWeiToDogeProtocol(value);
                                        if(previewsQuantity[0] != null) {
                                            if(!previewsQuantity[0].equals(currentQuantity[0])) {
                                                balanceTextView.setText(currentQuantity[0]);
                                                sendNotificationChannel(getApplicationContext().getString(R.string.notification_description) + " " + currentQuantity[0]);
                                            }
                                        }
                                        previewsQuantity[0] = currentQuantity[0];
                                    }
                                }
                                @Override
                                public void onFailure(com.dpwallet.app.api.read.ApiException e) {

                                }
                            });

                            task.execute(taskParams);

                            Thread.sleep(TIME_SLEEP);
                        }
                    } catch (Exception e) {
                        GlobalMethods.ExceptionError(getBaseContext(), TAG, e);
                    }
                }
            };
            thread.start();
        }catch (Exception e) {
            GlobalMethods.ExceptionError(getBaseContext(), TAG, e);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notification_channel_name);
            String description = getString(R.string.notification_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(
                    getString(R.string.notification_channel_id), name, importance
            );
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotificationChannel(String content) {
        String title = getResources().getString(R.string.notification_title);
        String channelId = getResources().getString(R.string.notification_channel_id);
        int priority = NotificationCompat.PRIORITY_DEFAULT;
        int notificationID = notificationRequestCode;

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationRequestCode,
                intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getApplicationContext(), channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo))
                        .setContentIntent(pendingIntent)
                        .setPriority(priority)
                        .setContentTitle(title)
                        .setContentText(content)
                        .setAutoCancel(true)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        // Since android Oreo notification channel is needed.
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationID, notificationBuilder.build());
    }

/*
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(HomeActivity.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(HomeActivity.this,  android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(HomeActivity.this, "Write External Storage permission allows us to read  files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]
                    {android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else{
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
            break;
        }
    }
*/

}