package com.dpwallet.app.hybrid;

public interface IHybridPqcJNIImpl {

    String[] dpKeypair();

    String dpSign(int[] m, int[] skKey);

    int dpSignVerify(int[] m, int[] sign, int[] pkKey);

    String dpPublicKeyFromPrivateKey(int[] skKey);

    String[] dpAddressFromPublicKey(int[] pkKey);

    String[] dpTxMessage(String from, String nonce, String to, String value,
                                           String gasLimit, String gasPrice, String data, String chainId);

    String[] dpTxHash(String from, String nonce, String to, String value,
                                        String gasLimit, String gasPrice, String data, String chainId,
                                        int[] pkKey, int[] sign);

    String[] dpTxData(String from, String nonce, String to, String value,
                                        String gasLimit, String gasPrice, String data, String chainId,
                                        int[] pkKey, int[] sign);

    String[] dpDogeProtocolToWei(String quantity);

    String[] dpParseBigFloat(String quantity);

    String[] dpWeiToDogeProtocol(String quantity);
}
