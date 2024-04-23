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
import java.util.ArrayList;

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
    public int getInfoLength() {
        try {
            return _jsonInteract.getInfoLength();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public String getTitleByInfo(int index) {
        try {
            return _jsonInteract.getTitleByInfo(index);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getDescByInfo(int index) {
        try {
            return _jsonInteract.getDescByInfo(index);
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
    public String getQuizWrongAnswer(){
        try {
            return _jsonInteract.getQuizWrongAnswer();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getQuizNoChoice(){
        try {
            return _jsonInteract.getQuizNoChoice();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public int getQuizLength() {
        try {
            return _jsonInteract.getQuizLength();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public String getTitleByQuiz(int index) {
        try {
            return _jsonInteract.getTitleByQuiz(index);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getQuestionByQuiz(int index) {
        try {
            return _jsonInteract.getQuestionByQuiz(index);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public ArrayList<String> getChoicesByQuiz(int index) {
        try {
            return _jsonInteract.getChoicesByQuiz(index);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public int getCorrectChoiceByQuiz(int index){
        try {
            return _jsonInteract.getCorrectChoiceByQuiz(index);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public String getAfterQuizInfoByQuiz(int index){
        try {
            return _jsonInteract.getAfterQuizInfoByQuiz(index);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getTitleByLangValues(){
        try {
            return _jsonInteract.getTitleByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getNextByLangValues() {
        try {
            return _jsonInteract.getNextByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getOkByLangValues() {
        try {
            return _jsonInteract.getOkByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getCancelByLangValues() {
        try {
            return _jsonInteract.getCancelByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getCloseByLangValues() {
        try {
            return _jsonInteract.getCloseByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getSubmitByLangValues() {
        try {
            return _jsonInteract.getSubmitByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
