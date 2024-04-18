package com.dpwallet.app.services;

import com.dpwallet.app.entity.Result;

public interface IKeyService {

    Result<Object> newAccount();

    Result<Object> signAccount(int[] message, int[] skKey);

    Result<Object> verifyAccount(int[] message, int[] sign, int[] pkKey);

    Result<Object> publicKeyFromPrivateKey(int[] skKey);


    Result<Object> getAccountAddress(int[] pkKey);

    Result<Object> getTxnSigningHash(String fromAddress, String nonce, String toAddress,
                     String amount, String gasLimit,  String data, String chainId);

    Result<Object> getTxHash(String fromAddress, String nonce, String toAddress,
                             String amount, String gasLimit,  String chainId,
                             int[] pkKey, int[] sig);

    Result<Object> getTxData(String fromAddress, String nonce, String toAddress,
                             String amount, String gasLimit,  String chainId,
                             int[] pkKey, int[] sig);

    Result<Object> unLockAccount(String encrypted_skKey, String password);

    Result<Object> importUnLockAccount(String encrypted_skKey, String password);

    Result<Object> storeAccountKey(String skKey, String password);

    Result<Object> getDogeProtocolToWei(String value);

    Result<Object> getParseBigFloat(String value);

    Result<Object> getWeiToDogeProtocol(String value);
    
}
