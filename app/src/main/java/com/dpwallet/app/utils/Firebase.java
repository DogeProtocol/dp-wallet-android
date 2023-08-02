package com.dpwallet.app.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;

public class Firebase {

    public static void Analytics(Context context, Bundle bundle){
        //Activity activity = (Activity) context;
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    //include all activity
    public static void CrashReport(Throwable ex){
        FirebaseCrash.report(new Exception(ex));
    }

    // include globalmethods -> apiExceptionSourceCodeRoute module
    public static void CrashLog(String exception){
        FirebaseCrash.log(exception);
    }

    // include globalmethods -> ExceptionError module
    public static void CrashLogcat(String tag, String exception){
        FirebaseCrash.logcat(Log.DEBUG, tag, exception);
    }

}
