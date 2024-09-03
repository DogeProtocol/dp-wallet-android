package com.dpwallet.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PrefConnect {
    //Key
    //public static String privatekey = "privatekey";

    public static String walletAddress = "walletAddress";
    public static final String PREF_NAME = "DP_QUANTUM_COIN_WALLET_APP_PREF";

    public static final int MAX_WALLETS = 128;
    public static String MAX_WALLET_INDEX_KEY = "MaxWalletIndex";
    public static String WALLET_KEY_PREFIX = "WALLET_";
    public static String WALLET_KEY_ADDRESS_INDEX = "ADDRESS_INDEX";
    public static String WALLET_KEY_INDEX_ADDRESS = "INDEX_ADDRESS";



    public static String WALLET_KEY_PASSWORD = "WALLET_PASSWORD";
    public static Map<String, String> WALLET_ADDRESS_TO_INDEX_MAP = new HashMap<>(); //key is address, value is index
    public static Map<String, String> WALLET_INDEX_TO_ADDRESS_MAP = new HashMap<>(); //key is index, value is address
    public static boolean  WALLET_ADDRESS_TO_INDEX_MAP_LOADED = false;

    public static String WALLET_CURRENT_ADDRESS_INDEX_KEY = "WALLET_CURRENT_ADDRESS_INDEX_KEY";
    public static String WALLET_CURRENT_ADDRESS_INDEX_VALUE = "0";


    public static String BLOCKCHAIN_NETWORK_ID_INDEX_KEY = "BLOCKCHAIN_NETWORK_ID_INDEX_KEY";



    public static void clearAllPrefs(Context context) {
        getEditor(context).clear().commit();
    }

    public static void writeBoolean(Context context, String key, boolean value) {
        getEditor(context).putBoolean(key, value).commit();
    }

    public static boolean readBoolean(Context context, String key, boolean defValue) {
        return getPreferences(context).getBoolean(key, defValue);
    }

    public static void writeInteger(Context context, String key, int value) {
        getEditor(context).putInt(key, value).commit();
    }

    public static int readInteger(Context context, String key, int defValue) {
        return getPreferences(context).getInt(key, defValue);
    }

    public static void writeString(Context context, String key, String value) {
        getEditor(context).putString(key, value).commit();
    }

    public static String readString(Context context, String key, String defValue) {
        if (getPreferences(context).contains(key)) {
            return getPreferences(context).getString(key, defValue);
        }
        else
        {
            return defValue;
        }
    }

    public static void writeFloat(Context context, String key, float value) {
        getEditor(context).putFloat(key, value).commit();
    }

    public static float readFloat(Context context, String key, float defValue) {
        return getPreferences(context).getFloat(key, defValue);
    }

    public static void writeLong(Context context, String key, long value) {
        getEditor(context).putLong(key, value).commit();
    }

    public static long readLong(Context context, String key, long defValue) {
        return getPreferences(context).getLong(key, defValue);
    }

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }


    public static void saveHasMap(Context context, String key, Map<String,String> inputMap){
        SharedPreferences pSharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if (pSharedPref != null){
            Gson gson = new Gson();
            String jsonText = gson.toJson(inputMap);

            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.putString(key, jsonText);
            editor.apply();
        }
    }

    public static void saveArrayMap(Context context, String key, ArrayList<String> data){
        SharedPreferences pSharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        if (pSharedPref != null) {
            Gson gson = new Gson();
            List<String> textList = new ArrayList<String>(data);
            String jsonText = gson.toJson(textList);

            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.putString(key, jsonText);
            editor.apply();
        }
    }


    public static Map<String,String> loadHashMap(Context context, String key){
        Map<String,String> outputMap = new HashMap<String,String>();
        SharedPreferences pSharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        try{
            if (pSharedPref != null){
                Gson gson = new Gson();
                String json = pSharedPref.getString(key,"");
                if(json.length()>3) {
                    java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
                    }.getType();
                    outputMap = gson.fromJson(json, type);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return outputMap;
    }

    public static ArrayList<String> loadArrayMap(Context context, String key){
        SharedPreferences pSharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        try{
            if (pSharedPref != null){
                Gson gson = new Gson();
                String jsonText =  pSharedPref.getString(key, (new JSONObject()).toString());
                if(jsonText.length()>3) {
                    return gson.fromJson(jsonText, ArrayList.class);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return  null;
    }


    public static String getSha256Hash(String password) {
        try {
            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
            }
            digest.reset();
            return bin2hex(digest.digest(password.getBytes()));
        } catch (Exception ignored) {
            return null;
        }
    }

    private static String bin2hex(byte[] data) {
        StringBuilder hex = new StringBuilder(data.length * 2);
        for (byte b : data)
            hex.append(String.format("%02x", b & 0xFF));
        return hex.toString();
    }

}
