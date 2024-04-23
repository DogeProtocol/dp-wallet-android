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
        return jsonObject.getString(GlobalMethods.infoStep);
    }

    public int getInfoLength() throws JSONException{
        return getInfo().length();
    }

    public String getTitleByInfo(int index) throws JSONException{
        JSONObject object  = getInfo().getJSONObject(index);
        return object.getString(GlobalMethods.title);
    }

    public String getDescByInfo(int index) throws JSONException{
        JSONObject object  = getInfo().getJSONObject(index);
        return object.getString(GlobalMethods.desc);
    }


    public String getQuizStep() throws JSONException{
        return jsonObject.getString(GlobalMethods.quizStep);
    }

    public String getQuizWrongAnswer() throws JSONException{
        return jsonObject.getString(GlobalMethods.quizWrongAnswer);
    }
    public String getQuizNoChoice() throws JSONException{
        return jsonObject.getString(GlobalMethods.quizNoChoice);
    }

    public int getQuizLength() throws JSONException{
        return getQuiz().length();
    }

    public String getTitleByQuiz(int index) throws JSONException{
        JSONObject object  = getQuiz().getJSONObject(index);
        return object.getString(GlobalMethods.title);
    }

    public String getQuestionByQuiz(int index) throws JSONException{
        JSONObject object  = getQuiz().getJSONObject(index);
        return object.getString(GlobalMethods.question);
    }

    public ArrayList<String> getChoicesByQuiz(int index) throws JSONException{
        JSONObject object  = getQuiz().getJSONObject(index);
        JSONArray jsonArray = object.getJSONArray(GlobalMethods.choices);

        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(jsonArray.getString(0));
        arrayList.add(jsonArray.getString(1));
        arrayList.add(jsonArray.getString(2));
        arrayList.add(jsonArray.getString(3));

        return arrayList;
    }

    public int getCorrectChoiceByQuiz(int index) throws JSONException{
        JSONObject object = getQuiz().getJSONObject(index);
        return object.getInt(GlobalMethods.correctChoice);
    }

    public String getAfterQuizInfoByQuiz(int index) throws JSONException{
        JSONObject object = getQuiz().getJSONObject(index);
        return object.getString(GlobalMethods.afterQuizInfo);
    }



    public String getTitleByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.title);
    }
    public String getNextByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.next);
    }
    public String getOkByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.ok);
    }
    public String getCancelByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.cancel);
    }
    public String getCloseByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.close);
    }
    public String getSubmitByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.submit);
    }
    public String getSendByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.send);
    }
    public String getReceiveByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.receive);
    }
    public String getTransactionsByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.transactions);
    }
    public String getCopyByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.copy);
    }
    public String getBackByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.back);
    }
    public String getBalanceByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.balance);
    }
    public String getRefreshByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.refresh);
    }
    public String getCompletedTransactionsByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.completedTransactions);
    }
    public String getPendingTransactionsByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.pendingTransactions);
    }
    public String getBackupByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.backup);
    }
    public String getRestoreByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.restore);
    }
    public String getWalletsByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.wallets);
    }
    public String getSettingsByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.settings);
    }
    public String getUnlockByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.unlock);
    }
    public String getUnlockWalletByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.unlockWallet);
    }

    public String getScanWalletByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.scan);
    }
    public String getBlockExplorerByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.blockExplorer);
    }
    public String getSelectNetworkByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.selectNetwork);
    }
    public String getEnterWalletPasswordWalletByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.enterWalletPassword);
    }
    public String getEnterApasswordByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.enterApassword);
    }
    public String getShowPasswordByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.showPassword);
    }
    public String getPasswordByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.password);
    }
    public String getSetWalletPassowrdByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.setWalletPassowrd);
    }
    public String getQuizByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.quizLangValue);
    }
    public String getGetCoinsForDogePTokensByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.getCoinsForDogePTokens);
    }

    public String getWalletPathByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.walletPath);
    }
    public String getSetWalletPasswordByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.setWalletPassword);
    }
    public String getUseStrongPasswordByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.useStrongPassword);
    }
    public String getRetypePasswordByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.retypePassword);
    }
    public String getRetypeThePasswordByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.retypeThePassword);
    }
    public String getCreateRestoreWalletByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.createRestoreWallet);
    }
    public String getSelectAnOptionByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.selectAnOption);
    }
    public String getCreateNewWalletByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.createNewWallet);
    }
    public String getRestoreWalletFromSeedByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.restoreWalletFromSeed);
    }
    public String getRestoreWalletFromBackupFileByLangValues() throws JSONException{
        return getLangValues().getString(GlobalMethods.restoreWalletFromBackupFile);
    }





    private JSONArray getInfo() throws JSONException{
        return jsonObject.getJSONArray(GlobalMethods.info);
    }
    private JSONArray getQuiz() throws JSONException{
        return jsonObject.getJSONArray(GlobalMethods.quiz);
    }
    private JSONObject getLangValues() throws JSONException{
        return jsonObject.getJSONObject(GlobalMethods.langValues);
    }

}
