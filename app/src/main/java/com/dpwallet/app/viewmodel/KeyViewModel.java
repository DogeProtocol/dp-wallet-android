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

import androidx.lifecycle.ViewModel;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.security.InvalidKeyException;

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

    //@Inject
    //public KeyViewModel(KeyInteract keyInteract) {
    //    this._keyInteract = keyInteract;
    //}

    //@Inject
    //public KeyViewModel() {

    //}

    public int[] newAccount() throws ServiceException {
            return _keyInteract.newAccount();
    }

    public int[] signAccount(int[] message, int[] skKey) throws ServiceException {
        return _keyInteract.signAccount(message, skKey);
    }

    public int verifyAccount(int[] message, int[] sign, int[] pkKey) throws ServiceException {
        return _keyInteract.verifyAccount(message, sign, pkKey);
    }

    public int[] publicKeyFromPrivateKey(int[] skKey) throws ServiceException {
        return _keyInteract.publicKeyFromPrivateKey(skKey);
    }

    public String getAccountAddress(int[] pkKey) throws ServiceException {
        return _keyInteract.getAccountAddress(pkKey);
    }

    public int[] getTxnSigningHash(String fromAddress, String nonce, String toAddress,
                              String amount, String gasLimit,  String chainId) throws ServiceException {
        return _keyInteract.getTxnSigningHash(fromAddress, nonce, toAddress,
                amount, gasLimit, chainId);
    }

    public String getTxHash(String fromAddress, String nonce, String toAddress,
                            String amount, String gasLimit, String chainId, int[] pkKey, int[] sig) throws ServiceException {
        return _keyInteract.getTxHash(fromAddress, nonce, toAddress,
                amount, gasLimit,  chainId, pkKey, sig);
    }

    public String getTxData(String fromAddress, String nonce, String toAddress,
                            String amount, String gasLimit, String chainId, int[] pkKey, int[] sig) throws ServiceException {
        return _keyInteract.getTxData(fromAddress, nonce, toAddress,
                amount, gasLimit, chainId, pkKey, sig);
    }

    public String getDogeProtocolToWei(String value) throws ServiceException {
        return _keyInteract.getDogeProtocolToWei(value);
    }

    public String getParseBigFloat(String value) throws ServiceException {
        return _keyInteract.getParseBigFloat(value);
    }

    public String getWeiToDogeProtocol(String value) throws ServiceException {
        return _keyInteract.getWeiToDogeProtocol(value);
    }

    public boolean encryptDataByAccount(Context context, String address, String password,
                                        int[] SK_KEY, int[] PK_KEY) {
        ByteBuffer sk_key_byteBuffer = ByteBuffer.allocate(SK_KEY.length * 4);
        IntBuffer sk_key_intBuffer = sk_key_byteBuffer.asIntBuffer();
        sk_key_intBuffer.put(SK_KEY);

        ByteBuffer pk_key_byteBuffer = ByteBuffer.allocate(PK_KEY.length * 4);
        IntBuffer pk_key_intBuffer = pk_key_byteBuffer.asIntBuffer();
        pk_key_intBuffer.put(PK_KEY);

        byte[] sk_key = sk_key_byteBuffer.array();
        byte[] pk_key = pk_key_byteBuffer.array();
        return _keyInteract.encryptDataByAccount(context, address, password, sk_key, pk_key);
    }

    public int[] decryptDataByAccount(Context context, String address, String password) throws InvalidKeyException, KeyServiceException {
        byte[] byteArray = _keyInteract.decryptDataByAccount(context, address, password);
        IntBuffer intBuf =
                ByteBuffer.wrap(byteArray)
                        .order(ByteOrder.BIG_ENDIAN)
                        .asIntBuffer();
        int[] array = new int[intBuf.remaining()];
        intBuf.get(array);
        return array;
    }

    public String exportKeyByAccount(Context context, String address) {
        return _keyInteract.exportKeyByAccount(context, address);
    }

    public int[] importKeyByAccount(Context context, String jsonString, String password)  {
        byte[] byteArray = _keyInteract.importKeyByAccount(context, jsonString, password);
        IntBuffer intBuf =
                ByteBuffer.wrap(byteArray)
                        .order(ByteOrder.BIG_ENDIAN)
                        .asIntBuffer();
        int[] array = new int[intBuf.remaining()];
        intBuf.get(array);
        return array;
    }

    public void deleteKeyByAccount(Context context, String address) {
      _keyInteract.deleteKeyByAccount(context, address);
    }

}
