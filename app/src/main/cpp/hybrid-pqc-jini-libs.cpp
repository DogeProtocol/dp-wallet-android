#include <jni.h>
#include <string.h>
#include <stdio.h>
#include <sstream>
#include <iostream>
#include "hybrid.h"
#include "randombytes.h"
#include "shake_prng.h"
#include "libgodp.h"
#define JNICALL

const int CRYPTO_SEED_BYTES = 96;
const int CRYPTO_EXPANDED_SEED_BYTES = 160;

const int CRYPTO_MESSAGE_LEN = 32;
const int CRYPTO_SECRETKEY_BYTES = 64 + 2560 + 1312 + 128; //4064
const int CRYPTO_PUBLICKEY_BYTES = 32 + 1312 + 64; //1408
const int CRYPTO_COMPACT_SIGNATURE_BYTES = 2 + 64 + 2420 + 40 + CRYPTO_MESSAGE_LEN; //2558

extern "C" JNIEXPORT jobjectArray   JNICALL
Java_com_dpwallet_app_hybrid_HybridPqcJNIImpl_KeypairSeed(JNIEnv* env, jobject, jintArray  expandedSeedArray)
{
    jint expandedSeedArraylen = env->GetArrayLength(expandedSeedArray);

    if (expandedSeedArraylen != CRYPTO_EXPANDED_SEED_BYTES) {
       return nullptr;
    }

    unsigned char *sk = (unsigned char*) malloc(CRYPTO_SECRETKEY_BYTES * sizeof(1));
    unsigned char *pk = (unsigned char*) malloc(CRYPTO_PUBLICKEY_BYTES * sizeof(1));
    unsigned char *seed = (unsigned char*) malloc(CRYPTO_EXPANDED_SEED_BYTES * sizeof(1));

    jint *expandedSeed = env->GetIntArrayElements(expandedSeedArray, NULL);

    for (int i = 0; i < expandedSeedArraylen; i++)
    {
        seed[i] = (unsigned char)expandedSeed[i];
    }

    crypto_sign_dilithium_ed25519_sphincs_keypair_seed(pk, sk, seed);

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

    memset(seed, 0, CRYPTO_EXPANDED_SEED_BYTES);
    free(seed);

    //Release string
    env->ReleaseIntArrayElements(expandedSeedArray, expandedSeed, 0);

    return keyArray; // return java string key array
}

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

    unsigned char *sm = (unsigned char*) malloc(CRYPTO_COMPACT_SIGNATURE_BYTES * sizeof(1));
    unsigned long long *smlen = (unsigned long long*) malloc(CRYPTO_COMPACT_SIGNATURE_BYTES * sizeof(8));

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
    for (int i = 0; i < CRYPTO_COMPACT_SIGNATURE_BYTES; i++) {
        sig << (int)sm[i] << ",";
    }

    std::string sign = sig.str();

    //memory free
    memset(m, 0, CRYPTO_MESSAGE_LEN);
    free(m);
    memset(sk, 0, CRYPTO_SECRETKEY_BYTES);
    free(sk);
    memset(sm, 0, CRYPTO_COMPACT_SIGNATURE_BYTES);
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
    unsigned char *sm = (unsigned char*) malloc(CRYPTO_COMPACT_SIGNATURE_BYTES * sizeof(1));
    unsigned char *pk = (unsigned char*) malloc(CRYPTO_PUBLICKEY_BYTES * sizeof(1)) ;

    jint *msg = env->GetIntArrayElements(message, NULL);
    jint *sig = env->GetIntArrayElements(sign, NULL);
    jint *pKey = env->GetIntArrayElements(pkKey, NULL);

    for (int i = 0; i < msglen; i++)
    {
        m[i] = (unsigned char)msg[i];
    }

    for (int i = 0; i < CRYPTO_COMPACT_SIGNATURE_BYTES; i++)
    {
        sm[i] = (unsigned char)sig[i];
    }

    for (int i = 0; i < CRYPTO_PUBLICKEY_BYTES; i++)
    {
        pk[i] = (unsigned char)pKey[i];
    }


    int v = crypto_verify_compact_dilithium_ed25519_sphincs(m, CRYPTO_MESSAGE_LEN, sm, CRYPTO_COMPACT_SIGNATURE_BYTES, pk);

    //memory free
    memset(m, 0, CRYPTO_MESSAGE_LEN);
    free(m);
    memset(sm, 0, CRYPTO_COMPACT_SIGNATURE_BYTES);
    free(sm);
    memset(pk, 0, CRYPTO_PUBLICKEY_BYTES);
    free(pk);

    //Release string
    env->ReleaseIntArrayElements(message, msg, 0);
    env->ReleaseIntArrayElements(sign, sig, 0);
    env->ReleaseIntArrayElements(pkKey, pKey, 0);

    return v;
}


