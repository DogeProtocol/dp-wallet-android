package com.dpwallet.app.services;

import android.app.Activity;
import android.content.Context;

import com.dpwallet.app.entity.Result;
import com.dpwallet.app.hybrid.IHybridPqcJNIImpl;

public class KeyService implements IKeyService
{
    private Context context;
    private Activity activity;

    private IHybridPqcJNIImpl _iHybridPqcJNI = null;

    public KeyService(IHybridPqcJNIImpl iHybridPqcJNI)
    {
        this._iHybridPqcJNI = iHybridPqcJNI;
    }

    @Override
    public Result<Object> newAccountFromSeed(int[] expandedSeedArray)
    {
        String[] keys = _iHybridPqcJNI.KeypairSeed(expandedSeedArray);
        return new Result<Object>(keys, null);
    }

    @Override
    public Result<Object> newAccount()
    {
        String[] keys = _iHybridPqcJNI.Keypair();
        return new Result<Object>(keys, null);
    }

    @Override
    public Result<Object> signAccount(int[] message, int[] skKey)
    {
        String sign = _iHybridPqcJNI.Sign(message, skKey);
        return new Result<Object>(sign, null);
    }

    @Override
    public Result<Object> verifyAccount(int[] message, int[] sign, int[] pkKey)
    {
        int result = _iHybridPqcJNI.SignVerify(message, sign, pkKey);
        return new Result<Object>(result, null);
    }

    @Override
    public Result<Object> seedExpander(int[] seed)
    {
        String seededExpander = _iHybridPqcJNI.SeedExpander(seed);
        return new Result<Object>(seededExpander, null);
    }

    @Override
    public Result<Object> random()
    {
        String rnd = _iHybridPqcJNI.Random();
        return new Result<Object>(rnd, null);
    }

    @Override
    public Result<Object> publicKeyFromPrivateKey(int[] skKey)
    {
        String pk = _iHybridPqcJNI.PublicKeyFromPrivateKey(skKey);
        return new Result<Object>(pk, null);
    }

    @Override
    public Result<Object> scrypt(int[] skKey, int[] salt)
    {
        String[] scrypt = _iHybridPqcJNI.Scrypt(skKey, salt);
        int length = scrypt[0].length();
        int[] result = new int[length];
        String error = scrypt[1];


        for (int i = 0; i < length; i++) {
            int codePointAt = Character.codePointAt(scrypt[0], i);
            result[i] = codePointAt;
        }

        if(length > 0 )
        {
            return new Result<Object>(result, null);
        }
        return new Result<Object>(null, error);
    }

    @Override
    public Result<Object> getAccountAddress(int[] pkKey)
    {
        String[] apk = _iHybridPqcJNI.AddressFromPublicKey(pkKey);
        String addressResult = apk[0];
        String addressError = apk[1];

        if(!addressResult.isEmpty()){
            return new Result<Object>(addressResult, null);
        }
        return new Result<Object>(null, addressError);
   }

    @Override
    public Result<Object> isValidAddress(String address)
    {
        String[] validAddress = _iHybridPqcJNI.IsValidAddress(address);

        String result = validAddress[0];
        String error = validAddress[1];

        if(!result.isEmpty()){
            return new Result<Object>(result, null);
        }
        return new Result<Object>(null, error);
    }


    @Override
    public Result<Object> getTxnSigningHash(String fromAddress, String nonce, String toAddress,
                            String amount, String gasLimit, String data, String chainId)
    {
        String[] msg = _iHybridPqcJNI.TxnSigningHash(fromAddress, nonce, toAddress, amount, gasLimit, data, chainId);

        int msgLen = msg[0].length();
        int[] messageResult = new int[msgLen];
        String messageError = msg[1];

        for (int i = 0; i < msgLen; i++) {
            int codePointAt = Character.codePointAt(msg[0], i);
            messageResult[i] = codePointAt;
        }

        if(msgLen > 0 )
        {
            return new Result<Object>(messageResult, null);
        }
        return new Result<Object>(null, messageError);
    }

    @Override
    public Result<Object> getTxHash(String fromAddress, String nonce, String toAddress,
                         String amount, String gasLimit, String data, String chainId,
                         int[] pkKey, int[] sig)
    {
        String[] hash = _iHybridPqcJNI.TxHash(fromAddress, nonce, toAddress, amount, gasLimit, data, chainId, pkKey, sig);
        String hashResult = hash[0];
        String hashError = hash[1];
        if(!hashResult.isEmpty())
        {
            return new Result<Object>(hashResult, null);
        }
        return new Result<Object>(null, hashError);
    }

    @Override
    public Result<Object> getTxData(String fromAddress, String nonce, String toAddress,
                        String amount, String gasLimit, String data, String chainId,
                        int[] pkKey, int[] sig)
    {
        String[] txData = _iHybridPqcJNI.TxData(fromAddress, nonce, toAddress, amount, gasLimit, data, chainId, pkKey, sig);
        String dataResult = txData[0];
        String dataError = txData[1];
        if(!dataResult.isEmpty())
        {
            return new Result<Object>(dataResult, null);
        }
        return new Result<Object>(null, dataError);
    }

    @Override
    public Result<Object> getContractData(String method, String abiData, String argument1, String argument2)
    {
        String[] data = _iHybridPqcJNI.ContractData(method, abiData, argument1, argument2);
        String dataResult = data[0];
        String dataError = data[1];
        if(!dataResult.isEmpty())
        {
            return new Result<Object>(dataResult, null);
        }
        return new Result<Object>(null, dataError);
    }

    @Override
    public Result<Object> getParseBigFloat(String value)
    {
        String[] parse = _iHybridPqcJNI.ParseBigFloat(value);
        String parseResult = parse[0];
        String parseError = parse[1];
        if(!parseResult.isEmpty())
        {
            return new Result<Object>(parseResult, null);
        }
        return new Result<Object>(null, parseError);
    }

    @Override
    public Result<Object> getParseBigFloatInner(String value)
    {
        String[] parse = _iHybridPqcJNI.ParseBigFloatInner(value);
        String parseResult = parse[0];
        String parseError = parse[1];
        if(!parseResult.isEmpty())
        {
            return new Result<Object>(parseResult, null);
        }
        return new Result<Object>(null, parseError);
    }

    @Override
    public Result<Object> getDogeProtocolToWei(String value)
    {
        String[] wei = _iHybridPqcJNI.DogeProtocolToWei(value);
        String weiResult = wei[0];
        String weiError = wei[1];
        if(!weiResult.isEmpty())
        {
            return new Result<Object>(weiResult, null);
        }
        return new Result<Object>(null, weiError);
    }



    @Override
    public Result<Object> getWeiToDogeProtocol(String value)
    {
        String[] dp = _iHybridPqcJNI.WeiToDogeProtocol(value);
        String dpResult = dp[0];
        String dpError = dp[1];
        if(!dpResult.isEmpty())
        {
            return new Result<Object>(dpResult, null);
        }
        return new Result<Object>(null, dpError);
    }
}
