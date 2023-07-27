package com.dpwallet.app.hybrid;

public class HybridPqcJNIImpl implements IHybridPqcJNIImpl {
    //private final Context context;

    public HybridPqcJNIImpl() {
        System.loadLibrary("dpwallet");
      //  this.context = ctx;
    }

    public native String[] dpKeypair();

    public native String dpSign(int[] m, int[] skKey);

    public native int dpSignVerify(int[] m, int[] sign, int[] pkKey);

    public native String dpPublicKeyFromPrivateKey(int[] skKey);

    public native String[] dpAddressFromPublicKey(int[] pkKey);

    public native String[] dpTxMessage(String from, String nonce, String to, String value,
                                       String gasLimit, String gasPrice, String data, String chainId);

    public native String[] dpTxHash(String from, String nonce, String to, String value,
                                    String gasLimit, String gasPrice, String data, String chainId,
                                    int[] pkKey, int[] sign);

    public native String[] dpTxData(String from, String nonce, String to, String value,
                                    String gasLimit, String gasPrice, String data, String chainId,
                                    int[] pkKey, int[] sign);

    public native String[] dpDogeProtocolToWei(String quantity);

    public native String[] dpParseBigFloat(String quantity);

    public native String[] dpWeiToDogeProtocol(String quantity);

}
