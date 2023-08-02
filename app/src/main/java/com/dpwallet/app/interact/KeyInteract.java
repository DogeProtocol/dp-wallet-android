package com.dpwallet.app.interact;

import android.content.Context;
import android.security.keystore.UserNotAuthenticatedException;

import com.dpwallet.app.entity.KeyServiceException;
import com.dpwallet.app.entity.ServiceException;
import com.dpwallet.app.entity.Result;
import com.dpwallet.app.keystorage.IKeyStore;
import com.dpwallet.app.services.IKeyService;

import java.security.InvalidKeyException;

import timber.log.Timber;

public class KeyInteract {

    private final IKeyService _iKeyService ;
    private final IKeyStore _iKeyStore;

    public KeyInteract(IKeyService iKeyService, IKeyStore iKeyStore) {
        this._iKeyService = iKeyService;
        this._iKeyStore = iKeyStore;
    }

    public int[] newAccount() throws ServiceException {
        try {
            Result<Object> result = _iKeyService.newAccount();
            if (result.getException() != null) {
                Timber.tag("newAccount").d("error %s", result.getException());
                throw new ServiceException((String) result.getException());
            }
            Timber.tag("newAccount").d("success");
            return (int[]) result.getResult();
        }
        catch(Exception ex) {
            Timber.tag("newAccount catch").d("error %s", ex.getMessage());
            throw new ServiceException((String) ex.getMessage());
        }
    }

    public int[] signAccount(int[] message, int[] skKey) throws ServiceException {
        try {
            Result<Object> result = _iKeyService.signAccount(message, skKey);
            if (result.getException() != null) {
                Timber.tag("signAccount").d("error %s", result.getException());
                throw new ServiceException((String) result.getException());
            }
            Timber.tag("signAccount").d("success");
            return (int[]) result.getResult();
        }
        catch(Exception ex) {
            Timber.tag("signAccount catch").d("error %s", ex.getMessage());
            throw new ServiceException((String) ex.getMessage());
        }
    }

    public int verifyAccount(int[] message, int[] sign, int[] pkKey) throws ServiceException {
        try {
            Result<Object> result = _iKeyService.verifyAccount(message, sign, pkKey);
            if (result.getException() != null) {
                Timber.tag("verifyAccount").d("error %s", result.getException());
                throw new ServiceException((String) result.getException());
            }
            Timber.tag("verifyAccount").d("success");
            return (int) result.getResult();
        }
        catch(Exception ex) {
            Timber.tag("verifyAccount catch").d("error %s", ex.getMessage());
            throw new ServiceException((String) ex.getMessage());
        }
    }

    public int[] publicKeyFromPrivateKey(int[] skKey) throws ServiceException {
        try {
            Result<Object> result = _iKeyService.publicKeyFromPrivateKey(skKey);
            if (result.getException() != null) {
                Timber.tag("publicKeyFromPrivateKey").d("error %s", result.getException());
                throw new ServiceException((String) result.getException());
            }
            Timber.tag("publicKeyFromPrivateKey").d("success");
            return (int[]) result.getResult();
        }
        catch(Exception ex) {
            Timber.tag("publicKeyFromPrivateKey").d("error %s", ex.getMessage());
            throw new ServiceException((String) ex.getMessage());
        }
    }

    public String getAccountAddress(int[] pkKey) throws ServiceException {
        try {
            Result<Object> result = _iKeyService.getAccountAddress(pkKey);
            if (result.getException() != null) {
                Timber.tag("getAccountAddress").d("error %s", result.getException());
                throw new ServiceException((String) result.getException());
            }
            Timber.tag("getAccountAddress").d("success");
            return (String) result.getResult();
        }
        catch(Exception ex) {
            Timber.tag("getAccountAddress").d("error %s", ex.getMessage());
            throw new ServiceException((String) ex.getMessage());
        }
    }

    public int[] getTxMessage(String fromAddress, String nonce, String toAddress,
                              String amount, String gas, String gasPrice, String chainId) throws ServiceException {
        try {
            Result<Object> result = _iKeyService.getTxMessage(fromAddress, nonce, toAddress,
                    amount, gas, gasPrice,"",  chainId);
            if (result.getException() != null) {
                Timber.tag("getTxMessage").d("error %s", result.getException());
                throw new ServiceException((String) result.getException());
            }
            Timber.tag("getTxMessage").d("success");
            return (int[]) result.getResult();
        }
        catch(Exception ex) {
            Timber.tag("getTxMessage").d("error %s", ex.getMessage());
            throw new ServiceException((String) ex.getMessage());
        }
    }

