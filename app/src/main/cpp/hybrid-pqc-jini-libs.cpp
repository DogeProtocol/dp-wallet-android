#include <jni.h>
#include <string.h>
#include <stdio.h>
#include <sstream>
#include <iostream>
#include "hybrid.h"
#include "libgodp.h"
#define JNICALL

const int CRYPTO_MESSAGE_LEN = 32;
const int CRYPTO_SECRETKEY_BYTES = 64 + 2560 + 1312 + 128;
const int CRYPTO_PUBLICKEY_BYTES = 32 + 1312 + 64;
const int CRYPTO_SIGNATURE_BYTES = 2 + 64 + 2420 + 40 + CRYPTO_MESSAGE_LEN; //2558

extern "C" JNIEXPORT jobjectArray   JNICALL
Java_com_dpwallet_app_hybrid_HybridPqcJNIImpl_Keypair(JNIEnv* env, jobject )
{
    unsigned char *sk = (unsigned char*) malloc(CRYPTO_SECRETKEY_BYTES * sizeof(1));
    unsigned char *pk = (unsigned char*) malloc(CRYPTO_PUBLICKEY_BYTES * sizeof(1));

    crypto_sign_dilithium_ed25519_sphincs_keypair(pk, sk);

    jstring skkey;
    jstring pkkey;

    std::ostringstream osk;
    for (int i = 0; i < CRYPTO_SECRETKEY_BYTES; i++) {
        osk <<(int)sk[i] << ",";
    }

    std::ostringstream opk;
    for (int i = 0; i < CRYPTO_PUBLICKEY_BYTES; i++) {
        opk << (int)pk[i] << ",";
    }

    std::string skey = osk.str();
    std::string pkey =  opk.str();

    skkey =  env->NewStringUTF(skey.c_str());
    pkkey =  env->NewStringUTF(pkey.c_str());

    jclass keyClass;
    keyClass = env->FindClass("java/lang/String");

    jobjectArray keyArray;
    keyArray = env->NewObjectArray(2, keyClass, NULL); // create java string key array

    env->SetObjectArrayElement(keyArray, 0, skkey);
    env->SetObjectArrayElement(keyArray, 1, pkkey);

    //memory free
    memset(sk, 0, CRYPTO_SECRETKEY_BYTES);
    free(sk);

    memset(pk, 0, CRYPTO_PUBLICKEY_BYTES);
    free(pk);

    return keyArray; // return java string key array
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_wallet_app_hybrid_HybridPqcJNIImpl_Sign(JNIEnv* env, jobject ,
                                                   jintArray  message, jintArray skKey)
{
    jint msglen = env->GetArrayLength(message);

    unsigned char *m = (unsigned char*) malloc(msglen * sizeof(1));
    unsigned char *sk = (unsigned char*) malloc(CRYPTO_SECRETKEY_BYTES * sizeof(1) ) ;

    unsigned char *sm = (unsigned char*) malloc(CRYPTO_SIGNATURE_BYTES * sizeof(1));
    unsigned long long *smlen = (unsigned long long*) malloc(CRYPTO_SIGNATURE_BYTES * sizeof(8));

    jint *msg = env->GetIntArrayElements(message, NULL);
    jint *sKey = env->GetIntArrayElements(skKey, NULL);

    for (int i = 0; i < msglen; i++)
    {
       m[i] = (unsigned char)msg[i];
    }

    for (int i = 0; i < CRYPTO_SECRETKEY_BYTES; i++)
    {
        sk[i] = (unsigned char)sKey[i];
    }

    crypto_sign_compact_dilithium_ed25519_sphincs(sm, smlen, m, CRYPTO_MESSAGE_LEN, sk);

    std::ostringstream sig;
    for (int i = 0; i < CRYPTO_SIGNATURE_BYTES; i++) {
        sig << (int)sm[i] << ",";
    }

    std::string sign = sig.str();

    //memory free
    memset(m, 0, CRYPTO_MESSAGE_LEN);
    free(m);
    memset(sk, 0, CRYPTO_SECRETKEY_BYTES);
    free(sk);
    memset(sm, 0, CRYPTO_SIGNATURE_BYTES);
    free(sm);

    env->ReleaseIntArrayElements(message, msg, 0);
    env->ReleaseIntArrayElements(skKey, sKey, 0);

    return env->NewStringUTF(sign.c_str());
}

extern "C" JNIEXPORT jint  JNICALL
Java_com_wallet_app_hybrid_HybridPqcJNIImpl_SignVerify(JNIEnv* env, jobject,
                                                         jintArray message, jintArray sign, jintArray pkKey)
{
    jint msglen = env->GetArrayLength(message);

    unsigned char *m = (unsigned char*) malloc(msglen * sizeof(1));
    unsigned char *sm = (unsigned char*) malloc(CRYPTO_SIGNATURE_BYTES * sizeof(1));
    unsigned char *pk = (unsigned char*) malloc(CRYPTO_PUBLICKEY_BYTES * sizeof(1)) ;

    jint *msg = env->GetIntArrayElements(message, NULL);
    jint *sig = env->GetIntArrayElements(sign, NULL);
    jint *pKey = env->GetIntArrayElements(pkKey, NULL);

    for (int i = 0; i < msglen; i++)
    {
        m[i] = (unsigned char)msg[i];
    }

    for (int i = 0; i < CRYPTO_SIGNATURE_BYTES; i++)
    {
        sm[i] = (unsigned char)sig[i];
    }

    for (int i = 0; i < CRYPTO_PUBLICKEY_BYTES; i++)
    {
        pk[i] = (unsigned char)pKey[i];
    }


    int v = crypto_verify_compact_dilithium_ed25519_sphincs(m, CRYPTO_MESSAGE_LEN, sm, CRYPTO_SIGNATURE_BYTES, pk);

    //memory free
    memset(m, 0, CRYPTO_MESSAGE_LEN);
    free(m);
    memset(sm, 0, CRYPTO_SIGNATURE_BYTES);
    free(sm);
    memset(pk, 0, CRYPTO_PUBLICKEY_BYTES);
    free(pk);

    //Release string
    env->ReleaseIntArrayElements(message, msg, 0);
    env->ReleaseIntArrayElements(sign, sig, 0);
    env->ReleaseIntArrayElements(pkKey, pKey, 0);

    return v;
}
extern "C" JNIEXPORT jstring JNICALL
Java_com_wallet_app_hybrid_HybridPqcJNIImpl_PublicKeyFromPrivateKey(JNIEnv* env, jobject , jintArray skKey)
{
    unsigned char *sk = (unsigned char*) malloc(CRYPTO_SECRETKEY_BYTES * sizeof(1));
    unsigned char *pk = (unsigned char*) malloc(CRYPTO_PUBLICKEY_BYTES * sizeof(1));

    jint *sKey = env->GetIntArrayElements(skKey, NULL);

    for (int i = 0; i < CRYPTO_SECRETKEY_BYTES; i++)
    {
        sk[i] = (unsigned char)(sKey[i]);
    }

    //crypto_public_key_from_private_key_falcon_ed25519(pk, sk);

    std::ostringstream opk;
    for (int i = 0; i < CRYPTO_PUBLICKEY_BYTES; i++) {
        opk << (int)pk[i] << ",";
    }

    std::string pkkey =  opk.str();

    //memory free
    memset(sk, 0, CRYPTO_SECRETKEY_BYTES);
    free(sk);
    memset(pk, 0, CRYPTO_PUBLICKEY_BYTES);
    free(pk);

    env->ReleaseIntArrayElements(skKey, sKey, 0);

    return env->NewStringUTF(pkkey.c_str());
}

extern "C" JNIEXPORT jobjectArray JNICALL
Java_com_wallet_app_hybrid_HybridPqcJNIImpl_AddressFromPublicKey(JNIEnv* env, jobject ,
                                                                   jintArray pkKey)
{
    jint *pKey = env->GetIntArrayElements(pkKey, NULL);
    char *pk = (char*) malloc(CRYPTO_PUBLICKEY_BYTES * sizeof(1));

    for (int i = 0; i < CRYPTO_PUBLICKEY_BYTES; i++)
    {
        pk[i] = (char)(pKey[i]);
    }

    PublicKeyToAddress_return r= PublicKeyToAddress(const_cast<char*>(pk),
                                                    CRYPTO_PUBLICKEY_BYTES);
    jstring result = env->NewStringUTF(r.r0);
    jstring error = env->NewStringUTF(r.r1);

    jclass keyClass;
    keyClass = env->FindClass("java/lang/String");

    jobjectArray keyArray;
    keyArray = env->NewObjectArray(2, keyClass, NULL); // create java string key array

    env->SetObjectArrayElement(keyArray, 0, result);
    env->SetObjectArrayElement(keyArray, 1, error);

    memset(pk, 0, CRYPTO_PUBLICKEY_BYTES);
    free(pk);

    if(r.r0 != NULL)
    {
        memset(r.r0, 0, sizeof(strlen(r.r0)));
        free(r.r0);
    }
    if(r.r1 != NULL)
    {
        memset(r.r1, 0, sizeof(strlen(r.r1)));
        free(r.r1);
    }

    env->ReleaseIntArrayElements(pkKey, pKey, 0);

    return keyArray;
}

extern "C" JNIEXPORT jobjectArray JNICALL
Java_com_wallet_app_hybrid_HybridPqcJNIImpl_TxnSigningHash(JNIEnv* env, jobject ,
                        jstring from, jstring nonce, jstring to, jstring value,
                        jstring gasLimit, jstring data, jstring chainId)
{
    jboolean isCopy;
    char *f = const_cast<char *>(env->GetStringUTFChars(from, &isCopy));
    char *n = const_cast<char *>(env->GetStringUTFChars(nonce, &isCopy));
    char *t = const_cast<char *>(env->GetStringUTFChars(to, &isCopy));
    char *v = const_cast<char *>(env->GetStringUTFChars(value, &isCopy));
    char *gl = const_cast<char *>(env->GetStringUTFChars(gasLimit, &isCopy));
    char *d = const_cast<char *>(env->GetStringUTFChars(data, &isCopy));
    char *c = const_cast<char *>(env->GetStringUTFChars(chainId, &isCopy));

    TxnSigningHash_return r = TxnSigningHash(f, n, t, v, gl, d, c);

    jstring result = env->NewStringUTF(r.r0);
    jstring error = env->NewStringUTF(r.r1);

    jclass keyClass;
    keyClass = env->FindClass("java/lang/String");

    jobjectArray keyArray;
    keyArray = env->NewObjectArray(2, keyClass, NULL); // create java string key array

    env->SetObjectArrayElement(keyArray, 0, result);
    env->SetObjectArrayElement(keyArray, 1, error);

    if(r.r0 != NULL)
    {
        memset(r.r0, 0, sizeof(strlen(r.r0)));
        free(r.r0);
    }
    if(r.r1 != NULL)
    {
        memset(r.r1, 0, sizeof(strlen(r.r1)));
        free(r.r1);
    }

    env->ReleaseStringUTFChars(from, f);
    env->ReleaseStringUTFChars(nonce, n);
    env->ReleaseStringUTFChars(to, t);
    env->ReleaseStringUTFChars(value, v);
    env->ReleaseStringUTFChars(gasLimit, gl);
    env->ReleaseStringUTFChars(data, d);
    env->ReleaseStringUTFChars(chainId, c);

    return keyArray;
}

extern "C" JNIEXPORT jobjectArray JNICALL
Java_com_wallet_app_hybrid_HybridPqcJNIImpl_TxHash(JNIEnv* env, jobject ,
                                                    jstring from, jstring nonce, jstring to, jstring value,
                                                    jstring gasLimit, jstring data, jstring chainId,
                                                    jintArray pkKey, jintArray sign)
{
    jboolean isCopy;
    char *f = const_cast<char *>(env->GetStringUTFChars(from, &isCopy));
    char *n = const_cast<char *>(env->GetStringUTFChars(nonce, &isCopy));
    char *t = const_cast<char *>(env->GetStringUTFChars(to, &isCopy));
    char *v = const_cast<char *>(env->GetStringUTFChars(value, &isCopy));
    char *gl = const_cast<char *>(env->GetStringUTFChars(gasLimit, &isCopy));
    char *d = const_cast<char *>(env->GetStringUTFChars(data, &isCopy));
    char *c = const_cast<char *>(env->GetStringUTFChars(chainId, &isCopy));

    jint *pKey = env->GetIntArrayElements(pkKey, NULL);
    char *pk = (char*) malloc(CRYPTO_PUBLICKEY_BYTES * sizeof(1)) ;
    for (int i = 0; i < CRYPTO_PUBLICKEY_BYTES; i++)
    {
        pk[i] = (char)(pKey[i]);
    }

    jint *sig = env->GetIntArrayElements(sign, NULL);
    char *sm = (char*) malloc(CRYPTO_SIGNATURE_BYTES * sizeof(1));
    for (int i = 0; i < CRYPTO_SIGNATURE_BYTES; i++)
    {
        sm[i] = (char)sig[i];
    }

    TxHash_return r = TxHash(f, n, t, v, gl, d, c,
                             pk, sm,  CRYPTO_PUBLICKEY_BYTES, CRYPTO_SIGNATURE_BYTES);

    jstring result = env->NewStringUTF(r.r0);
    jstring error = env->NewStringUTF(r.r1);

    jclass keyClass;
    keyClass = env->FindClass("java/lang/String");

    jobjectArray keyArray;
    keyArray = env->NewObjectArray(2, keyClass, NULL); // create java string key array

    env->SetObjectArrayElement(keyArray, 0, result);
    env->SetObjectArrayElement(keyArray, 1, error);

    memset(pk, 0, CRYPTO_PUBLICKEY_BYTES);
    free(pk);

    memset(sm, 0, CRYPTO_SIGNATURE_BYTES);
    free(sm);

    if(r.r0 != NULL)
    {
        memset(r.r0, 0, sizeof(strlen(r.r0)));
        free(r.r0);
    }
    if(r.r1 != NULL)
    {
        memset(r.r1, 0, sizeof(strlen(r.r1)));
        free(r.r1);
    }

    env->ReleaseStringUTFChars(from, f);
    env->ReleaseStringUTFChars(nonce, n);
    env->ReleaseStringUTFChars(to, t);
    env->ReleaseStringUTFChars(value, v);
    env->ReleaseStringUTFChars(gasLimit, gl);
    env->ReleaseStringUTFChars(data, d);
    env->ReleaseStringUTFChars(chainId, c);

    env->ReleaseIntArrayElements(pkKey, pKey, 0);
    env->ReleaseIntArrayElements(sign, sig, 0);

    return keyArray;
}

extern "C" JNIEXPORT jobjectArray JNICALL
Java_com_wallet_app_hybrid_HybridPqcJNIImpl_TxData(JNIEnv* env, jobject ,
                                                     jstring from, jstring nonce, jstring to, jstring value,
                                                     jstring gasLimit, jstring data, jstring chainId,
                                                     jintArray pkKey, jintArray sign)
{
    jboolean isCopy;
    char *f = const_cast<char *>(env->GetStringUTFChars(from, &isCopy));
    char *n = const_cast<char *>(env->GetStringUTFChars(nonce, &isCopy));
    char *t = const_cast<char *>(env->GetStringUTFChars(to, &isCopy));
    char *v = const_cast<char *>(env->GetStringUTFChars(value, &isCopy));
    char *gl = const_cast<char *>(env->GetStringUTFChars(gasLimit, &isCopy));
    char *d = const_cast<char *>(env->GetStringUTFChars(data, &isCopy));
    char *c = const_cast<char *>(env->GetStringUTFChars(chainId, &isCopy));

    jint *pKey = env->GetIntArrayElements(pkKey, NULL);
    char *pk = (char*) malloc(CRYPTO_PUBLICKEY_BYTES * sizeof(1));
    for (int i = 0; i < CRYPTO_PUBLICKEY_BYTES; i++)
    {
        pk[i] = (char)(pKey[i]);
    }

    jint *sig = env->GetIntArrayElements(sign, NULL);
    char *sm = (char*) malloc(CRYPTO_SIGNATURE_BYTES * sizeof(1));
    for (int i = 0; i < CRYPTO_SIGNATURE_BYTES; i++)
    {
        sm[i] = (char)sig[i];
    }

    TxData_return r = TxData(f, n, t, v, gl, d, c,
                             pk, sm,  CRYPTO_PUBLICKEY_BYTES, CRYPTO_SIGNATURE_BYTES);

    jstring result = env->NewStringUTF(r.r0);
    jstring error = env->NewStringUTF(r.r1);

    jclass keyClass;
    keyClass = env->FindClass("java/lang/String");

    jobjectArray keyArray;
    keyArray = env->NewObjectArray(2, keyClass, NULL); // create java string key array

    env->SetObjectArrayElement(keyArray, 0, result);
    env->SetObjectArrayElement(keyArray, 1, error);

    memset(pk, 0, CRYPTO_PUBLICKEY_BYTES);
    free(pk);

    memset(sm, 0, CRYPTO_SIGNATURE_BYTES);
    free(sm);

    if(r.r0 != NULL)
    {
        memset(r.r0, 0, sizeof(strlen(r.r0)));
        free(r.r0);
    }
    if(r.r1 != NULL)
    {
        memset(r.r1, 0, sizeof(strlen(r.r1)));
        free(r.r1);
    }

    env->ReleaseStringUTFChars(from, f);
    env->ReleaseStringUTFChars(nonce, n);
    env->ReleaseStringUTFChars(to, t);
    env->ReleaseStringUTFChars(value, v);
    env->ReleaseStringUTFChars(gasLimit, gl);
    env->ReleaseStringUTFChars(data, d);
    env->ReleaseStringUTFChars(chainId, c);

    env->ReleaseIntArrayElements(pkKey, pKey, 0);
    env->ReleaseIntArrayElements(sign, sig, 0);

    return keyArray;
}

extern "C" JNIEXPORT jobjectArray JNICALL
Java_com_wallet_app_hybrid_HybridPqcJNIImpl_DogeProtocolToWei(JNIEnv* env, jobject ,
                                                                jstring quantity)
{
    jboolean isCopy;
    char *q = const_cast<char *>(env->GetStringUTFChars(quantity, &isCopy));

    EtherToWeiFloat_return r= EtherToWeiFloat(const_cast<char*>(q));
    jstring result = env->NewStringUTF(r.r0);
    jstring error = env->NewStringUTF(r.r1);

    jclass keyClass;
    keyClass = env->FindClass("java/lang/String");

    jobjectArray keyArray;
    keyArray = env->NewObjectArray(2, keyClass, NULL); // create java string key array

    env->SetObjectArrayElement(keyArray, 0, result);
    env->SetObjectArrayElement(keyArray, 1, error);

    if(r.r0 != NULL)
    {
        memset(r.r0, 0, sizeof(strlen(r.r0)));
        free(r.r0);
    }
    if(r.r1 != NULL)
    {
        memset(r.r1, 0, sizeof(strlen(r.r1)));
        free(r.r1);
    }

    env->ReleaseStringUTFChars(quantity, q);

    return keyArray;
}

extern "C" JNIEXPORT jobjectArray JNICALL
Java_com_wallet_app_hybrid_HybridPqcJNIImpl_ParseBigFloat(JNIEnv* env, jobject ,
                                                                jstring quantity)
{
    jboolean isCopy;
    char *q = const_cast<char *>(env->GetStringUTFChars(quantity, &isCopy));

    ParseBigFloat_return r= ParseBigFloat(const_cast<char*>(q));
    jstring result = env->NewStringUTF(r.r0);
    jstring error = env->NewStringUTF(r.r1);

    jclass keyClass;
    keyClass = env->FindClass("java/lang/String");

    jobjectArray keyArray;
    keyArray = env->NewObjectArray(2, keyClass, NULL); // create java string key array

    env->SetObjectArrayElement(keyArray, 0, result);
    env->SetObjectArrayElement(keyArray, 1, error);

    if(r.r0 != NULL)
    {
        memset(r.r0, 0, sizeof(strlen(r.r0)));
        free(r.r0);
    }
    if(r.r1 != NULL)
    {
        memset(r.r1, 0, sizeof(strlen(r.r1)));
        free(r.r1);
    }

    env->ReleaseStringUTFChars(quantity, q);

    return keyArray;
}

extern "C" JNIEXPORT jobjectArray JNICALL
Java_com_wallet_app_hybrid_HybridPqcJNIImpl_WeiToDogeProtocol(JNIEnv* env, jobject ,
                                                            jstring quantity)
{
    jboolean isCopy;
    char *q = const_cast<char *>(env->GetStringUTFChars(quantity, &isCopy));

    WeiToEther_return r= WeiToEther(const_cast<char*>(q));
    jstring result = env->NewStringUTF(r.r0);
    jstring error = env->NewStringUTF(r.r1);

    jclass keyClass;
    keyClass = env->FindClass("java/lang/String");

    jobjectArray keyArray;
    keyArray = env->NewObjectArray(2, keyClass, NULL); // create java string key array

    env->SetObjectArrayElement(keyArray, 0, result);
    env->SetObjectArrayElement(keyArray, 1, error);

    if(r.r0 != NULL)
    {
        memset(r.r0, 0, sizeof(strlen(r.r0)));
        free(r.r0);
    }
    if(r.r1 != NULL)
    {
        memset(r.r1, 0, sizeof(strlen(r.r1)));
        free(r.r1);
    }

    env->ReleaseStringUTFChars(quantity, q);

    return keyArray;
}
