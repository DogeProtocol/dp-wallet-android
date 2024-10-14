package com.dpwallet.app.viewmodel;

import android.content.Context;
import android.security.keystore.UserNotAuthenticatedException;

import com.dpwallet.app.entity.KeyServiceException;
import com.dpwallet.app.entity.ServiceException;
import com.dpwallet.app.hybrid.HybridPqcJNIImpl;
import com.dpwallet.app.hybrid.IHybridPqcJNIImpl;
import com.dpwallet.app.interact.KeyInteract;
import com.dpwallet.app.keystorage.IKeyStore;
import com.dpwallet.app.keystorage.KeyStore;
import com.dpwallet.app.services.IKeyService;
import com.dpwallet.app.services.KeyService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.lifecycle.ViewModel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//@HiltViewModel
public class KeyViewModel  extends ViewModel{

    private IHybridPqcJNIImpl iHybridPqcJNI = new HybridPqcJNIImpl();
    private IKeyService iKeyService = new KeyService(iHybridPqcJNI);
    private IKeyStore iKeyStore = new KeyStore();

    //@Inject
    private final KeyInteract _keyInteract;

    //@Inject
    public KeyViewModel() {
        _keyInteract = new KeyInteract(iKeyService, iKeyStore);
    }

    public String cryptoNewSeed() throws ServiceException {
        return _keyInteract.random();
    }

    public String cryptoExpandSeed(int[] seed) throws ServiceException {
        return _keyInteract.seedExpander(seed);
    }

    public String[] cryptoNewKeyPairFromSeed(int[] expandedSeedArray) throws ServiceException {
        return (String[]) newAccountFromSeed(expandedSeedArray);
    }

    public String cryptoSign(int[] message, int[] skKey) throws ServiceException {
        return (String) _keyInteract.signAccount(message, skKey);
    }

    public int cryptoVerify(int[] message, int[] sign, int[] pkKey) throws ServiceException {
        return _keyInteract.verifyAccount(message, sign, pkKey);
    }

    public int[] scrypt(int[] skKey, int[] salt) throws ServiceException {
        return _keyInteract.scrypt(skKey, salt);
    }

    public String publicKeyFromPrivateKey(int[] skKey) throws ServiceException {
        return _keyInteract.publicKeyFromPrivateKey(skKey);
    }

    public String getAccountAddress(int[] pkKey) throws ServiceException {
        return _keyInteract.getAccountAddress(pkKey);
    }

    public String isValidAddress(String quantumAddress) throws ServiceException {
        return _keyInteract.isValidAddress(quantumAddress);
    }


    public int[] getTxnSigningHash(String fromAddress, String nonce, String toAddress,
                                   String amount, String gasLimit, String data, String chainId) throws ServiceException {
        return _keyInteract.getTxnSigningHash(fromAddress, nonce, toAddress,
                amount, gasLimit,data, chainId);
    }

    public String getTxHash(String fromAddress, String nonce, String toAddress,
                            String amount, String gasLimit, String data, String chainId, int[] pkKey, int[] sig) throws ServiceException {
        return _keyInteract.getTxHash(fromAddress, nonce, toAddress,
                amount, gasLimit, data, chainId, pkKey, sig);
    }

    public String getTxData(String fromAddress, String nonce, String toAddress,
                            String amount, String gasLimit, String data, String chainId, int[] pkKey, int[] sig) throws ServiceException {
        return _keyInteract.getTxData(fromAddress, nonce, toAddress,
                amount, gasLimit, data, chainId, pkKey, sig);
    }

    public String getContractData(String method, String abiData, String argument1, String argument2) throws ServiceException {
        return _keyInteract.getContractData(method, abiData, argument1, argument2);
    }

    public String getParseBigFloat(String value) throws ServiceException {
        return _keyInteract.getParseBigFloat(value);
    }

    public String getParseBigFloatInner(String value) throws ServiceException {
        return _keyInteract.getParseBigFloatInner(value);
    }

    public String getDogeProtocolToWei(String value) throws ServiceException {
        return _keyInteract.getDogeProtocolToWei(value);
    }

    public String getWeiToDogeProtocol(String value) throws ServiceException {
        return _keyInteract.getWeiToDogeProtocol(value);
    }

    public boolean encryptDataByString(Context context, String key, String password, String passwordSHA256) {
        return _keyInteract.encryptDataByAccount(context, key, password, passwordSHA256);
    }


    public String decryptDataByString(Context context, String key, String password) throws InvalidKeyException, KeyServiceException {
        byte[] byteArray = _keyInteract.decryptDataByAccount(context, key, password);
        String str = new String(byteArray); // for UTF-8 encoding
        return str;
    }

    public boolean encryptDataByAccount(Context context, String key, String password,
                                        String[] keyPair) {
       /* ByteBuffer sk_key_byteBuffer = ByteBuffer.allocate(SK_KEY.length * 4);
        IntBuffer sk_key_intBuffer = sk_key_byteBuffer.asIntBuffer();
        sk_key_intBuffer.put(SK_KEY);

        ByteBuffer pk_key_byteBuffer = ByteBuffer.allocate(PK_KEY.length * 4);
        IntBuffer pk_key_intBuffer = pk_key_byteBuffer.asIntBuffer();
        pk_key_intBuffer.put(PK_KEY);

        byte[] sk_key = sk_key_byteBuffer.array();
        byte[] pk_key = pk_key_byteBuffer.array();
        */

        Gson gson = new Gson();
        List<String> textList = new ArrayList<String>(Arrays.asList(keyPair));
        String jsonText = gson.toJson(textList);
        return _keyInteract.encryptDataByAccount(context, key, password, jsonText);
    }

    public String[] decryptDataByAccount(Context context, String key, String password) throws InvalidKeyException, KeyServiceException {
        byte[] byteArray = _keyInteract.decryptDataByAccount(context, key, password);
        String jsonString = new String(byteArray);
        List<String> dataList = Arrays.asList(new GsonBuilder().create().fromJson(jsonString, String[].class));
        String[] data = new String[dataList.size()];
        data = dataList.toArray(data);
        return data;

        /*
        IntBuffer intBuf =
                ByteBuffer.wrap(byteArray)
                        .order(ByteOrder.BIG_ENDIAN)
                        .asIntBuffer();

        String[] array = new String[intBuf.remaining()];
        intBuf.get(array);
        return array;
         */
    }

    public String[] newAccountFromSeed(int[] expandedSeedArray) throws ServiceException {
        return _keyInteract.newAccountFromSeed(expandedSeedArray);
    }

    public String[] newAccount() throws ServiceException {
        return _keyInteract.newAccount();
    }

    public String signAccount(int[] message, int[] skKey) throws ServiceException {
        return _keyInteract.signAccount(message, skKey);
    }

    public int verifyAccount(int[] message, int[] sign, int[] pkKey) throws ServiceException {
        return _keyInteract.verifyAccount(message, sign, pkKey);
    }

//    public int[] seedExpander(int[] seed) throws ServiceException {
//        return _keyInteract.seedExpander(seed);
//    }
//    public int[] random() throws ServiceException {
//        return _keyInteract.random();
//    }
}

