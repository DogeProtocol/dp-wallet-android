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
    public String getSeedWordsByLangValues() {
        try {
            return _jsonInteract.getSeedWordsByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getSeedWordsInfo1ByLangValues() {
        try {
            return _jsonInteract.getSeedWordsInfo1ByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getSeedWordsInfo2ByLangValues() {
        try {
            return _jsonInteract.getSeedWordsInfo2ByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getSeedWordsInfo3ByLangValues() {
        try {
            return _jsonInteract.getSeedWordsInfo3ByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getSeedWordsInfo4ByLangValues() {
        try {
            return _jsonInteract.getSeedWordsInfo4ByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getSeedWordsShowByLangValues() {
        try {
            return _jsonInteract.getSeedWordsShowByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getVerifySeedWordsByLangValues() {
        try {
            return _jsonInteract.getVerifySeedWordsByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getVerifyWalletPasswordByLangValues() {
        try {
            return _jsonInteract.getVerifyWalletPasswordByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getVerifyWalletPasswordInfoByLangValues() {
        try {
            return _jsonInteract.getVerifyWalletPasswordInfoByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getWaitWalletSaveByLangValues() {
        try {
            return _jsonInteract.getWaitWalletSaveByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String getWalletSavedByLangValues() {
        try {
            return _jsonInteract.getWalletSavedByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getBackupWalletByLangValues() {
        try {
            return _jsonInteract.getBackupWalletByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getBackupWalletInfo1ByLangValues() {
        try {
            return _jsonInteract.getBackupWalletInfo1ByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getBackupWalletInfo2ByLangValues() {
        try {
            return _jsonInteract.getBackupWalletInfo2ByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getBackupWalletSkipByLangValues() {
        try {
            return _jsonInteract.getBackupWalletSkipByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getBackupWaitByLangValues() {
        try {
            return _jsonInteract.getBackupWaitByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getWalletBackedUpByLangValues() {
        try {
            return _jsonInteract.getWalletBackedUpByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getRestoreWalletFromBackupByLangValues() {
        try {
            return _jsonInteract.getRestoreWalletFromBackupByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getEnterAboveWalletPasswordByLangValues() {
        try {
            return _jsonInteract.getEnterAboveWalletPasswordByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getWalletFileRestoreWaitByLangValues() {
        try {
            return _jsonInteract.getWalletFileRestoreWaitByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getWaitRevealSeedByLangValues() {
        try {
            return _jsonInteract.getWaitRevealSeedByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getWaitWalletOpenByLangValues() {
        try {
            return _jsonInteract.getWaitWalletOpenByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getOpenByLangValues() {
        try {
            return _jsonInteract.getOpenByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getTotal_balanceByLangValues() {
        try {
            return _jsonInteract.getTotal_balanceByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getHelpByLangValues() {
        try {
            return _jsonInteract.getHelpByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getDpscanByLangValues() {
        try {
            return _jsonInteract.getDpscanByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getAddressByLangValues() {
        try {
            return _jsonInteract.getAddressByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getCoinsByLangValues() {
        try {
            return _jsonInteract.getCoinsByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getRevealSeedByLangValues() {
        try {
            return _jsonInteract.getRevealSeedByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getCreateOrRestoreWalletByLangValues() {
        try {
            return _jsonInteract.getCreateOrRestoreWalletByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getRevealByLangValues() {
        try {
            return _jsonInteract.getRevealByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getWaitUnlockByLangValues() {
        try {
            return _jsonInteract.getWaitUnlockByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getNetworksByLangValues() {
        try {
            return _jsonInteract.getNetworksByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getIdByLangValues() {
        try {
            return _jsonInteract.getIdByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getNameByLangValues() {
        try {
            return _jsonInteract.getNameByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getScanApiUrlByLangValues() {
        try {
            return _jsonInteract.getScanApiUrlByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getTxnApiUrlByLangValues() {
        try {
            return _jsonInteract.getTxnApiUrlByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getBlockExplorerUrlByLangValues() {
        try {
            return _jsonInteract.getBlockExplorerUrlByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getAddNetworkByLangValues() {
        try {
            return _jsonInteract.getAddNetworkByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getAddByLangValues() {
        try {
            return _jsonInteract.getAddByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getEnterNetwork_jsonByLangValues() {
        try {
            return _jsonInteract.getEnterNetwork_jsonByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getAddNetworkWarnByLangValues() {
        try {
            return _jsonInteract.getAddNetworkWarnByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getNetworkAddedByLangValues() {
        try {
            return _jsonInteract.getNetworkAddedByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getGetCoinsForTokensByLangValues() {
        try {
            return _jsonInteract.getGetCoinsForTokensByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getGetCoinsForTokensInfoByLangValues() {
        try {
            return _jsonInteract.getGetCoinsForTokensInfoByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getChooseEthWalletOptionByLangValues() {
        try {
            return _jsonInteract.getChooseEthWalletOptionByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getEthOptionSeedByLangValues() {
        try {
            return _jsonInteract.getEthOptionSeedByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getEthOptionPrivateKeyByLangValues() {
        try {
            return _jsonInteract.getEthOptionPrivateKeyByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getEthOptionKeystoreByLangValues() {
        try {
            return _jsonInteract.getEthOptionKeystoreByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getEthOptionManualByLangValues() {
        try {
            return _jsonInteract.getEthOptionManualByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getEnterEthSeedByLangValues() {
        try {
            return _jsonInteract.getEnterEthSeedByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getVerifyConversionAddressByLangValues() {
        try {
            return _jsonInteract.getVerifyConversionAddressByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getConversionAgreeByLangValues() {
        try {
            return _jsonInteract.getConversionAgreeByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getEthAddressByLangValues() {
        try {
            return _jsonInteract.getEthAddressByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getQuantumAddressByLangValues() {
        try {
            return _jsonInteract.getQuantumAddressByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getNetworkByLangValues() {
        try {
            return _jsonInteract.getNetworkByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getEnterQuantumWalletPasswordByLangValues() {
        try {
            return _jsonInteract.getEnterQuantumWalletPasswordByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getTypeTheWordsByLangValues() {
        try {
            return _jsonInteract.getTypeTheWordsByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getConversionMessageByLangValues() {
        try {
            return _jsonInteract.getConversionMessageByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getConversionRequestByLangValues() {
        try {
            return _jsonInteract.getConversionRequestByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getPleaseWaitSubmitByLangValues() {
        try {
            return _jsonInteract.getPleaseWaitSubmitByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getEnterEthKeyByLangValues() {
        try {
            return _jsonInteract.getEnterEthKeyByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getSelectEthKeyJsonByLangValues() {
        try {
            return _jsonInteract.getSelectEthKeyJsonByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getEnterPasswordEthKeyJsonByLangValues() {
        try {
            return _jsonInteract.getEnterPasswordEthKeyJsonByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getEnterEthPasswordByLangValues() {
        try {
            return _jsonInteract.getEnterEthPasswordByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getCopyEthAddressByLangValues() {
        try {
            return _jsonInteract.getCopyEthAddressByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getEnterEthAddressByLangValues() {
        try {
            return _jsonInteract.getEnterEthAddressByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getCopyMessageByLangValues() {
        try {
            return _jsonInteract.getCopyMessageByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getPasteSignatureInfoByLangValues() {
        try {
            return _jsonInteract.getPasteSignatureInfoByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getPasteSignatureByLangValues() {
        try {
            return _jsonInteract.getPasteSignatureByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getBalanceChangedByLangValues() {
        try {
            return _jsonInteract.getBalanceChangedByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getAddressToSendByLangValues() {
        try {
            return _jsonInteract.getAddressToSendByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getQuantityToSendByLangValues() {
        try {
            return _jsonInteract.getQuantityToSendByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getSendRequestByLangValues() {
        try {
            return _jsonInteract.getSendRequestByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getReceive_coinsByLangValues() {
        try {
            return _jsonInteract.getReceive_coinsByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getSendOnlyByLangValues() {
        try {
            return _jsonInteract.getSendOnlyByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getInoutByLangValues() {
        try {
            return _jsonInteract.getInoutByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getDateByLangValues() {
        try {
            return _jsonInteract.getDateByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getFromByLangValues() {
        try {
            return _jsonInteract.getFromByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getToByLangValues() {
        try {
            return _jsonInteract.getToByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getHashByLangValues() {
        try {
            return _jsonInteract.getHashByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getBlockByLangValues() {
        try {
            return _jsonInteract.getBlockByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getSendConfirmByLangValues() {
        try {
            return _jsonInteract.getSendConfirmByLangValues();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getErrorByErrors() {
        try {
            return _jsonInteract.getErrorByErrors();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getWrongAnswerByErrors() {
        try {
            return _jsonInteract.getWrongAnswerByErrors();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getSelectOptionByErrors() {
        try {
            return _jsonInteract.getSelectOptionByErrors();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getRetypePasswordMismatchByErrors() {
        try {
            return _jsonInteract.getRetypePasswordMismatchByErrors();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getPasswordSpecByErrors() {
        try {
            return _jsonInteract.getPasswordSpecByErrors();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getPasswordSpaceByErrors() {
        try {
            return _jsonInteract.getPasswordSpaceByErrors();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getSeedInitErrorByErrors() {
        try {
            return _jsonInteract.getSeedInitErrorByErrors();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getSeedEmptyByErrors() {
        try {
            return _jsonInteract.getSeedEmptyByErrors();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getSeedDoesNotExistByErrors() {
        try {
            return _jsonInteract.getSeedDoesNotExistByErrors();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getSeedMismatchByErrors() {
        try {
            return _jsonInteract.getSeedMismatchByErrors();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getWalletPasswordMismatchByErrors() {
        try {
            return _jsonInteract.getWalletPasswordMismatchByErrors();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getWordToSeedByErrors() {
        try {
            return _jsonInteract.getWordToSeedByErrors();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getSelectWalletFileByErrors() {
        try {
            return _jsonInteract.getSelectWalletFileByErrors();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getEnterWalletFilePasswordByErrors() {
        try {
            return _jsonInteract.getEnterWalletFilePasswordByErrors();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getWalletFileOpenErrorByErrors() {
        try {
            return _jsonInteract.getWalletFileOpenErrorByErrors();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getEnterWalletPassordByErrors() {
        try {
            return _jsonInteract.getEnterWalletPassordByErrors();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getWalletOpenErrorByErrors() {
        try {
            return _jsonInteract.getWalletOpenErrorByErrors();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getNoSeed() {
        try {
            return _jsonInteract.getNoSeed();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getWalletAddressExists() {
        try {
            return _jsonInteract.getWalletAddressExists();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getInvalidNetworkJson() {
        try {
            return _jsonInteract.getInvalidNetworkJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getInvalidApiResponse() {
        try {
            return _jsonInteract.getInvalidApiResponse();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getEthSeedEmpty() {
        try {
            return _jsonInteract.getEthSeedEmpty();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getEthSeedError() {
        try {
            return _jsonInteract.getEthSeedError();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getNoEthConversionWallets() {
        try {
            return _jsonInteract.getNoEthConversionWallets();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getNoEthConversionWallet() {
        try {
            return _jsonInteract.getNoEthConversionWallet();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getSelectEthAddress() {
        try {
            return _jsonInteract.getSelectEthAddress();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getEnterQuantumPassword() {
        try {
            return _jsonInteract.getEnterQuantumPassword();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getInvalidEthPrivateKey() {
        try {
            return _jsonInteract.getInvalidEthPrivateKey();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getEthSigMatch() {
        try {
            return _jsonInteract.getEthSigMatch();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getEnterEthSig() {
        try {
            return _jsonInteract.getEnterEthSig();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getEnterAmount() {
        try {
            return _jsonInteract.getEnterAmount();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getAmountLarge() {
        try {
            return _jsonInteract.getAmountLarge();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getEthAddr() {
        try {
            return _jsonInteract.getEthAddr();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getQuantumAddr() {
        try {
            return _jsonInteract.getQuantumAddr();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getNoMoreTxns() {
        try {
            return _jsonInteract.getNoMoreTxns();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getInternetDisconnected() {
        try {
            return _jsonInteract.getInternetDisconnected();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getUnexpectedError() {
        try {
            return _jsonInteract.getUnexpectedError();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