    public String getTxHash(String fromAddress, String nonce, String toAddress,
                              String amount, String gas, String gasPrice, String chainId, int[] pkKey, int[] sig) throws ServiceException {
        try {
            Result<Object> result = _iKeyService.getTxHash(fromAddress, nonce, toAddress,
                    amount, gas, gasPrice,  chainId, pkKey, sig);
            if (result.getException() != null) {
                Timber.tag("getTxHash").d("error %s", result.getException());
                throw new ServiceException((String) result.getException());
            }
            Timber.tag("getTxHash").d("success");
            return (String) result.getResult();
        }
        catch(Exception ex) {
            Timber.tag("getTxHash").d("error %s", ex.getMessage());
            throw new ServiceException((String) ex.getMessage());
        }
    }

    public String getTxData(String fromAddress, String nonce, String toAddress,
                            String amount, String gas, String gasPrice, String chainId, int[] pkKey, int[] sig) throws ServiceException {
        try {
            Result<Object> result = _iKeyService.getTxData(fromAddress, nonce, toAddress,
                    amount, gas, gasPrice,  chainId, pkKey, sig);
            if (result.getException() != null) {
                Timber.tag("getTxData").d("error %s", result.getException());
                throw new ServiceException((String) result.getException());
            }
            Timber.tag("getTxData").d("success");
            return (String) result.getResult();
        }
        catch(Exception ex) {
            Timber.tag("getTxData").d("error %s", ex.getMessage());
            throw new ServiceException((String) ex.getMessage());
        }
    }

    public String getDogeProtocolToWei(String value) throws ServiceException {
        try {
            Result<Object> result = _iKeyService.getDogeProtocolToWei(value);
            if (result.getException() != null) {
                Timber.tag("getDogeProtocolToWei").d("error %s", result.getException());
                throw new ServiceException((String) result.getException());
            }
            Timber.tag("getDogeProtocolToWei").d("success");
            return (String) result.getResult();
        }
        catch(Exception ex) {
            Timber.tag("getDogeProtocolToWei").d("error %s", ex.getMessage());
            throw new ServiceException((String) ex.getMessage());
        }
    }

    public String getParseBigFloat(String value) throws ServiceException {
        try {
            Result<Object> result = _iKeyService.getParseBigFloat(value);
            if (result.getException() != null) {
                Timber.tag("getParseBigFloat").d("error %s", result.getException());
                throw new ServiceException((String) result.getException());
            }
            Timber.tag("getParseBigFloat").d("success");
            return (String) result.getResult();
        }
        catch(Exception ex) {
            Timber.tag("getParseBigFloat").d("error %s", ex.getMessage());
            throw new ServiceException((String) ex.getMessage());
        }
    }

    public String getWeiToDogeProtocol(String value) throws ServiceException {
        try {
            Result<Object> result = _iKeyService.getWeiToDogeProtocol(value);
            if (result.getException() != null) {
                Timber.tag("getWeiToDogeProtocol").d("error %s", result.getException());
                throw new ServiceException((String) result.getException());
            }
            Timber.tag("getWeiToDogeProtocol").d("success");
            return (String) result.getResult();
        }
        catch(Exception ex) {
            Timber.tag("getWeiToDogeProtocol").d("error %s", ex.getMessage());
            throw new ServiceException((String) ex.getMessage());
        }
    }

    public boolean encryptDataByAccount(Context context, String address, String password, byte[] SK_KEY, byte[] PK_KEY){
        return _iKeyStore.EncryptData(context, address, password, SK_KEY, PK_KEY);
    }

    public byte[] decryptDataByAccount(Context context, String address, String password) throws InvalidKeyException, KeyServiceException {
        return _iKeyStore.DecryptData(context, address, password);
    }

    public String exportKeyByAccount(Context context, String address)  {
        return _iKeyStore.ExportKey(context, address);
    }

    public byte[] importKeyByAccount(Context context, String jsonString, String password)  {
        return _iKeyStore.ImportKey(context, jsonString, password);
    }

    public void deleteKeyByAccount(Context context, String address) {
        _iKeyStore.DeleteKey(context, address);
    }
}
