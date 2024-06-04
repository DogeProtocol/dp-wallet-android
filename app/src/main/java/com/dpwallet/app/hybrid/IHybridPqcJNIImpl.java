package com.dpwallet.app.hybrid;

public interface IHybridPqcJNIImpl {

    String[] KeypairSeed(int[] expandedSeedArray);
    String[] Keypair();

    String Sign(int[] m, int[] skKey);

    int SignVerify(int[] m, int[] sign, int[] pkKey);

    String[] SeedExpander(int[] seed);

    String[] Random();

    String PublicKeyFromPrivateKey(int[] skKey);

    String[] Scrypt(int[] skKey, int[] salt);

    String[] AddressFromPublicKey(int[] pkKey);

    String[] IsValidAddress(String address);

    String[] TxnSigningHash(String from, String nonce, String to, String value,
                                           String gasLimit,  String data, String chainId);

    String[] TxHash(String from, String nonce, String to, String value,
                                        String gasLimit, String data, String chainId,
                                        int[] pkKey, int[] sign);

    String[] TxData(String from, String nonce, String to, String value,
                                        String gasLimit, String data, String chainId,
                                        int[] pkKey, int[] sign);

    String[] ContractData(String method, String abidata, String argument1, String argument2);

    String[] ParseBigFloat(String quantity);

    String[] ParseBigFloatInner(String quantity);

    String[] DogeProtocolToWei(String quantity);

    String[] WeiToDogeProtocol(String quantity);
}
