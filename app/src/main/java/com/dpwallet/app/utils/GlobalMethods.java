package com.dpwallet.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RawRes;

import com.dpwallet.app.R;

import java.io.InputStream;
import java.util.Locale;
import java.util.Scanner;

public class GlobalMethods {

    public static Toast toast;

    public static String step = "[STEP]";
    public static String totalSteps = "[TOTAL_STEPS]";


/*
    //joson field
    public static String info = "info";
    public static String infoStep = "infoStep";
    public static String step = "[STEP]";
    public static String totalSteps = "[TOTAL_STEPS]";

    public static String quizStep = "quizStep";

    public static String quizWrongAnswer = "quizWrongAnswer";
    public static String quizNoChoice = "quizNoChoice";
    public static String quiz = "quiz";

    public static String title = "title";
    public static String desc = "desc";
    public static String question = "question";
    public static String choices = "choices";
    public static String correctChoice = "correctChoice";
    public static String afterQuizInfo = "afterQuizInfo";

    public static String langValues = "langValues";
    public static String next = "next";
    public static String ok = "ok";
    public static String cancel = "cancel";
    public static String close = "close";
    public static String submit = "submit";
    public static String send = "Send";
    public static String receive = "Receive";
    public static String transactions = "Transactions";
    public static String copy = "Copy";
    public static String back = "Back";

    public static String balance = "Balance";
    public static String refresh = "Refresh";
    public static String completedTransactions = "Completed Transactions";
    public static String pendingTransactions = "Pending Transactions";
    public static String backup = "Backup";
    public static String restore = "Restore";
    public static String wallets = "Wallets";

    public static String settings = "Settings";
    public static String unlock = "Unlock";
    public static String unlockWallet = "Unlock wallet";
    public static String scan = "DpScan";
    public static String blockExplorer = "Block Explorer";
    public static String selectNetwork=" selectNetwork";
    public static String enterWalletPassword = "Enter Quantum Wallet Password";
    public static String enterApassword = "Enter a password";
    public static String showPassword = "Show Password";

    public static String password = "Password";
    public static String setWalletPassowrd = "Set Wallet Password";
    public static String quizLangValue = "quiz";
    public static String getCoinsForDogePTokens = "Get Coins For DogeP Tokens";
    public static String walletPath = "Wallet Path";
    public static String setWalletPassword = "Set Wallet Password";
    public static String useStrongPassword = "Use a strong and long password. And do not forget it!";
    public static String retypePassword = "Retype Password";
    public static String retypeThePassword = "Retype the password";
    public static String createRestoreWallet = "Create or Restore Quantum Wallet";
    public static String selectAnOption = "Select an option";
    public static String createNewWallet = "Create New Quantum Wallet";
    public static String restoreWalletFromSeed = "Restore A Quantum Wallet From Seed Words";
    public static String restoreWalletFromBackupFile = "Restore A Quantum Wallet From a Backup File";
    public static String seedWords = "Seed Words";
    public static String seedWordsInfo1 = "Ensure that no one is looking at the screen other than you.";
    public static String seedWordsInfo2 = "Ensure that there is no camera pointed at this screen, including from your phone.";
    public static String seedWordsInfo3 = "You should save the seed words safely offline and keep multiple copies in a trustworthy and safe location.";
    public static String seedWordsInfo4 = "If these seed words are stolen or someone else gets access to them, your wallet is compromised.";
    public static String seedWordsShow = "Click here to reveal the seed words.";
    public static String verifySeedWords = "Verify Seed Words";
    public static String verifyWalletPassword = "Verify Current Quantum Wallet Password";
    public static String verifyWalletPasswordInfo = "Retype your current quantum wallet password, to verify that you remember it. Upon verification, your wallet will be saved.";
    public static String waitWalletSave = "Please wait while your wallet is being saved with strong encryption. This can take upto a minute or so to complete..";
    public static String walletSaved = "Your wallet has been saved successfully.";

    public static String backupWallet = "Backup Wallet";
    public static String backupWalletInfo1 = "For additional safety, please make sure that you keep backup copies in atleast three different devices offline.";
    public static String backupWalletInfo2 = "And remember you need your wallet password to restore the backup!";
    public static String backupWalletSkip = "Click here to skip this step";
    public static String backupWait = "Please wait while your wallet is being backed up with strong encryption. This can take upto a minute or so to complete...";
    public static String walletBackedUp = "Your wallet has been backed up successfully. Remember you need your wallet password in order to restore it.";
    public static String restoreWalletFromBackup = "Restore Quantum Wallet From Backup File";
    public static String enterAboveWalletPassword = "Enter the above wallet's password";
    public static String walletFileRestoreWait = "Please wait while your wallet is being decrypted and opened. This can take upto a minute.";
    public static String waitRevealSeed = "Please wait while your wallet seed words are being decrypted and opened. This can take upto a minute.";
    public static String waitWalletOpen = "Please wait while your wallet is being decrypted and opened. This can take upto a minute.";

*/
    ////////////////////////////////////////////////////////////////////////////////////////////////

    //URL
    public static String READ_API_URL = "https://scan.dpapi.org";
    public static String WRITE_API_URL = "https://txn.dpapi.org";

