package com.dpwallet.app.keystorage;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.security.keystore.UserNotAuthenticatedException;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.RequiresApi;

import com.dpwallet.app.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import timber.log.Timber;

import com.dpwallet.app.entity.KeyServiceException;
import com.dpwallet.app.utils.GlobalMethods;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONObject;
import java.util.Base64;


public class KeyStore implements IKeyStore {

    //private static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    //private static final String KEYSTORE_FOLDER = "keystore/keystore";

    private Toast toast = null;

    public static final int SALT_LENGTH = 128; //16;
    public static final int IV_LENGTH = 128; //16;

    public static final int KEY_LENGTH = 256; //32;
    public static final int ITERATION_COUNT = 100;

    private static final String RANDOM_ALGORITHM = "SHA1PRNG";
    private static final String PBKDF2_ALGORITHM = "PBKDF2withHmacSHA1"; // "PBKDF2withHmacSHA256";
    private static final String CIPHER_ALGORITHM =  "AES/CBC/PKCS7Padding"; //"AES/CBC/PKCS5Padding";
    public static final String SECRET_KEY_ALGORITHM = "AES";
    private static final String TAG = "EncryptionPassword";

    public KeyStore() {

    }

    @Override
    public boolean EncryptData(Context context, String address, String password, String keyPair) {
        return encrypt(context, address, password, keyPair);
    }

    @Override
    public byte[] DecryptData(Context context, String address, String password) throws InvalidKeyException, KeyServiceException {
        return decrypt(context, address, password);
    }

    /*
    @Override
    public void DeleteKey(Context context, String address) {
        deleteKey(context, address.toLowerCase().substring(2));
        deleteKey(context, address.toLowerCase().substring(2) + "-pk-key");
    }


    @Override
    public String ExportKey(Context context, String address) {
        try {
            //encrypted_skKey
            String skKeyPath = getFilePath(context, address.toLowerCase().substring(2));
            byte[] skKeyMessage = readBytesFromFile(skKeyPath);

            byte[] skKeySalt = Arrays.copyOfRange(skKeyMessage, 0, 16);
            byte[] skKeyIv = Arrays.copyOfRange(skKeyMessage,  16, 32);
            byte[] skKeyEncryptedData = Arrays.copyOfRange(skKeyMessage,  32, skKeyMessage.length);

            String skKeySaltHex = byteArrayToString(skKeySalt);
            String skKeyIvHex = byteArrayToString(skKeyIv);
            String skKeyEncryptedHex = byteArrayToString(skKeyEncryptedData);

            String  encrypted_skKey = skKeySaltHex + skKeyIvHex + skKeyEncryptedHex;

            //encrypted_pkKey
            String pkKeyPath = getFilePath(context, address.toLowerCase().substring(2) + "-pk-key");
            byte[] pkKeyMessage = readBytesFromFile(pkKeyPath);

            byte[] pkKeySalt = Arrays.copyOfRange(pkKeyMessage, 0, 16);
            byte[] pkKeyIv = Arrays.copyOfRange(pkKeyMessage,  16, 32);
            byte[] pkKeyEncryptedData = Arrays.copyOfRange(pkKeyMessage,  32, pkKeyMessage.length);

            String pkKeySaltHex = byteArrayToString(pkKeySalt);
            String pkKeyIvHex = byteArrayToString(pkKeyIv);
            String pkKeyEncryptedHex = byteArrayToString(pkKeyEncryptedData);

            String  encrypted_pkKey = pkKeySaltHex + pkKeyIvHex + pkKeyEncryptedHex;

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("encrypted_skkey", encrypted_skKey);
            jsonObject.put("encrypted_pkkey", encrypted_pkKey);

            String jSONDocument = jsonObject.toString();

            return jSONDocument;
        }
        catch (Exception e)
        {

        }
        return null;
    }

    @Override
    public byte[] ImportKey(Context context, String jsonString, String password) {
        try{
            JSONObject jsonObject = new JSONObject(jsonString);
            String encrypted_skKey = jsonObject.getJSONObject("encrypted_skkey").toString();

            String skKeySaltString = encrypted_skKey.substring(0, 32);
            String skKeyIvString = encrypted_skKey.substring(32, 64);
            String skKeyEncryptedDataString = encrypted_skKey.substring(64);

            byte[] skKeySalt = stringToByteArray(skKeySaltString);
            byte[] skKeyIv = stringToByteArray(skKeyIvString);
            byte[] skKeyEncryptedData = stringToByteArray(skKeyEncryptedDataString);

            SecretKey secretKey = getSecretKey(password, skKeySalt);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(skKeyIv);

            Cipher outCipher = Cipher.getInstance(CIPHER_ALGORITHM);
            outCipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            byte[] SK_KEY = outCipher.doFinal(skKeyEncryptedData);

            return SK_KEY;
        }
        catch (Exception e)
        {

        }
        return null;
    }
*/
    private synchronized boolean encrypt(Context context, String address, String password,
                                         String keyPair) {
        try
        {
            byte[] salt = generateSalt();
            SecretKey secretKey = getSecretKey(password, salt);

            byte[] iv = generateIv();
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

            //sk key
            byte[] keyEncryptedData = cipher.doFinal(keyPair.getBytes());
            byte[] keyMessage = new byte[salt.length + iv.length + keyEncryptedData.length];
            keyMessage = ArrayUtils.addAll(ArrayUtils.addAll(salt,  iv),keyEncryptedData);
            String skKeyEncryptedPath = getFilePath(context, address.toLowerCase().substring(2));
            writeBytesToFile(skKeyEncryptedPath, keyMessage);

            //pk key
            //byte[] pkKeyEncryptedData = cipher.doFinal(PK_KEY);
            //byte[] pkKeyMessage = new byte[salt.length + iv.length + pkKeyEncryptedData.length];
            //pkKeyMessage = ArrayUtils.addAll(ArrayUtils.addAll(salt,  iv), pkKeyEncryptedData);
            //String pkKeyEncryptedPath = getFilePath(context, address.toLowerCase().substring(2) + "-pk-key");
            //writeBytesToFile(pkKeyEncryptedPath, pkKeyMessage);

            return true;
        }
        catch (Exception ex)
        {
            Timber.tag(TAG).d(ex, "Key store error");
        }
        return false;
    }

