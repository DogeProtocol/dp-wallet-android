package com.dpwallet.app.hybrid;

public class HybridPqcJNIImpl implements IHybridPqcJNIImpl {
    //private final Context context;

    public HybridPqcJNIImpl() {
        System.loadLibrary("dpwallet");
      //  this.context = ctx;
    }

    public native String[] KeypairSeed(int[] expandedSeedArray);
    public native String[] Keypair();

    public native String Sign(int[] m, int[] skKey);

    public native int SignVerify(int[] m, int[] sign, int[] pkKey);

    public native String SeedExpander(int[] seed);

    public native String Random();

    public native String PublicKeyFromPrivateKey(int[] skKey);

    public native String[] Scrypt(int[] skKey, int[] salt);

    public native String[] AddressFromPublicKey(int[] pkKey);

    public native String[] IsValidAddress(String address);

    public native String[] TxnSigningHash(String from, String nonce, String to, String value,
                                       String gasLimit,  String data, String chainId);

    public native String[] TxHash(String from, String nonce, String to, String value,
                                    String gasLimit, String data, String chainId,
                                    int[] pkKey, int[] sign);

    public native String[] TxData(String from, String nonce, String to, String value,
                                    String gasLimit,  String data, String chainId,
                                    int[] pkKey, int[] sign);

    public native String[] ContractData(String method, String abidata, String argument1, String argument2);

    public native String[] ParseBigFloat(String quantity);

    public native String[] ParseBigFloatInner(String quantity);

    public native String[] WeiToDogeProtocol(String quantity);

    public native String[] DogeProtocolToWei(String quantity);

}
