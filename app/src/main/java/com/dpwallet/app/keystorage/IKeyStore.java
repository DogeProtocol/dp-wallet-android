package com.dpwallet.app.keystorage;

import android.content.Context;
import android.security.keystore.UserNotAuthenticatedException;

import com.dpwallet.app.entity.KeyServiceException;

import java.security.InvalidKeyException;

public interface IKeyStore {

    boolean EncryptData(Context context, String address, String password, byte[] SK_KEY, byte[] PK_KEY);

    byte[] DecryptData(Context context, String address, String password) throws InvalidKeyException, KeyServiceException;

    String ExportKey(Context context, String address);

    byte[] ImportKey(Context context, String jsonString, String password);

    void DeleteKey(Context context, String address);

}