    private synchronized byte[] decrypt(Context context, String address, String password) throws InvalidKeyException, KeyServiceException {
        try
        {
            String decryptedKeyPath = getFilePath(context, address.toLowerCase().substring(2));

            byte[] skKeyMessage = readBytesFromFile(decryptedKeyPath);

            byte[] salt = Arrays.copyOfRange(skKeyMessage, 0, 16);
            byte[] iv = Arrays.copyOfRange(skKeyMessage,  16, 32);
            byte[] skKeyEncryptedData = Arrays.copyOfRange(skKeyMessage,  32, skKeyMessage.length);

            SecretKey secretKey = getSecretKey(password, salt);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            Cipher outCipher = Cipher.getInstance(CIPHER_ALGORITHM);
            outCipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            byte[] SK_KEY = outCipher.doFinal(skKeyEncryptedData);

            return SK_KEY;
        }
        catch (InvalidKeyException e)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (e instanceof UserNotAuthenticatedException)
                {
                    throw new UserNotAuthenticatedException(context.getString(R.string.authentication_error));
                }
            }
            throw new KeyServiceException(e.getMessage());
        } catch (IOException | CertificateException | KeyStoreException | NoSuchAlgorithmException | NoSuchPaddingException  e)
        {
            e.printStackTrace();
            throw new KeyServiceException(e.getMessage());
        }
        catch (Exception e)
        {
            throw new KeyServiceException(e.getMessage());
        }
    }

    private synchronized static String getFilePath(Context context, String fileName)
    {
        //check for matching file
        File check = new File(context.getFilesDir(), fileName);
        if (check.exists())
        {
            return check.getAbsolutePath();
        }
        else
        {
            //find matching file, ignoring case
            File[] files = context.getFilesDir().listFiles();
            for (File checkFile : files)
            {
                if (checkFile.getName().equalsIgnoreCase(fileName))
                {
                    return checkFile.getAbsolutePath();
                }
            }
        }
        return check.getAbsolutePath(); //Should never get here
    }

    private boolean writeBytesToFile(String path, byte[] data)
    {
        File file = new File(path);
        try (FileOutputStream fos = new FileOutputStream(file))
        {
            fos.write(data);
        }
        catch (IOException e)
        {
            Timber.d(e, "Exception while writing file ");
            return false;
        }

        return true;
    }

    private static byte[] readBytesFromFile(String path)
    {
        byte[] bytes = null;
        File file = new File(path);
        try (FileInputStream fin = new FileInputStream(file))
        {
            bytes = readBytesFromStream(fin);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return bytes;
    }

    private static byte[] readBytesFromStream(InputStream in) throws IOException
    {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 2048;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = in.read(buffer)) != -1)
        {
            byteBuffer.write(buffer, 0, len);
        }

        byteBuffer.close();
        return byteBuffer.toByteArray();
    }

    private synchronized void deleteKey(Context context, String fileName)
    {
        try {
            File encryptedKeyBytes = new File(getFilePath(context, fileName));
            if (encryptedKeyBytes.exists()) encryptedKeyBytes.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final char[] ENC_TAB = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };
    private static String byteArrayToString(byte[] data) {
        int length = data.length;
        StringBuffer buff = new StringBuffer(length*2);
        int i = 0;
        while (i < length)
        {
            buff.append(ENC_TAB[(data[i]&0xF0)>>4]);
            buff.append(ENC_TAB[data[i]&0x0F]);
            i ++;
        }

        return buff.toString();
    }

    // decoding characters table.
    public static final byte[] DEC_TAB =
            {
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 16
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 32
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 48
                    0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 64

                    0x00, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 80
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 96
                    0x00, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // 112
                    0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00
            };
    private static byte[] stringToByteArray(String hex) {
        int i = 0;
        int total = (hex.length()/2)*2;
        int idx = 0;
        byte[] data = new byte[hex.length()/2];
        while (i < total)
        {
            data[idx++] = (byte)((DEC_TAB[hex.charAt(i++)]<<4)|DEC_TAB[hex.charAt(i++)]);
        }
        return data;
    }

    private static SecretKey getSecretKey(String password, byte[] salt) throws Exception {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
            PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_LENGTH );
            SecretKey tmp = factory.generateSecret(pbeKeySpec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), SECRET_KEY_ALGORITHM);
            return secret;
        } catch (Exception e) {
            throw new Exception("Unable to get secret key", e);
        }
    }

    private static byte[] generateSalt() throws Exception {
        try {
            SecureRandom random = SecureRandom.getInstance(RANDOM_ALGORITHM);
            byte[] salt = new byte[SALT_LENGTH / 8];
            random.nextBytes(salt);
            return salt;
           } catch (Exception e) {
            throw new Exception("Unable to generate salt", e);
        }
    }

    private static byte[] generateIv() throws NoSuchAlgorithmException, NoSuchProviderException {
        SecureRandom random = SecureRandom.getInstance(RANDOM_ALGORITHM);
        byte[] iv = new byte[IV_LENGTH / 8];
        random.nextBytes(iv);
        return iv;
    }

    private void download(Context context, String URL, String filename, byte[] buffer)
    {
        try
        {
            URL url = new URL(URL);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();

            String PATH = Environment.getExternalStorageDirectory().toString()
                    + "/wallet";
            Log.v("LOG_TAG", "PATH: " + PATH);

            File file = new File(PATH);
            file.mkdirs();
            File outputFile = new File(file, filename);
            FileOutputStream fos = new FileOutputStream(outputFile);
            InputStream is = c.getInputStream();

            int len1 = 0;

            while ((len1 = is.read(buffer)) != -1)
            {
                fos.write(buffer, 0, len1);
            }

            fos.close();
            is.close();

            String message = " A new file is downloaded successfully";
            GlobalMethods.ShowToast(context, message);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}