extern "C" JNIEXPORT jobjectArray   JNICALL
Java_com_dpwallet_app_hybrid_HybridPqcJNIImpl_SeedExpander(JNIEnv* env, jobject, jintArray  seedArray)
{
    jint seedArraylen = env->GetArrayLength(seedArray);

    if (seedArray == NULL || seedArraylen != CRYPTO_SEED_BYTES) {
        return nullptr;
    }

    unsigned char *seed = (unsigned char*) malloc(CRYPTO_SEED_BYTES * sizeof(1));
    unsigned char *expandedSeed = (unsigned char*) malloc(CRYPTO_EXPANDED_SEED_BYTES * sizeof(1));

    jint *s = env->GetIntArrayElements(seedArray, NULL);

    for (int i = 0; i < seedArraylen; i++)
    {
        seed[i] = (unsigned char)s[i];
    }

    crypto_sign_dilithium_ed25519_sphincs_keypair_seed_expander(seed, expandedSeed);

    jstring expandedkey;

    std::ostringstream oexpanded;
    for (int i = 0; i < CRYPTO_EXPANDED_SEED_BYTES; i++) {
        oexpanded <<(int)expandedSeed[i] << ",";
    }

    std::string ekey = oexpanded.str();

    expandedkey =  env->NewStringUTF(ekey.c_str());

    jclass keyClass;
    keyClass = env->FindClass("java/lang/String");

    jobjectArray keyArray;
    keyArray = env->NewObjectArray(2, keyClass, NULL); // create java string key array

    env->SetObjectArrayElement(keyArray, 0, expandedkey);

    //memory free
    memset(seed, 0, CRYPTO_SEED_BYTES);
    free(seed);

    memset(expandedSeed, 0, CRYPTO_EXPANDED_SEED_BYTES);
    free(expandedSeed);

    //Release string
    env->ReleaseIntArrayElements(seedArray, s, 0);

    return keyArray; // return java string key array
}


