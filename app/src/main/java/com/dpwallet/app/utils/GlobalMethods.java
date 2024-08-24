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
import com.dpwallet.app.seedwords.SeedWords;

import java.io.InputStream;
import java.util.Locale;
import java.util.Scanner;

public class GlobalMethods {

    public static Toast toast;

    public static String step = "[STEP]";
    public static String totalSteps = "[TOTAL_STEPS]";


    //URL
    public static String READ_API_URL = "https://scan.dpapi.org";
    public static String WRITE_API_URL = "https://txn.dpapi.org";

    public static String DP_DOCS_URL = "https://dpdocs.org/";
    public static String BLOCK_EXPLORER_URL = "https://dpscan.app";
    public static String BLOCK_EXPLORER_TX_HASH_URL = "https://dpscan.app/txn/{txhash}";
    public static String BLOCK_EXPLORER_ACCOUNT_TRANSACTION_URL = "https://dpscan.app/account/{address}/txn/page";

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

    public static SeedWords seedWords;
    public static boolean seedLoaded = false;



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

    public static int[] GetIntDataArrayByString(String d) {
        String[] data = d.split(",");
        int[] intData = new int[data.length];
        for(int i = 0;i < intData.length;i++)
        {
            intData[i] = Integer.parseInt(data[i]);
        }
        return intData;
    }

    public static int[] GetIntDataArrayByStringArray(String[] string) {
        int number[] = new int[string.length];

        for (int i = 0; i < string.length; i++) {
            number[i] = Integer.parseInt(string[i]); // error here
        }
        return number;
    }

}

