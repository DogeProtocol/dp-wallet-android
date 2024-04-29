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

    private static final String data_lang_key_info = "info";
    private static final String data_lang_key_infoStep = "infoStep";

    private static final String data_lang_key_quizStep = "quizStep";
    private static final String data_lang_key_quizWrongAnswer = "quizWrongAnswer";
    private static final String data_lang_key_quizNoChoice = "quizNoChoice";
    private static final String data_lang_key_quiz = "quiz";

    private static final String data_lang_key_title = "title";
    private static final String data_lang_key_desc = "desc";
    private static final String data_lang_key_question = "question";
    private static final String data_lang_key_choices = "choices";
    private static final String data_lang_key_correctChoice = "correctChoice";
    private static final String data_lang_key_afterQuizInfo = "afterQuizInfo";

    private static final String data_lang_key_langValues = "langValues";
    private static final String data_lang_key_next = "next";
    private static final String data_lang_key_ok = "ok";
    private static final String data_lang_key_cancel = "cancel";
    private static final String data_lang_key_close = "close";
    private static final String data_lang_key_submit = "submit";
    private static final String data_lang_key_send = "send";
    private static final String data_lang_key_receive = "receive";
    private static final String data_lang_key_transactions = "transactions";
    private static final String data_lang_key_copy = "copy";
    private static final String data_lang_key_back = "back";
    private static final String data_lang_key_balance = "balance";
    private static final String data_lang_key_refresh = "refresh";
    private static final String data_lang_key_completed_transactions = "completed-transactions";
    private static final String data_lang_key_pending_transactions = "pending-transactions";
    private static final String data_lang_key_backup = "backup";
    private static final String data_lang_key_restore = "restore";
    private static final String data_lang_key_wallets = "wallets";
    private static final String data_lang_key_settings = "settings";
    private static final String data_lang_key_unlock = "unlock";
    private static final String data_lang_key_unlock_wallet = "unlock-wallet";
    private static final String data_lang_key_scan = "scan";
    private static final String data_lang_key_block_explorer = "block-explorer";
    private static final String data_lang_key_select_network = "select-network";
    private static final String data_lang_key_enter_wallet_password = "enter-wallet-password";
    private static final String data_lang_key_enter_a_password = "enter-a-password";
    private static final String data_lang_key_show_password = "show-password";
    private static final String data_lang_key_password = "password";
    private static final String data_lang_key_set_wallet_passowrd = "set-wallet-passowrd";
    //private static final String data_lang_key_quiz = "quiz";
    private static final String data_lang_key_get_coins_for_dogep_tokens = "get-coins-for-dogep-tokens";
    private static final String data_lang_key_wallet_path = "wallet-path";
    private static final String data_lang_key_set_wallet_password = "set-wallet-password";
    private static final String data_lang_key_use_strong_password = "use-strong-password";
    private static final String data_lang_key_retype_password = "retype-password";
    private static final String data_lang_key_retype_the_password = "retype-the-password";
    private static final String data_lang_key_create_restore_wallet = "create-restore-wallet";

    private static final String data_lang_key_select_an_option = "select-an-option";
    private static final String data_lang_key_create_new_wallet = "create-new-wallet";
    private static final String data_lang_key_restore_wallet_from_seed = "restore-wallet-from-seed";
    private static final String data_lang_key_restore_wallet_from_backup_file = "restore-wallet-from-backup-file";
    private static final String data_lang_key_seed_words = "seed-words";
    private static final String data_lang_key_seed_words_info_1 = "seed-words-info-1";
    private static final String data_lang_key_seed_words_info_2 = "seed-words-info-2";
    private static final String data_lang_key_seed_words_info_3 = "seed-words-info-3";
    private static final String data_lang_key_seed_words_info_4 = "seed-words-info-4";
    private static final String data_lang_key_seed_words_show = "seed-words-show";
    private static final String data_lang_key_verify_seed_words = "verify-seed-words";
    private static final String data_lang_key_verify_wallet_password = "verify-wallet-password";
    private static final String data_lang_key_verify_wallet_password_info = "verify-wallet-password-info";
    private static final String data_lang_key_waitWalletSave = "waitWalletSave";
    private static final String data_lang_key_walletSaved = "walletSaved";
    private static final String data_lang_key_backup_wallet = "backup-wallet";
    private static final String data_lang_key_backup_wallet_info_1 = "backup-wallet-info-1";
    private static final String data_lang_key_backup_wallet_info_2 = "backup-wallet-info-2";
    private static final String data_lang_key_backup_wallet_skip = "backup-wallet-skip";
    private static final String data_lang_key_backupWait = "backupWait";
    private static final String data_lang_key_walletBackedUp = "walletBackedUp";
    private static final String data_lang_key_restore_wallet_from_backup = "restore-wallet-from-backup";
    private static final String data_lang_key_enter_above_wallet_password = "enter-above-wallet-password";
    private static final String data_lang_key_walletFileRestoreWait = "walletFileRestoreWait";
    private static final String data_lang_key_waitRevealSeed = "waitRevealSeed";
    private static final String data_lang_key_waitWalletOpen = "waitWalletOpen";
    private static final String data_lang_key_open = "open";
    private static final String data_lang_key_total_balance = "total-balance";
    private static final String data_lang_key_help = "help";
    private static final String data_lang_key_dpscan = "dpscan";
    private static final String data_lang_key_address = "address";
    private static final String data_lang_key_coins = "coins";
    private static final String data_lang_key_reveal_seed = "reveal-seed";
    private static final String data_lang_key_create_or_restore_wallet = "create-or-restore-wallet";
    private static final String data_lang_key_reveal = "reveal";
    private static final String data_lang_key_waitUnlock = "waitUnlock";
    private static final String data_lang_key_networks = "networks";
    private static final String data_lang_key_id = "id";
    private static final String data_lang_key_name = "name";
    private static final String data_lang_key_scan_api_url = "scan-api-url";
    private static final String data_lang_key_txn_api_url = "txn-api-url";
    private static final String data_lang_key_block_explorer_url = "block-explorer-url";
    private static final String data_lang_key_add_network = "add-network";
    private static final String data_lang_key_add = "add";
    private static final String data_lang_key_enter_network_json = "enter-network-json";
    private static final String data_lang_key_addNetworkWarn = "addNetworkWarn";
    private static final String data_lang_key_networkAdded = "networkAdded";
    private static final String data_lang_key_get_coins_for_tokens = "get-coins-for-tokens";
    private static final String data_lang_key_get_coins_for_tokens_info = "get-coins-for-tokens-info";
    private static final String data_lang_key_choose_eth_wallet_option = "choose-eth-wallet-option";
    private static final String data_lang_key_eth_option_seed = "eth-option-seed";
    private static final String data_lang_key_eth_option_private_key = "eth-option-private-key";
    private static final String data_lang_key_eth_option_keystore = "eth-option-keystore";
    private static final String data_lang_key_eth_option_manual = "eth-option-manual";
    private static final String data_lang_key_enter_eth_seed = "enter-eth-seed";
    private static final String data_lang_key_verify_conversion_address = "verify-conversion-address";
    private static final String data_lang_key_conversionAgree = "conversionAgree";
    private static final String data_lang_key_eth_address = "eth-address";
    private static final String data_lang_key_quantum_address = "quantum-address";
    private static final String data_lang_key_network = "network";
    private static final String data_lang_key_enter_quantum_wallet_password = "enter-quantum-wallet-password";
    private static final String data_lang_key_type_the_words = "type-the-words";
    private static final String data_lang_key_conversionMessage = "conversionMessage";
    private static final String data_lang_key_conversionRequest = "conversionRequest";
    private static final String data_lang_key_pleaseWaitSubmit = "pleaseWaitSubmit";
    private static final String data_lang_key_enter_eth_key = "enter-eth-key";
    private static final String data_lang_key_select_eth_key_json = "select-eth-key-json";
    private static final String data_lang_key_enter_password_eth_key_json = "enter-password-eth-key-json";
    private static final String data_lang_key_enter_eth_password = "enter-eth-password";
    private static final String data_lang_key_copy_eth_address = "copy-eth-address";
    private static final String data_lang_key_enter_eth_address = "enter-eth-address";
    private static final String data_lang_key_copy_message = "copy-message";
    private static final String data_lang_key_paste_signature_info = "paste-signature-info";
    private static final String data_lang_key_paste_signature = "paste-signature";
    private static final String data_lang_key_balanceChanged = "balanceChanged";
    private static final String data_lang_key_address_to_send = "address-to-send";
    private static final String data_lang_key_quantity_to_send = "quantity-to-send";
    private static final String data_lang_key_sendRequest = "sendRequest";
    private static final String data_lang_key_receive_coins = "receive-coins";
    private static final String data_lang_key_send_only = "send-only";
    private static final String data_lang_key_inout = "inout";
    private static final String data_lang_key_date = "date";
    private static final String data_lang_key_from = "from";
    private static final String data_lang_key_to = "to";
    private static final String data_lang_key_hash = "hash";
    private static final String data_lang_key_block = "block";
    private static final String data_lang_key_sendConfirm = "sendConfirm";


    private static final String data_lang_key_errors = "errors";
    private static final String data_lang_key_error = "error";
    private static final String data_lang_key_wrongAnswer = "wrongAnswer";
    private static final String data_lang_key_selectOption = "selectOption";
    private static final String data_lang_key_retypePasswordMismatch = "retypePasswordMismatch";
    private static final String data_lang_key_passwordSpec = "passwordSpec";
    private static final String data_lang_key_passwordSpace = "passwordSpace";
    private static final String data_lang_key_seedInitError = "seedInitError";
    private static final String data_lang_key_seedEmpty = "seedEmpty";
    private static final String data_lang_key_seedDoesNotExist = "seedDoesNotExist";
    private static final String data_lang_key_seedMismatch = "seedMismatch";
    private static final String data_lang_key_walletPasswordMismatch = "walletPasswordMismatch";
    private static final String data_lang_key_wordToSeed = "wordToSeed";
    private static final String data_lang_key_selectWalletFile = "selectWalletFile";
    private static final String data_lang_key_enterWalletFilePassword = "enterWalletFilePassword";
    private static final String data_lang_key_walletFileOpenError = "walletFileOpenError";
    private static final String data_lang_key_enterWalletPassord = "enterWalletPassord";
    private static final String data_lang_key_walletOpenError = "walletOpenError";
    private static final String data_lang_key_noSeed = "noSeed";
    private static final String data_lang_key_walletAddressExists = "walletAddressExists";
    private static final String data_lang_key_invalidNetworkJson = "invalidNetworkJson";
    private static final String data_lang_key_invalidApiResponse = "invalidApiResponse";
    private static final String data_lang_key_ethSeedEmpty = "ethSeedEmpty";
    private static final String data_lang_key_ethSeedError = "ethSeedError";
    private static final String data_lang_key_noEthConversionWallets = "noEthConversionWallets";
    private static final String data_lang_key_noEthConversionWallet = "noEthConversionWallet";
    private static final String data_lang_key_selectEthAddress = "selectEthAddress";
    private static final String data_lang_key_enterQuantumPassword = "enterQuantumPassword";
    private static final String data_lang_key_invalidEthPrivateKey = "invalidEthPrivateKey";
    private static final String data_lang_key_ethSigMatch = "ethSigMatch";
    private static final String data_lang_key_enterEthSig = "enterEthSig";
    private static final String data_lang_key_enterAmount = "enterAmount";
    private static final String data_lang_key_amountLarge = "amountLarge";
    private static final String data_lang_key_ethAddr = "ethAddr";
    private static final String data_lang_key_quantumAddr = "quantumAddr";
    private static final String data_lang_key_noMoreTxns = "noMoreTxns";
    private static final String data_lang_key_internetDisconnected = "internetDisconnected";
    private static final String data_lang_key_unexpectedError = "unexpectedError";



    public JsonInteract(String jsonString) throws JSONException {
        jsonObject  = new JSONObject(jsonString);
    }
    public String getInfoStep() throws JSONException{
        return jsonObject.getString(data_lang_key_infoStep);
    }

    public int getInfoLength() throws JSONException{
        return getInfo().length();
    }

    public String getTitleByInfo(int index) throws JSONException{
        JSONObject object  = getInfo().getJSONObject(index);
        return object.getString(data_lang_key_title);
    }

    public String getDescByInfo(int index) throws JSONException{
        JSONObject object  = getInfo().getJSONObject(index);
        return object.getString(data_lang_key_desc);
    }


    public String getQuizStep() throws JSONException{
        return jsonObject.getString(data_lang_key_quizStep);
    }

    public String getQuizWrongAnswer() throws JSONException{
        return jsonObject.getString(data_lang_key_quizWrongAnswer);
    }
    public String getQuizNoChoice() throws JSONException{
        return jsonObject.getString(data_lang_key_quizNoChoice);
    }

    public int getQuizLength() throws JSONException{
        return getQuiz().length();
    }

    public String getTitleByQuiz(int index) throws JSONException{
        JSONObject object  = getQuiz().getJSONObject(index);
        return object.getString(data_lang_key_title);
    }

    public String getQuestionByQuiz(int index) throws JSONException{
        JSONObject object  = getQuiz().getJSONObject(index);
        return object.getString(data_lang_key_question);
    }

    public ArrayList<String> getChoicesByQuiz(int index) throws JSONException{
        JSONObject object  = getQuiz().getJSONObject(index);
        JSONArray jsonArray = object.getJSONArray(data_lang_key_choices);

        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(jsonArray.getString(0));
        arrayList.add(jsonArray.getString(1));
        arrayList.add(jsonArray.getString(2));
        arrayList.add(jsonArray.getString(3));

        return arrayList;
    }

    public int getCorrectChoiceByQuiz(int index) throws JSONException{
        JSONObject object = getQuiz().getJSONObject(index);
        return object.getInt(data_lang_key_correctChoice);
    }

    public String getAfterQuizInfoByQuiz(int index) throws JSONException{
        JSONObject object = getQuiz().getJSONObject(index);
        return object.getString(data_lang_key_afterQuizInfo);
    }



    public String getTitleByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_title);
    }
    public String getNextByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_next);
    }
    public String getOkByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_ok);
    }
    public String getCancelByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_cancel);
    }
    public String getCloseByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_close);
    }
    public String getSubmitByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_submit);
    }
    public String getSendByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_send);
    }
    public String getReceiveByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_receive);
    }
    public String getTransactionsByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_transactions);
    }
    public String getCopyByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_copy);
    }
    public String getBackByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_back);
    }
    public String getBalanceByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_balance);
    }
    public String getRefreshByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_refresh);
    }
    public String getCompletedTransactionsByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_completed_transactions);
    }
    public String getPendingTransactionsByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_pending_transactions);
    }
    public String getBackupByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_backup);
    }
    public String getRestoreByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_restore);
    }
    public String getWalletsByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_wallets);
    }
    public String getSettingsByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_settings);
    }
    public String getUnlockByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_unlock);
    }
    public String getUnlockWalletByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_unlock_wallet);
    }

    public String getScanWalletByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_scan);
    }
    public String getBlockExplorerByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_block_explorer);
    }
    public String getSelectNetworkByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_select_network);
    }
    public String getEnterWalletPasswordWalletByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_enter_wallet_password);
    }
    public String getEnterApasswordByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_enter_a_password);
    }
    public String getShowPasswordByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_show_password);
    }
    public String getPasswordByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_password);
    }
    public String getSetWalletPassowrdByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_set_wallet_passowrd);
    }
    public String getQuizByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_quiz);
    }
    public String getGetCoinsForDogePTokensByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_get_coins_for_dogep_tokens);
    }

    public String getWalletPathByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_wallet_path);
    }
    public String getSetWalletPasswordByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_set_wallet_passowrd);
    }
    public String getUseStrongPasswordByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_use_strong_password);
    }
    public String getRetypePasswordByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_retype_password);
    }
    public String getRetypeThePasswordByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_retype_the_password);
    }
    public String getCreateRestoreWalletByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_create_restore_wallet);
    }
    public String getSelectAnOptionByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_select_an_option);
    }
    public String getCreateNewWalletByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_create_new_wallet);
    }
    public String getRestoreWalletFromSeedByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_restore_wallet_from_seed);
    }
    public String getRestoreWalletFromBackupFileByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_restore_wallet_from_backup_file);
    }
    public String getSeedWordsByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_seed_words);
    }
    public String getSeedWordsInfo1ByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_seed_words_info_1);
    }
    public String getSeedWordsInfo2ByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_seed_words_info_2);
    }
    public String getSeedWordsInfo3ByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_seed_words_info_3);
    }
    public String getSeedWordsInfo4ByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_seed_words_info_4);
    }
    public String getSeedWordsShowByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_seed_words_show);
    }
    public String getVerifySeedWordsByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_verify_seed_words);
    }
    public String getVerifyWalletPasswordByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_verify_wallet_password);
    }
    public String getVerifyWalletPasswordInfoByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_verify_wallet_password_info);
    }
    public String getWaitWalletSaveByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_waitWalletSave);
    }
    public String getWalletSavedByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_walletSaved);
    }
    public String getBackupWalletByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_backup_wallet);
    }
    public String getBackupWalletInfo1ByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_backup_wallet_info_1);
    }
    public String getBackupWalletInfo2ByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_backup_wallet_info_2);
    }
    public String getBackupWalletSkipByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_backup_wallet_skip);
    }
    public String getBackupWaitByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_backupWait);
    }
    public String getWalletBackedUpByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_walletBackedUp);
    }
    public String getRestoreWalletFromBackupByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_restore_wallet_from_backup);
    }
    public String getEnterAboveWalletPasswordByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_enter_above_wallet_password);
    }
    public String getWalletFileRestoreWaitByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_walletFileRestoreWait);
    }
    public String getWaitRevealSeedByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_waitRevealSeed);
    }
    public String getWaitWalletOpenByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_waitWalletOpen);
    }


    public String getOpenByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_open);
    }
    public String getTotal_balanceByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_total_balance);
    }
    public String getHelpByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_help);
    }
    public String getDpscanByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_dpscan);
    }
    public String getAddressByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_address);
    }
    public String getCoinsByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_coins);
    }
    public String getRevealSeedByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_reveal_seed);
    }
    public String getCreateOrRestoreWalletByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_create_or_restore_wallet);
    }
    public String getRevealByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_reveal);
    }
    public String getWaitUnlockByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_waitUnlock);
    }
    public String getNetworksByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_networks);
    }
    public String getIdByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_id);
    }
    public String getNameByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_name);
    }
    public String getScanApiUrlByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_scan_api_url);
    }
    public String getTxnApiUrlByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_txn_api_url);
    }
    public String getBlockExplorerUrlByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_block_explorer_url);
    }
    public String getAddNetworkByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_add_network);
    }
    public String getAddByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_add);
    }
    public String getEnterNetwork_jsonByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_enter_network_json);
    }
    public String getAddNetworkWarnByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_addNetworkWarn);
    }
    public String getNetworkAddedByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_networkAdded);
    }
    public String getGetCoinsForTokensByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_get_coins_for_tokens);
    }
    public String getGetCoinsForTokensInfoByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_get_coins_for_tokens_info);
    }
    public String getChooseEthWalletOptionByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_choose_eth_wallet_option);
    }
    public String getEthOptionSeedByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_eth_option_seed);
    }
    public String getEthOptionPrivateKeyByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_eth_option_private_key);
    }
    public String getEthOptionKeystoreByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_eth_option_keystore);
    }
    public String getEthOptionManualByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_eth_option_manual);
    }
    public String getEnterEthSeedByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_enter_eth_seed);
    }
    public String getVerifyConversionAddressByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_verify_conversion_address);
    }
    public String getConversionAgreeByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_conversionAgree);
    }
    public String getEthAddressByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_eth_address);
    }
    public String getQuantumAddressByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_quantum_address);
    }
    public String getNetworkByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_network);
    }
    public String getEnterQuantumWalletPasswordByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_enter_quantum_wallet_password);
    }
    public String getTypeTheWordsByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_type_the_words);
    }
    public String getConversionMessageByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_conversionMessage);
    }
    public String getConversionRequestByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_conversionRequest);
    }
    public String getPleaseWaitSubmitByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_pleaseWaitSubmit);
    }
    public String getEnterEthKeyByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_enter_eth_key);
    }
    public String getSelectEthKeyJsonByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_select_eth_key_json);
    }
    public String getEnterPasswordEthKeyJsonByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_enter_password_eth_key_json);
    }
    public String getEnterEthPasswordByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_enter_eth_password);
    }
    public String getCopyEthAddressByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_copy_eth_address);
    }
    public String getEnterEthAddressByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_enter_eth_address);
    }
    public String getCopyMessageByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_copy_message);
    }
    public String getPasteSignatureInfoByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_paste_signature_info);
    }
    public String getPasteSignatureByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_paste_signature);
    }
    public String getBalanceChangedByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_balanceChanged);
    }
    public String getAddressToSendByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_address_to_send);
    }
    public String getQuantityToSendByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_quantity_to_send);
    }
    public String getSendRequestByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_sendRequest);
    }
    public String getReceive_coinsByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_receive_coins);
    }
    public String getSendOnlyByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_send_only);
    }
    public String getInoutByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_inout);
    }
    public String getDateByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_date);
    }
    public String getFromByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_from);
    }
    public String getToByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_to);
    }
    public String getHashByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_hash);
    }
    public String getBlockByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_block);
    }
    public String getSendConfirmByLangValues() throws JSONException{
        return getLangValues().getString(data_lang_key_sendConfirm);
    }

    public String getErrorByErrors() throws JSONException{
        return getErrors().getString(data_lang_key_error);
    }
    public String getWrongAnswerByErrors() throws JSONException{
        return getErrors().getString(data_lang_key_wrongAnswer);
    }
    public String getSelectOptionByErrors() throws JSONException{
        return getErrors().getString(data_lang_key_selectOption);
    }
    public String getRetypePasswordMismatchByErrors() throws JSONException{
        return getErrors().getString(data_lang_key_retypePasswordMismatch);
    }
    public String getPasswordSpecByErrors() throws JSONException{
        return getErrors().getString(data_lang_key_passwordSpec);
    }
    public String getPasswordSpaceByErrors() throws JSONException{
        return getErrors().getString(data_lang_key_passwordSpace);
    }
    public String getSeedInitErrorByErrors() throws JSONException{
        return getErrors().getString(data_lang_key_seedInitError);
    }
    public String getSeedEmptyByErrors() throws JSONException{
        return getErrors().getString(data_lang_key_seedEmpty);
    }
    public String getSeedDoesNotExistByErrors() throws JSONException{
        return getErrors().getString(data_lang_key_seedDoesNotExist);
    }
    public String getSeedMismatchByErrors() throws JSONException{
        return getErrors().getString(data_lang_key_seedMismatch);
    }
    public String getWalletPasswordMismatchByErrors() throws JSONException{
        return getErrors().getString(data_lang_key_walletPasswordMismatch);
    }
    public String getWordToSeedByErrors() throws JSONException{
        return getErrors().getString(data_lang_key_wordToSeed);
    }
    public String getSelectWalletFileByErrors() throws JSONException{
        return getErrors().getString(data_lang_key_selectWalletFile);
    }
    public String getEnterWalletFilePasswordByErrors() throws JSONException{
        return getErrors().getString(data_lang_key_enterWalletFilePassword);
    }
    public String getWalletFileOpenErrorByErrors() throws JSONException{
        return getErrors().getString(data_lang_key_walletFileOpenError);
    }
    public String getEnterWalletPassordByErrors() throws JSONException{
        return getErrors().getString(data_lang_key_enterWalletPassord);
    }
    public String getWalletOpenErrorByErrors() throws JSONException{
        return getErrors().getString(data_lang_key_walletOpenError);
    }
    public String getNoSeed() throws JSONException{
        return getLangValues().getString(data_lang_key_noSeed );
    }
    public String getWalletAddressExists() throws JSONException{
        return getLangValues().getString(data_lang_key_walletAddressExists );
    }
    public String getInvalidNetworkJson() throws JSONException{
        return getLangValues().getString(data_lang_key_invalidNetworkJson );
    }
    public String getInvalidApiResponse() throws JSONException{
        return getLangValues().getString(data_lang_key_invalidApiResponse );
    }
    public String getEthSeedEmpty() throws JSONException{
        return getLangValues().getString(data_lang_key_ethSeedEmpty );
    }
    public String getEthSeedError() throws JSONException{
        return getLangValues().getString(data_lang_key_ethSeedError );
    }
    public String getNoEthConversionWallets() throws JSONException{
        return getLangValues().getString(data_lang_key_noEthConversionWallets );
    }
    public String getNoEthConversionWallet() throws JSONException{
        return getLangValues().getString(data_lang_key_noEthConversionWallet );
    }
    public String getSelectEthAddress() throws JSONException{
        return getLangValues().getString(data_lang_key_selectEthAddress );
    }
    public String getEnterQuantumPassword() throws JSONException{
        return getLangValues().getString(data_lang_key_enterQuantumPassword );
    }
    public String getInvalidEthPrivateKey() throws JSONException{
        return getLangValues().getString(data_lang_key_invalidEthPrivateKey );
    }
    public String getEthSigMatch() throws JSONException{
        return getLangValues().getString(data_lang_key_ethSigMatch );
    }
    public String getEnterEthSig() throws JSONException{
        return getLangValues().getString(data_lang_key_enterEthSig );
    }
    public String getEnterAmount() throws JSONException{
        return getLangValues().getString(data_lang_key_enterAmount );
    }
    public String getAmountLarge() throws JSONException{
        return getLangValues().getString(data_lang_key_amountLarge );
    }
    public String getEthAddr() throws JSONException{
        return getLangValues().getString(data_lang_key_ethAddr );
    }
    public String getQuantumAddr() throws JSONException{
        return getLangValues().getString(data_lang_key_quantumAddr );
    }
    public String getNoMoreTxns() throws JSONException{
        return getLangValues().getString(data_lang_key_noMoreTxns );
    }
    public String getInternetDisconnected() throws JSONException{
        return getLangValues().getString(data_lang_key_internetDisconnected );
    }
    public String getUnexpectedError() throws JSONException{
        return getLangValues().getString(data_lang_key_unexpectedError );
    }



    private JSONArray getInfo() throws JSONException{
        return jsonObject.getJSONArray(data_lang_key_info);
    }
    private JSONArray getQuiz() throws JSONException{
        return jsonObject.getJSONArray(data_lang_key_quiz);
    }
    private JSONObject getLangValues() throws JSONException{
        return jsonObject.getJSONObject(data_lang_key_langValues);
    }
    private JSONObject getErrors() throws JSONException{
        return jsonObject.getJSONObject(data_lang_key_errors);
    }

}