extern "C" JNIEXPORT jobjectArray   JNICALL
Java_com_dpwallet_app_hybrid_HybridPqcJNIImpl_Random(JNIEnv* env, jobject)
{
    unsigned char *rand = (unsigned char*) malloc(CRYPTO_SEED_BYTES * sizeof(1));

    randombytes(rand, CRYPTO_SEED_BYTES);

    jstring randkey;

    std::ostringstream orand;
    for (int i = 0; i < CRYPTO_SEED_BYTES; i++) {
        orand <<(int)rand[i] << ",";
    }

    std::string rkey = orand.str();

    randkey =  env->NewStringUTF(rkey.c_str());

    jclass keyClass;
    keyClass = env->FindClass("java/lang/String");

    jobjectArray keyArray;
    keyArray = env->NewObjectArray(2, keyClass, NULL); // create java string key array

    env->SetObjectArrayElement(keyArray, 0, randkey);

    //memory free
    memset(rand, 0, CRYPTO_SEED_BYTES);
    free(rand);

    return keyArray; // return java string key array
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
Java_com_wallet_app_hybrid_HybridPqcJNIImpl_Scrypt(JNIEnv* env, jobject ,
                                                                 jintArray skKey, jintArray salt)
{
    jint saltlen = env->GetArrayLength(salt);

    char *sk = (char*) malloc(CRYPTO_SECRETKEY_BYTES * sizeof(1));
    char *s = (char*) malloc(saltlen * sizeof(1));

    jint *sKey = env->GetIntArrayElements(skKey, NULL);
    jint *sal = env->GetIntArrayElements(salt, NULL);

    for (int i = 0; i < CRYPTO_SECRETKEY_BYTES; i++)
    {
        sk[i] = (char)(sKey[i]);
    }

    for (int i = 0; i < saltlen; i++)
    {
        s[i] = (char)(sal[i]);
    }

    Scrypt_return r= Scrypt(const_cast<char*>(sk), const_cast<char*>(s),
                            CRYPTO_SECRETKEY_BYTES);

    jstring result = env->NewStringUTF(r.r0);
    jstring error = env->NewStringUTF(r.r1);

    jclass keyClass;
    keyClass = env->FindClass("java/lang/String");

    jobjectArray keyArray;
    keyArray = env->NewObjectArray(2, keyClass, NULL); // create java string key array

    env->SetObjectArrayElement(keyArray, 0, result);
    env->SetObjectArrayElement(keyArray, 1, error);

    memset(sk, 0, CRYPTO_SECRETKEY_BYTES);
    free(sk);

    memset(s, 0, saltlen);
    free(s);

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

    env->ReleaseIntArrayElements(skKey, sKey, 0);
    env->ReleaseIntArrayElements(salt, sal, 0);

    return keyArray;
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
Java_com_wallet_app_hybrid_HybridPqcJNIImpl_IsValidAddress(JNIEnv* env, jobject ,
                                                           jstring address)
{
    jint addresslen = env->GetStringLength(address);

    jboolean isCopy;
    char *a = const_cast<char *>(env->GetStringUTFChars(address, &isCopy));

    IsValidAddress_return r= IsValidAddress(const_cast<char*>(a));

    jstring result = env->NewStringUTF(r.r0);
    jstring error = env->NewStringUTF(r.r1);

    jclass keyClass;
    keyClass = env->FindClass("java/lang/String");

    jobjectArray keyArray;
    keyArray = env->NewObjectArray(2, keyClass, NULL); // create java string key array

    env->SetObjectArrayElement(keyArray, 0, result);
    env->SetObjectArrayElement(keyArray, 1, error);

    memset(a, 0, addresslen);
    free(a);

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

    env->ReleaseStringUTFChars(address, a);

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
    char *sm = (char*) malloc(CRYPTO_COMPACT_SIGNATURE_BYTES * sizeof(1));
    for (int i = 0; i < CRYPTO_COMPACT_SIGNATURE_BYTES; i++)
    {
        sm[i] = (char)sig[i];
    }

    TxHash_return r = TxHash(f, n, t, v, gl, d, c,
                             pk, sm,  CRYPTO_PUBLICKEY_BYTES, CRYPTO_COMPACT_SIGNATURE_BYTES);

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

    memset(sm, 0, CRYPTO_COMPACT_SIGNATURE_BYTES);
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
    char *sm = (char*) malloc(CRYPTO_COMPACT_SIGNATURE_BYTES * sizeof(1));
    for (int i = 0; i < CRYPTO_COMPACT_SIGNATURE_BYTES; i++)
    {
        sm[i] = (char)sig[i];
    }

    TxData_return r = TxData(f, n, t, v, gl, d, c,
                             pk, sm,  CRYPTO_PUBLICKEY_BYTES, CRYPTO_COMPACT_SIGNATURE_BYTES);

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

    memset(sm, 0, CRYPTO_COMPACT_SIGNATURE_BYTES);
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


static jobjectArray make_row(JNIEnv *env, jsize count, const char* elements[])
{
    jclass keyClass = env->FindClass("java/lang/String");
    jobjectArray row = env->NewObjectArray( count, keyClass, 0);
    jsize i;

    for (i = 0; i < count; ++i) {
        jstring result = env->NewStringUTF(elements[i]);
        env->SetObjectArrayElement( row, i, result);
    }
    return row;
}


extern "C" JNIEXPORT jobjectArray JNICALL
Java_com_wallet_app_hybrid_HybridPqcJNIImpl_ContractData(JNIEnv* env, jobject ,
                                                         jstring method, jstring abidata, jstring argument1, jstring argument2)
{
    const jsize NumColumns = 1;
    const jsize NumRows = 4;

    jboolean isCopy;

    char *m = const_cast<char *>(env->GetStringUTFChars(method, &isCopy)) ;
    char *a = const_cast<char *>(env->GetStringUTFChars(abidata, &isCopy));
    char *arg1 = const_cast<char *>(env->GetStringUTFChars(argument1, &isCopy));
    char *arg2 = const_cast<char *>(env->GetStringUTFChars(argument2, &isCopy));

    const char* methods[] = {reinterpret_cast<const char *>(env->NewStringUTF(m))};
    const char* abiDatas[] = {reinterpret_cast<const char *>(env->NewStringUTF(a))};
    const char* arguments1[] = {reinterpret_cast<const char *>(env->NewStringUTF(arg1)) };
    const char* arguments2[] = { reinterpret_cast<const char *>(env->NewStringUTF(arg2))};

    jobjectArray argv1 = make_row(env, NumColumns, methods);
    jobjectArray argv2 = make_row(env, NumColumns, abiDatas);
    jobjectArray argv3 = make_row(env, NumColumns, arguments1);
    jobjectArray argv4 = make_row(env, NumColumns, arguments2);

    jobjectArray args = env->NewObjectArray( NumRows, env->GetObjectClass( argv1), 0);

    env->SetObjectArrayElement(args, 0,  argv1);
    env->SetObjectArrayElement(args, 1, argv2);
    env->SetObjectArrayElement(args, 2, argv3);
    env->SetObjectArrayElement(args, 3, argv4);

    ContractData_return r = ContractData(reinterpret_cast<char **>(args), NumRows);

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

    env->ReleaseStringUTFChars(method, m);
    env->ReleaseStringUTFChars(abidata, a);
    env->ReleaseStringUTFChars(argument1, arg1);
    env->ReleaseStringUTFChars(argument2, arg2);

    memset(methods, 0, sizeof(strlen(methods[0])));
    free(methods);

    memset(abiDatas, 0, sizeof(strlen(abiDatas[0])));
    free(abiDatas);

    memset(arguments1, 0, sizeof(strlen(arguments1[0])));
    free(arguments1);

    memset(arguments2, 0, sizeof(strlen(arguments2[0])));
    free(arguments2);

    env->ReleaseStringUTFChars(reinterpret_cast<jstring>(argv1),
                               reinterpret_cast<const char *>(methods));
    env->ReleaseStringUTFChars(reinterpret_cast<jstring>(argv2),
                               reinterpret_cast<const char *>(abiDatas));
    env->ReleaseStringUTFChars(reinterpret_cast<jstring>(argv3),
                               reinterpret_cast<const char *>(arguments1));
    env->ReleaseStringUTFChars(reinterpret_cast<jstring>(argv4),
                               reinterpret_cast<const char *>(arguments2));
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
Java_com_wallet_app_hybrid_HybridPqcJNIImpl_ParseBigFloatInner(JNIEnv* env, jobject ,
                                                          jstring quantity)
{
    jboolean isCopy;
    char *q = const_cast<char *>(env->GetStringUTFChars(quantity, &isCopy));

    ParseBigFloatInner_return r= ParseBigFloatInner(const_cast<char*>(q));
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


