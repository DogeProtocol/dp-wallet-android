package com.dpwallet.app.hybrid;

public interface IHybridPqcJNIImpl {

    String[] Keypair();

    String Sign(int[] m, int[] skKey);

    int SignVerify(int[] m, int[] sign, int[] pkKey);

    String PublicKeyFromPrivateKey(int[] skKey);

    String[] AddressFromPublicKey(int[] pkKey);

    String[] TxnSigningHash(String from, String nonce, String to, String value,
                                           String gasLimit,  String data, String chainId);

    String[] TxHash(String from, String nonce, String to, String value,
                                        String gasLimit, String data, String chainId,
                                        int[] pkKey, int[] sign);

    String[] TxData(String from, String nonce, String to, String value,
                                        String gasLimit, String data, String chainId,
                                        int[] pkKey, int[] sign);

    String[] DogeProtocolToWei(String quantity);

    String[] ParseBigFloat(String quantity);

    String[] WeiToDogeProtocol(String quantity);
}
