package com.dpwallet.app.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class Utility {

    public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthdp=180
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
        return noOfColumns;
    }

    public static int calculateScreenWidthDp(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        //int screenWidthDp = (int) (displayMetrics.widthPixels / displayMetrics.density);
        return displayMetrics.widthPixels;
    }

    public static int calculateScreenHeightDp(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        //int screenHeightDp = (int) (displayMetrics.heightPixels / displayMetrics.density);
        return displayMetrics.heightPixels;
    }

    //public static float dpFromPx(final Context context, final float px) {
    //    return px / context.getResources().getDisplayMetrics().density;
    //}

    //public static float pxFromDp(final Context context, final float dp) {
    //    return dp * context.getResources().getDisplayMetrics().density;
    //}

}