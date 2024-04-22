package com.dpwallet.app.viewmodel;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.dpwallet.app.entity.KeyServiceException;
import com.dpwallet.app.entity.ServiceException;
import com.dpwallet.app.hybrid.HybridPqcJNIImpl;
import com.dpwallet.app.hybrid.IHybridPqcJNIImpl;
import com.dpwallet.app.interact.JsonInteract;
import com.dpwallet.app.interact.KeyInteract;
import com.dpwallet.app.keystorage.IKeyStore;
import com.dpwallet.app.keystorage.KeyStore;
import com.dpwallet.app.services.IKeyService;
import com.dpwallet.app.services.KeyService;
import com.dpwallet.app.utils.GlobalMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.security.InvalidKeyException;

//@HiltViewModel
public class JsonViewModel extends ViewModel{
  //@Inject
    private JsonInteract _jsonInteract;

    //@Inject
    public JsonViewModel(Context context, String languageKey) {
        try {
            String jsonString = GlobalMethods.LocaleLanguage(context,  languageKey);
            _jsonInteract = new JsonInteract(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public String getInfoStep() {
        try {
            return _jsonInteract.getInfoStep();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public JSONArray getInfo() {
        try {
            return _jsonInteract.getInfo();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getQuizStep() {
        try {
            return _jsonInteract.getQuizStep();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public JSONArray getQuiz() {
        try {
            return _jsonInteract.getQuiz();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public JSONObject getLangValues() {
        try {
            return _jsonInteract.getLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
