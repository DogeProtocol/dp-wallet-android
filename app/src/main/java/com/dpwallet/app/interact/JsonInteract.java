package com.dpwallet.app.interact;

import android.content.Context;

import com.dpwallet.app.entity.KeyServiceException;
import com.dpwallet.app.entity.Result;
import com.dpwallet.app.entity.ServiceException;
import com.dpwallet.app.keystorage.IKeyStore;
import com.dpwallet.app.services.IKeyService;
import com.dpwallet.app.utils.GlobalMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.InvalidKeyException;

import timber.log.Timber;

public class JsonInteract {
    private JSONObject jsonObject;

    public JsonInteract(String jsonString) throws JSONException {
        jsonObject  = new JSONObject(jsonString);
    }
    public String getInfoStep() throws JSONException{
        return jsonObject.getString(GlobalMethods.InfoStep);
    }
    public JSONArray getInfo() throws JSONException{
        return jsonObject.getJSONArray(GlobalMethods.Info);
    }

    public String getQuizStep() throws JSONException{
        return jsonObject.getString(GlobalMethods.QuizStep);
    }
    public JSONArray getQuiz() throws JSONException{
        return jsonObject.getJSONArray(GlobalMethods.Quiz);
    }

    public JSONObject getLangValues() throws JSONException{
        return jsonObject.getJSONObject(GlobalMethods.LangValues);
    }

}
