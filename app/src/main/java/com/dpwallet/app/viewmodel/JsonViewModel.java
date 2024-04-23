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

    public String getSendByLangValues() {
        try {
            return _jsonInteract.getSendByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getReceiveByLangValues() {
        try {
            return _jsonInteract.getReceiveByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getTransactionsByLangValues() {
        try {
            return _jsonInteract.getTransactionsByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getCopyByLangValues() {
        try {
            return _jsonInteract.getCopyByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getBackByLangValues() {
        try {
            return _jsonInteract.getBackByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getBalanceByLangValues() {
        try {
            return _jsonInteract.getBalanceByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getRefreshByLangValues() {
        try {
            return _jsonInteract.getRefreshByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getCompletedTransactionsByLangValues() {
        try {
            return _jsonInteract.getCompletedTransactionsByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getPendingTransactionsByLangValues() {
        try {
            return _jsonInteract.getPendingTransactionsByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getBackupByLangValues() {
        try {
            return _jsonInteract.getBackupByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getRestoreByLangValues() {
        try {
            return _jsonInteract.getRestoreByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getWalletsByLangValues() {
        try {
            return _jsonInteract.getWalletsByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getSettingsByLangValues() {
        try {
            return _jsonInteract.getSettingsByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getUnlockByLangValues() {
        try {
            return _jsonInteract.getUnlockByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getUnlockWalletByLangValues() {
        try {
            return _jsonInteract.getUnlockWalletByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getScanWalletByLangValues() {
        try {
            return _jsonInteract.getScanWalletByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getBlockExplorerByLangValues() {
        try {
            return _jsonInteract.getBlockExplorerByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getSelectNetworkByLangValues() {
        try {
            return _jsonInteract.getSelectNetworkByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getEnterWalletPasswordWalletByLangValues() {
        try {
            return _jsonInteract.getEnterWalletPasswordWalletByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getEnterApasswordByLangValues() {
        try {
            return _jsonInteract.getEnterApasswordByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getShowPasswordByLangValues() {
        try {
            return _jsonInteract.getShowPasswordByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getPasswordByLangValues() {
        try {
            return _jsonInteract.getPasswordByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getSetWalletPassowrdByLangValues() {
        try {
            return _jsonInteract.getSetWalletPassowrdByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getQuizByLangValues() {
        try {
            return _jsonInteract.getQuizByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getGetCoinsForDogePTokensByLangValues() {
        try {
            return _jsonInteract.getGetCoinsForDogePTokensByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getWalletPathByLangValues() {
        try {
            return _jsonInteract.getWalletPathByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getSetWalletPasswordByLangValues() {
        try {
            return _jsonInteract.getSetWalletPasswordByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getUseStrongPasswordByLangValues() {
        try {
            return _jsonInteract.getUseStrongPasswordByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getRetypePasswordByLangValues() {
        try {
            return _jsonInteract.getRetypePasswordByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getRetypeThePasswordByLangValues() {
        try {
            return _jsonInteract.getRetypeThePasswordByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getCreateRestoreWalletByLangValues() {
        try {
            return _jsonInteract.getCreateRestoreWalletByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getSelectAnOptionByLangValues() {
        try {
            return _jsonInteract.getSelectAnOptionByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getCreateNewWalletByLangValues() {
        try {
            return _jsonInteract.getCreateNewWalletByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getRestoreWalletFromSeedByLangValues() {
        try {
            return _jsonInteract.getRestoreWalletFromSeedByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getRestoreWalletFromBackupFileByLangValues() {
        try {
            return _jsonInteract.getRestoreWalletFromBackupFileByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
