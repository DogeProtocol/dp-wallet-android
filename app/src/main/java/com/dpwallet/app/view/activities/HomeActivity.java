package com.dpwallet.app.view.activities;

import android.Manifest;
import android.annotation.SuppressLint;
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
import com.dpwallet.app.asynctask.read.AccountBalanceRestTask;
import com.dpwallet.app.entity.ServiceException;
import com.dpwallet.app.model.BlockchainNetwork;
import com.dpwallet.app.seedwords.SeedWords;
import com.dpwallet.app.utils.GlobalMethods;
import com.dpwallet.app.utils.PrefConnect;
import com.dpwallet.app.utils.Utility;
import com.dpwallet.app.view.fragment.BlockchainNetworkDialogFragment;
import com.dpwallet.app.view.fragment.BlockchainNetworkFragment;
import com.dpwallet.app.view.fragment.HomeMainFragment;
import com.dpwallet.app.view.fragment.HomeStartFragment;
import com.dpwallet.app.view.fragment.HomeWalletFragment;
import com.dpwallet.app.view.fragment.ReceiveFragment;
import com.dpwallet.app.view.fragment.SendFragment;
import com.dpwallet.app.view.fragment.SettingsFragment;
import com.dpwallet.app.view.fragment.RevealWalletFragment;
import com.dpwallet.app.view.fragment.AccountTransactionsFragment;