    public static String DP_DOCS_URL = "https://dpdocs.org/";
    public static String DP_SCAN_URL = "https://dpscan.app";
    public static String DP_SCAN_TX_HASH_URL = "https://dpscan.app/txn/{txhash}";
    public static String DP_SCAN_ACCOUNT_TRANSACTION_URL = "https://dpscan.app/account/{address}/txn/page";

    public static String FAUCET_API_URL = "https://faucet.dpapi.org";

    //Network
    public static String NETWORK_NAME = "T3";
    public static String CHAIN_ID = "36996";
    public static String GAS_LIMIT = "21000";

    //public static String NONCE = "0";

    public static int DURATION = 20;
    public static int MINIMUM_PASSWORD_LENGTH = 12;
    public static int ADDRESS_LENGTH = 66;
    public static String ADDRESS_START_PREFIX = "0x";

   // public static int TOAST_SHOW_LENGTH = 1;

    //public static Context context = null;

    //String Values to be Used in App
    public static final String downloadDirectory = "dpWallet";
    public static final String mainUrl = "https://dpscan.app/demo/";


    public static String LocaleLanguage(Context context, String languageKey){
        if (languageKey.equals("en")) {
            return readRawResource(context, R.raw.en_us);
        }
        return readRawResource(context, R.raw.en_us);
    }

    public static String readRawResource(Context context,  @RawRes int res) {
        return readStream(context.getResources().openRawResource(res));
    }

    private static String readStream(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    /*
    public static void LocaleLanguage(Context context, String TAG, String languagekey){
        try {
            if (TextUtils.isEmpty(languagekey)) {
                languagekey = "en";
            }


            Locale locale = new Locale(languagekey);
            Locale.setDefault(locale);
            android.content.res.Configuration config = new Configuration();
            config.locale = locale;

            context.getResources().updateConfiguration(config,
                    context.getResources().getDisplayMetrics());
        }catch(Exception ex){
            GlobalMethods.ExceptionError(context, TAG, ex);
        }
    }
*/

    public static boolean IsNetworkAvailable(Context context) {
        if (context == null) {
            return false;
        }
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()) {
            return false;
        }
        return true;
    }

    //api exception error
    public static void ExceptionError(Context context, String tag, Exception e) {
        ShowToast(context, tag + " : " + e.getMessage());
        //Firebase.CrashLogcat(tag, e.toString());
    }

    //Api exception error onFailure(ApiException e)
    public static boolean ApiExceptionSourceCodeBoolean(int code) {
        boolean checkExcaption = true;
        switch (code) {
            case 401: //Unauthorized
                break;
            case 404: //NotFound
                break;
            case 406: //NotAcceptable (Invalid header)
                break;
            case 409: //Conflict (Try again)
                break;
            case 417: //ExpectationFailed(captha verification)
                break;
            default:
                checkExcaption = false;
                break;
        }
        return checkExcaption;
    }

    //api exception error route
    public static void ApiExceptionSourceCodeRoute(Context context, int code,
                                                   String displayerrormessage, String exceptionError) {
        Activity activity = (Activity) context;

        switch (code) {
            case 401: //Unauthorized
                GlobalMethods.ShowToast(context, activity.getString(R.string.code_401));
                break;
            case 404: //NotFound
                GlobalMethods.ShowToast(context, activity.getString(R.string.code_404));
                break;
            case 406: //NotAcceptable (Invalid header)
                GlobalMethods.ShowToast(context, activity.getString(R.string.code_406));
                break;
            case 409: //Conflict (Try again)
                GlobalMethods.ShowToast(context, activity.getString(R.string.code_409));
                break;
            case 417: //ExpectationFailed(captha verification)
                GlobalMethods.ShowToast(context, activity.getString(R.string.code_417));
                break;
            default:
                GlobalMethods.ShowToast(context, displayerrormessage);
                break;
        }

        //FireBase log
        Firebase.CrashLog(exceptionError);
    }

    //Exception Offline Or exception error
    public static void OfflineOrExceptionError(Context context, LinearLayout layout, ImageView image,
                                               TextView textTitle, TextView textSubTitle,
                                               boolean isNetworkAvailable){
        //Activity activity = (Activity) context;

        layout.setVisibility(View.VISIBLE);

        if(isNetworkAvailable==false)
        {
            image.setImageResource(R.drawable.noconnection);
            textTitle.setText(context.getString(R.string.general_connect_internet));
            textSubTitle.setText(context.getString(R.string.general_offline_access));
        }
        else
        {
            image.setImageResource(R.drawable.noconnection);
            textTitle.setText(context.getString(R.string.offline_exception_error_title));
            textSubTitle.setText(context.getString(R.string.offline_exception_error_subtitle));
        }
    }


    public static void ShowToast(Context context, String message) {
        // Set the toast and duration
        int toastDurationInMilliSeconds = 600;
        toast = Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_LONG);

        // Set the countdown to display the toast
        CountDownTimer toastCountDown;
        Toast finalToast = toast;
        toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 1) {
            public void onTick(long millisUntilFinished) {
                finalToast.show();
            }
            public void onFinish() {
                finalToast.cancel();
            }
        };

        // Show the toast and starts the countdown
        toast.show();
        toastCountDown.start();
    }

}

