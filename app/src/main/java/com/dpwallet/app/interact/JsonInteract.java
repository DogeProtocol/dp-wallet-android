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

import java.lang.reflect.Array;
import java.security.InvalidKeyException;
import java.util.ArrayList;

import timber.log.Timber;

public class JsonInteract {
    private JSONObject jsonObject;

    public JsonInteract(String jsonString) throws JSONException {
        jsonObject  = new JSONObject(jsonString);
    }
    public String getInfoStep() throws JSONException{
        return jsonObject.getString(GlobalMethods.InfoStep);
    }

    public int getInfoLength() throws JSONException{
        return getInfo().length();
    }

    public String getTitleByInfo(int index) throws JSONException{
        JSONObject object  = getInfo().getJSONObject(index);
        return object.getString(GlobalMethods.Title);
    }

    public String getDescByInfo(int index) throws JSONException{
        JSONObject object  = getInfo().getJSONObject(index);
        return object.getString(GlobalMethods.Desc);
    }


    public String getQuizStep() throws JSONException{
        return jsonObject.getString(GlobalMethods.QuizStep);
    }

    public String getQuizWrongAnswer() throws JSONException{
        return jsonObject.getString(GlobalMethods.QuizWrongAnswer);
    }
    public String getQuizNoChoice() throws JSONException{
        return jsonObject.getString(GlobalMethods.QuizNoChoice);
    }

    public int getQuizLength() throws JSONException{
        return getQuiz().length();
    }

    public String getTitleByQuiz(int index) throws JSONException{
        JSONObject object  = getQuiz().getJSONObject(index);
        return object.getString(GlobalMethods.Title);
    }

    public String getQuestionByQuiz(int index) throws JSONException{
        JSONObject object  = getQuiz().getJSONObject(index);
        return object.getString(GlobalMethods.Question);
    }

    public ArrayList<String> getChoicesByQuiz(int index) throws JSONException{
        JSONObject object  = getQuiz().getJSONObject(index);
        JSONArray jsonArray = object.getJSONArray(GlobalMethods.Choices);

        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(jsonArray.getString(0));
        arrayList.add(jsonArray.getString(1));
        arrayList.add(jsonArray.getString(2));
        arrayList.add(jsonArray.getString(3));

        return arrayList;
    }

    public int getCorrectChoiceByQuiz(int index) throws JSONException{
        JSONObject object = getQuiz().getJSONObject(index);
        return object.getInt(GlobalMethods.CorrectChoice);
    }

    public String getAfterQuizInfoByQuiz(int index) throws JSONException{
        JSONObject object = getQuiz().getJSONObject(index);
        return object.getString(GlobalMethods.AfterQuizInfo);
    }



    public String getTitleByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.Title);
    }
    public String getNextByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.Next);
    }
    public String getOkByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.Ok);
    }
    public String getCancelByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.Cancel);
    }
    public String getCloseByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.Close);
    }
    public String getSubmitByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.Submit);
    }


    private JSONArray getInfo() throws JSONException{
        return jsonObject.getJSONArray(GlobalMethods.Info);
    }
    private JSONArray getQuiz() throws JSONException{
        return jsonObject.getJSONArray(GlobalMethods.Quiz);
    }
    private JSONObject getLangValues() throws JSONException{
        return jsonObject.getJSONObject(GlobalMethods.LangValues);
    }

}