import com.dpwallet.app.view.fragment.WalletsFragment;
import com.dpwallet.app.viewmodel.JsonViewModel;
import com.dpwallet.app.viewmodel.KeyViewModel;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HomeActivity extends FragmentActivity implements
        HomeMainFragment.OnHomeMainCompleteListener, HomeStartFragment.OnHomeStartCompleteListener,
        HomeWalletFragment.OnHomeWalletCompleteListener,
        BlockchainNetworkFragment.OnBlockchainNetworkCompleteListener,
        BlockchainNetworkDialogFragment.OnBlockchainNetworkDialogCompleteListener,
        SendFragment.OnSendCompleteListener, ReceiveFragment.OnReceiveCompleteListener,
        AccountTransactionsFragment.OnAccountTransactionsCompleteListener, WalletsFragment.OnWalletsCompleteListener,
        SettingsFragment.OnSettingsCompleteListener, RevealWalletFragment.OnRevealWalletCompleteListener {

    private static final String TAG = "HomeActivity";

    private final int notificationRequestCode = 112;

    private LinearLayout topLinearLayout;
    private ViewGroup.LayoutParams topLinearLayoutParams;
    private RelativeLayout centerRelativeLayout;

    private Bundle bundle;

    private String walletAddress = "";

    private TextView blockChainNetworkTextView;

    private TextView walletAddressTextView;
    private TextView balanceValueTextView;
    private ProgressBar progressBar;
    private BottomNavigationView bottomNavigationView;

    private LinearLayout linerLayoutOffline;
    private ImageView imageViewRetry;
    private TextView textViewTitleRetry;
    private TextView textViewSubTitleRetry;

    private JsonViewModel jsonViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            //locale language

            //String languageKey = getIntent().getStringExtra("languageKey");
            String languageKey="en";

            //Bundle
            bundle = new Bundle();
            bundle.putString("languageKey", languageKey);

            jsonViewModel = new JsonViewModel(getApplicationContext(),languageKey);

            loadSeedsThread();

            PrefConnect.WALLET_ADDRESS_TO_INDEX_MAP = PrefConnect.loadHashMap(getApplicationContext(),
                    PrefConnect.WALLET_KEY_PREFIX + PrefConnect.WALLET_KEY_ADDRESS_INDEX);

            PrefConnect.WALLET_INDEX_TO_ADDRESS_MAP = PrefConnect.loadHashMap(getApplicationContext(),
                    PrefConnect.WALLET_KEY_PREFIX + PrefConnect.WALLET_KEY_INDEX_ADDRESS);

            PrefConnect.WALLET_CURRENT_ADDRESS_INDEX_VALUE = PrefConnect.readString(getApplicationContext(),
                    PrefConnect.WALLET_CURRENT_ADDRESS_INDEX_KEY, "0");

            setContentView(R.layout.home_activity);

            //Linear top layout
            topLinearLayout = (LinearLayout) findViewById(R.id.top_linear_layout_home_id);
            topLinearLayoutParams = topLinearLayout.getLayoutParams();
            blockChainNetworkTextView = (TextView) findViewById(R.id.textView_home_blockchain_network);
            TextView titleTextView = (TextView) findViewById(R.id.textView_home_tile);

            //Center Relative layout & Image Button
            centerRelativeLayout = (RelativeLayout) findViewById(R.id.center_relative_layout_home_id);
            walletAddressTextView = (TextView) findViewById(R.id.textView_home_wallet_address);
            ImageButton copyClipboardImageButton = (ImageButton) findViewById(R.id.imageButton_home_copy_clipboard);
            ImageButton blockExploreImageButton = (ImageButton) findViewById(R.id.imageButton_home_block_explore);

            TextView balanceTitleTextView = (TextView) findViewById(R.id.textView_home_balance_title);
            balanceValueTextView = (TextView) findViewById(R.id.textView_home_balance_value);
            ImageButton refreshImageButton = (ImageButton) findViewById(R.id.imageButton_home_refresh);
            progressBar = (ProgressBar) findViewById(R.id.progress_home_loader);

            TextView sendTitleTextView = (TextView) findViewById(R.id.textView_home_send_title);
            ImageButton sendImageButton = (ImageButton) findViewById(R.id.imageButton_home_send);
            TextView receiveTitleTextView = (TextView) findViewById(R.id.textView_home_receive_title);
            ImageButton receiveImageButton = (ImageButton) findViewById(R.id.imageButton_home_receive);
            TextView transactionsTitleTextView = (TextView) findViewById(R.id.textView_home_transactions_title);
            ImageButton transactionsImageButton = (ImageButton) findViewById(R.id.imageButton_home_transactions);

            //Bottom navigation
            bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

            linerLayoutOffline = (LinearLayout) findViewById(R.id.linerLayout_home_offline);
            imageViewRetry = (ImageView) findViewById(R.id.image_retry);
            textViewTitleRetry = (TextView) findViewById(R.id.textview_title_retry);
            textViewSubTitleRetry = (TextView) findViewById(R.id.textview_subtitle_retry);
            Button buttonRetry = (Button) findViewById(R.id.button_retry);

            titleTextView.setText(jsonViewModel.getTitleByLangValues());
            balanceTitleTextView.setText(jsonViewModel.getBalanceByLangValues());

            sendTitleTextView.setText(jsonViewModel.getSendByLangValues());
            receiveTitleTextView.setText(jsonViewModel.getReceiveByLangValues());
            transactionsTitleTextView.setText(jsonViewModel.getTransactionsByLangValues());

            getCurrentWallet(PrefConnect.WALLET_CURRENT_ADDRESS_INDEX_VALUE);

            //Notification permission
            if (Build.VERSION.SDK_INT >= 33) {
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, notificationRequestCode);
                }
                createNotificationChannel();
            }

            int blockchainNetworkIdIndex = PrefConnect.readInteger(getApplicationContext(),
                    PrefConnect.BLOCKCHAIN_NETWORK_ID_INDEX_KEY, 0);
            List<BlockchainNetwork> blockchainNetworkList = GlobalMethods.BlockChainNetworkRead(getApplicationContext());
            BlockchainNetwork blockchainNetwork = blockchainNetworkList.get(blockchainNetworkIdIndex);
            GlobalMethods.SCAN_API_URL = GlobalMethods.HTTPS + blockchainNetwork.getScanApiDomain();
            GlobalMethods.TXN_API_URL = GlobalMethods.HTTPS + blockchainNetwork.getTxnApiDomain();
            GlobalMethods.BLOCK_EXPLORER_URL = GlobalMethods.HTTPS + blockchainNetwork.getBlockExplorerDomain();
            GlobalMethods.BLOCKCHAIN_NAME = blockchainNetwork.getBlockchainName();
            GlobalMethods.NETWORK_ID = blockchainNetwork.getNetworkId();

            blockChainNetworkTextView.setText(GlobalMethods.BLOCKCHAIN_NAME);

            blockChainNetworkTextView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    bundle.putString("blockchainNetworkIdIndex", String.valueOf(blockchainNetworkIdIndex));

                    BlockchainNetworkDialogFragment blockChainDialogFragment = BlockchainNetworkDialogFragment.newInstance();
                    blockChainDialogFragment.setCancelable(false);
                    blockChainDialogFragment.setArguments(bundle);
                    blockChainDialogFragment.show(getSupportFragmentManager(), "BlockchainNetworkDialog");
                }
            });

            //Center buttons setOnClickListener
            copyClipboardImageButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ClipboardManager clipBoard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("label", walletAddressTextView.getText());
                    clipBoard.setPrimaryClip(clipData);
                }
            });

            blockExploreImageButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(GlobalMethods.BLOCK_EXPLORER_ACCOUNT_TRANSACTION_URL.replace("{address}", walletAddressTextView.getText())))
                    );
                }
            });

            refreshImageButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    getBalanceByAccount(walletAddress, balanceValueTextView, progressBar);
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

            buttonRetry.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });

            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_wallets:
                            if (walletAddress.startsWith(GlobalMethods.ADDRESS_START_PREFIX)) {
                                if (walletAddress.length() == GlobalMethods.ADDRESS_LENGTH){
                                    screenViewType(1);
                                    beginTransaction(WalletsFragment.newInstance(), bundle);
                                }
                            }
                            return true;
                        case R.id.nav_help:
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(GlobalMethods.DP_DOCS_URL))
                            );
                            return true;
                        case R.id.nav_block_explorer:
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(GlobalMethods.BLOCK_EXPLORER_URL))
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

            if (walletAddress.startsWith(GlobalMethods.ADDRESS_START_PREFIX)) {
                if (walletAddress.length() == GlobalMethods.ADDRESS_LENGTH){
                    screenViewType(0);
                    beginTransaction(HomeMainFragment.newInstance(), bundle);
                    notificationThread(1);
                }
            } else {
                screenViewType(-1);
                beginTransaction(HomeStartFragment.newInstance(), bundle);
            }

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
    public void onHomeMainComplete() {
        try {
            getBalanceByAccount(walletAddress, balanceValueTextView, progressBar);
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getApplicationContext(), TAG, e);
        }
    }

    @Override
    public void onHomeStartComplete() {
        try {
            screenViewType(-1);
            beginTransaction(HomeWalletFragment.newInstance(), bundle);
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getApplicationContext(), TAG, e);
        }
    }

    @Override
    public void onHomeWalletCompleteByHomeMain( String indexKey) {
        try {
            getCurrentWallet(indexKey);
            screenViewType(0);
            beginTransaction(HomeMainFragment.newInstance(), bundle);
            notificationThread(1);
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getApplicationContext(), TAG, e);
        }
    }

    @Override
    public void onHomeWalletCompleteByWallets() {
        try {
            screenViewType(1);
            beginTransaction(WalletsFragment.newInstance(), bundle);
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getApplicationContext(), TAG, e);
        }
    }

    @Override
    public void onBlockchainNetworkDialogCancel() {
        try{
            screenViewType(0);
            beginTransaction(HomeMainFragment.newInstance(), bundle);
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getApplicationContext(), TAG, e);
        }
    }

    @SuppressLint("UnsafeIntentLaunch")
    @Override
    public void onBlockchainNetworkDialogOk() {
        try{
            finish();
            startActivity(getIntent());
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getApplicationContext(), TAG, e);
        }
    }

    @Override
    public void onBlockchainNetworkCompleteByBackArrow() {
        try{
            screenViewType(1);
            beginTransaction(SettingsFragment.newInstance(), bundle);
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getApplicationContext(), TAG, e);
        }
    }

    @Override
    public void onBlockchainNetworkCompleteByAdd() {
        try{
            screenViewType(1);
            beginTransaction(SettingsFragment.newInstance(), bundle);
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getApplicationContext(), TAG, e);
        }
    }


    @Override
    public void onSendComplete(String password) {
        try {
            bundle.putString("sendPassword", password);
            screenViewType(0);
            beginTransaction(HomeMainFragment.newInstance(), bundle);
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getApplicationContext(), TAG, e);
        }
    }

    @Override
    public void onReceiveComplete() {
        try {
            screenViewType(0);
            beginTransaction(HomeMainFragment.newInstance(), bundle);
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getApplicationContext(), TAG, e);
        }
    }

    @Override
    public void onAccountTransactionsComplete() {
        try {
            screenViewType(0);
            beginTransaction(HomeMainFragment.newInstance(), bundle);
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getApplicationContext(), TAG, e);
        }
    }

    @Override
    public void  onWalletsCompleteByBackArrow(){
        screenViewType(0);
        beginTransaction(HomeMainFragment.newInstance(), bundle);
    }

    @Override
    public void  onWalletsCompleteByCreateOrRestore(String walletPassword){
        screenViewType(1);
        bundle.putString("walletPassword", walletPassword);
        beginTransaction(HomeWalletFragment.newInstance(), bundle);
    }

    @Override
    public void  onWalletsCompleteBySwitchAddress(String indexKey){
        getCurrentWallet(indexKey);
        screenViewType(0);
        beginTransaction(HomeMainFragment.newInstance(), bundle);
    }

    @Override
    public void onWalletsCompleteByReveal(String walletAddress, String walletPassword){
        screenViewType(1);
        bundle.putString("walletAddress", walletAddress);
        bundle.putString("walletPassword", walletPassword);
        beginTransaction(RevealWalletFragment.newInstance(), bundle);
    }

    @Override
    public void onSettingsCompleteCompleteByBackArrow() {
        try {
            screenViewType(0);
            beginTransaction(HomeMainFragment.newInstance(), bundle);
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getApplicationContext(), TAG, e);
        }
    }

    @Override
    public void onSettingsCompleteByNetwork() {
        try {
            screenViewType(1);
            beginTransaction(BlockchainNetworkFragment.newInstance(), bundle);
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getApplicationContext(), TAG, e);
        }
    }

    @Override
    public void onRevealWalletComplete() {
        try {
            screenViewType(1);
            beginTransaction(WalletsFragment.newInstance(), bundle);
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

    private void screenViewType(int status) {
        try {
            // 0 - default main screen, 1 - fragment screen, -1 - Start app (default)

            int screenHeight = 0;

            switch (status) {
                case 0:
                    topLinearLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    topLinearLayout.setLayoutParams(topLinearLayoutParams);
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    centerRelativeLayout.setVisibility(View.VISIBLE);
                    blockChainNetworkTextView.setVisibility(View.VISIBLE);
                    break;
                case 1:
                    screenHeight = (Utility.calculateScreenWidthDp(getApplicationContext()) * 30 / 100);
                    topLinearLayoutParams.height = screenHeight;
                    topLinearLayout.setLayoutParams(topLinearLayoutParams);
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    centerRelativeLayout.setVisibility(View.GONE);
                    blockChainNetworkTextView.setVisibility(View.GONE);
                    break;
                default:
                    screenHeight = (Utility.calculateScreenWidthDp(getApplicationContext()) * 30 / 100);
                    topLinearLayoutParams.height = screenHeight;
                    topLinearLayout.setLayoutParams(topLinearLayoutParams);
                    bottomNavigationView.setVisibility(View.GONE);
                    centerRelativeLayout.setVisibility(View.GONE);
                    blockChainNetworkTextView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            GlobalMethods.ExceptionError(getApplicationContext(), TAG, e);
        }
    }

    private void getCurrentWallet(String indexKey) {
        if(PrefConnect.WALLET_ADDRESS_TO_INDEX_MAP != null) {
            for (Map.Entry<String, String> entry : PrefConnect.WALLET_INDEX_TO_ADDRESS_MAP.entrySet()) {
                if (Objects.equals(indexKey, entry.getKey())) {
                    PrefConnect.writeString(getApplicationContext(), PrefConnect.WALLET_CURRENT_ADDRESS_INDEX_KEY, indexKey);
                    walletAddress = entry.getValue();
                    break;
                }
            }
        }
        bundle.putString("walletAddress", walletAddress);
        walletAddressTextView.setText(walletAddress);
    }

    //Get balance task
    private void getBalanceByAccount(String address, TextView balanceValueTextView, ProgressBar progressBar) {
        try {
            linerLayoutOffline.setVisibility(View.GONE);

            //Internet connection check
            if (GlobalMethods.IsNetworkAvailable(getApplicationContext())) {

                progressBar.setVisibility(View.VISIBLE);

                String[] taskParams = {address};

                AccountBalanceRestTask task = new AccountBalanceRestTask(
                        getApplicationContext(), new AccountBalanceRestTask.TaskListener() {
                    @Override
                    public void onFinished(BalanceResponse balanceResponse) throws ServiceException {
                        if (balanceResponse.getResult().getBalance() != null) {
                            String value = balanceResponse.getResult().getBalance().toString();

                            KeyViewModel keyViewModel = new KeyViewModel();
                            String quantity = (String) keyViewModel.getWeiToDogeProtocol(value);

                            balanceValueTextView.setText(quantity);
                        }
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(com.dpwallet.app.api.read.ApiException e) {
                        progressBar.setVisibility(View.GONE);

                        int code = e.getCode();
                        boolean check = GlobalMethods.ApiExceptionSourceCodeBoolean(code);
                        if (check == true) {
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
                    if (accountStatus == 0) {
                        previewsQuantity[0] = "0";
                    }

                    try {
                        while (true) {

                            if (walletAddress.length() != GlobalMethods.ADDRESS_LENGTH) {
                                break;
                            }

                            String[] taskParams = {walletAddress};

                            AccountBalanceRestTask task = new AccountBalanceRestTask(
                                    getApplicationContext(), new AccountBalanceRestTask.TaskListener() {
                                @Override
                                public void onFinished(BalanceResponse balanceResponse) throws ServiceException {
                                    if (balanceResponse.getResult().getBalance() != null) {
                                        String value = balanceResponse.getResult().getBalance().toString();
                                        KeyViewModel keyViewModel = new KeyViewModel();
                                        currentQuantity[0] = (String) keyViewModel.getWeiToDogeProtocol(value);
                                        if (previewsQuantity[0] != null) {
                                            if (!previewsQuantity[0].equals(currentQuantity[0])) {
                                                balanceValueTextView.setText(currentQuantity[0]);
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
        } catch (Exception e) {
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(notificationID, notificationBuilder.build());
    }

    private void loadSeedsThread() {
        try {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        while (true) {
                           GlobalMethods.seedWords = new SeedWords();
                           boolean seed = GlobalMethods.seedWords.initializeSeedWordsFromUrl(getApplicationContext());
                           if (seed){
                               GlobalMethods.seedLoaded = true;
                               return;
                           }
                           Thread.sleep(1000);
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



}