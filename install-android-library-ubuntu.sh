#!/bin/bash

apt-get install -qq -y unzip
apt-get update -qq -y

mkdir templibs
mkdir templibs/hybrid-pqc
mkdir templibs/mobile-go

mkdir templibs/hybrid-pqc/android-arm64-v8a
mkdir templibs/hybrid-pqc/android-armeabi-v7a
mkdir templibs/hybrid-pqc/android-x86
mkdir templibs/hybrid-pqc/android-x86_64

mkdir templibs/mobile-go/android-arm64-v8a
mkdir templibs/mobile-go/android-armeabi-v7a
mkdir templibs/mobile-go/android-x86
mkdir templibs/mobile-go/android-x86_64

sudo chmod -R a+rwx $PWD/app/src/main/jniLibs/arm64-v8a
sudo chmod -R a+rwx $PWD/app/src/main/jniLibs/armeabi-v7a
sudo chmod -R a+rwx $PWD/app/src/main/jniLibs/x86
sudo chmod -R a+rwx $PWD/app/src/main/jniLibs/x86_64

sudo rm -r $PWD/app/src/main/jniLibs/arm64-v8a/*
sudo rm -r $PWD/app/src/main/jniLibs/armeabi-v7a/*
sudo rm -r $PWD/app/src/main/jniLibs/x86/*
sudo rm -r $PWD/app/src/main/jniLibs/x86_64/*

curl -Lo $PWD/templibs/hybrid-pqc/includes.zip https://github.com/DogeProtocol/hybrid-pqc/releases/download/v0.1.39/includes.zip
unzip $PWD/templibs/hybrid-pqc/includes.zip -d $PWD/templibs/hybrid-pqc
echo "357BF7878A6910990DF69641815112BCD23CB53FE3DCEBF9930E617E00DBED1D $PWD/templibs/hybrid-pqc/includes.zip" | sha256sum --check  - || exit 1

curl -Lo $PWD/templibs/hybrid-pqc/android-arm64-v8a.tar.gz https://github.com/DogeProtocol/hybrid-pqc/releases/download/v0.1.39/android-arm64-v8a.tar.gz
tar -zxf $PWD/templibs/hybrid-pqc/android-arm64-v8a.tar.gz --directory $PWD/templibs/hybrid-pqc//android-arm64-v8a
echo "e0d5d67850be55a4304f83157c1ecac852994a6afd417d2729f524b1909653e1 $PWD/templibs/hybrid-pqc/android-arm64-v8a.tar.gz" | sha256sum --check  - || exit 1

curl -Lo $PWD/templibs/hybrid-pqc/android-armeabi-v7a.tar.gz https://github.com/DogeProtocol/hybrid-pqc/releases/download/v0.1.39/android-armeabi-v7a.tar.gz
tar -zxf $PWD/templibs/hybrid-pqc/android-armeabi-v7a.tar.gz --directory $PWD/templibs/hybrid-pqc/android-armeabi-v7a
echo "58e78dfa86b897c43bfafce53893e6a9ccab0184bedf1cf5fd21bdd6dcc9dc87 $PWD/templibs/hybrid-pqc/android-armeabi-v7a.tar.gz" | sha256sum --check  - || exit 1

curl -Lo $PWD/templibs/hybrid-pqc/android-x86.tar.gz https://github.com/DogeProtocol/hybrid-pqc/releases/download/v0.1.39/android-x86.tar.gz
tar -zxf $PWD/templibs/hybrid-pqc/android-x86.tar.gz --directory $PWD/templibs/hybrid-pqc/android-x86
echo "528d3a3b046b5312687fb56a31031f0df13c95a9e6c6f7e8b85a8c30262c21af $PWD/templibs/hybrid-pqc/android-x86.tar.gz" | sha256sum --check  - || exit 1

curl -Lo $PWD/templibs/hybrid-pqc/android-x86_64.tar.gz https://github.com/DogeProtocol/hybrid-pqc/releases/download/v0.1.39/android-x86_64.tar.gz
tar -zxf $PWD/templibs/hybrid-pqc/android-x86_64.tar.gz --directory $PWD/templibs/hybrid-pqc/android-x86_64
echo "5335a9e476ae67cf90657a83e6c8e8daadfd0553cf834a7151671cc6c1dc94fa $PWD/templibs/hybrid-pqc/android-x86_64.tar.gz" | sha256sum --check  - || exit 1

sudo cp $PWD/templibs/hybrid-pqc/build/include/dilithium/hybrid.h $PWD/app/src/main/jniLibs/arm64-v8a/hybrid.h
sudo cp $PWD/templibs/hybrid-pqc/build/include/dilithium/hybrid.h $PWD/app/src/main/jniLibs/armeabi-v7a/hybrid.h
sudo cp $PWD/templibs/hybrid-pqc/build/include/dilithium/hybrid.h $PWD/app/src/main/jniLibs/x86/hybrid.h
sudo cp $PWD/templibs/hybrid-pqc/build/include/dilithium/hybrid.h $PWD/app/src/main/jniLibs/x86_64/hybrid.h

sudo cp $PWD/templibs/hybrid-pqc/build/include/common/randombytes.h $PWD/app/src/main/jniLibs/arm64-v8a/randombytes.h
sudo cp $PWD/templibs/hybrid-pqc/build/include/common/randombytes.h $PWD/app/src/main/jniLibs/armeabi-v7a/randombytes.h
sudo cp $PWD/templibs/hybrid-pqc/build/include/common/randombytes.h $PWD/app/src/main/jniLibs/x86/randombytes.h
sudo cp $PWD/templibs/hybrid-pqc/build/include/common/randombytes.h $PWD/app/src/main/jniLibs/x86_64/randombytes.h

sudo cp $PWD/templibs/hybrid-pqc/build/include/common/shake_prng.h $PWD/app/src/main/jniLibs/arm64-v8a/shake_prng.h
sudo cp $PWD/templibs/hybrid-pqc/build/include/common/shake_prng.h $PWD/app/src/main/jniLibs/armeabi-v7a/shake_prng.h
sudo cp $PWD/templibs/hybrid-pqc/build/include/common/shake_prng.h $PWD/app/src/main/jniLibs/x86/shake_prng.h
sudo cp $PWD/templibs/hybrid-pqc/build/include/common/shake_prng.h $PWD/app/src/main/jniLibs/x86_64/shake_prng.h

sudo cp $PWD/templibs/hybrid-pqc/build/include/common/fips202.h $PWD/app/src/main/jniLibs/arm64-v8a/fips202.h
sudo cp $PWD/templibs/hybrid-pqc/build/include/common/fips202.h $PWD/app/src/main/jniLibs/armeabi-v7a/fips202.h
sudo cp $PWD/templibs/hybrid-pqc/build/include/common/fips202.h $PWD/app/src/main/jniLibs/x86/fips202.h
sudo cp $PWD/templibs/hybrid-pqc/build/include/common/fips202.h $PWD/app/src/main/jniLibs/x86_64/fips202.h

sudo cp $PWD/templibs/hybrid-pqc/android-arm64-v8a/libhybridpqc.so $PWD/app/src/main/jniLibs/arm64-v8a/libhybridpqc.so
sudo cp $PWD/templibs/hybrid-pqc/android-armeabi-v7a/libhybridpqc.so $PWD/app/src/main/jniLibs/armeabi-v7a/libhybridpqc.so
sudo cp $PWD/templibs/hybrid-pqc/android-x86/libhybridpqc.so $PWD/app/src/main/jniLibs/x86/libhybridpqc.so
sudo cp $PWD/templibs/hybrid-pqc/android-x86_64/libhybridpqc.so $PWD/app/src/main/jniLibs/x86_64/libhybridpqc.so

curl -Lo $PWD/templibs/mobile-go/android-arm64-v8a.tar.gz https://github.com/DogeProtocol/go-dp/releases/download/v2.0.49/android-arm64-v8a.tar.gz
tar -zxf $PWD/templibs/mobile-go/android-arm64-v8a.tar.gz --directory $PWD/templibs/mobile-go/android-arm64-v8a
echo "b03a6e856229b728652fd1af486875ac54081b8b4c546ef52aa093f0eb3dc0bd $PWD/templibs/mobile-go/android-arm64-v8a.tar.gz" | sha256sum --check  - || exit 1

curl -Lo $PWD/templibs/mobile-go/android-armeabi-v7a.tar.gz https://github.com/DogeProtocol/go-dp/releases/download/v2.0.49/android-armeabi-v7a.tar.gz
tar -zxf $PWD/templibs/mobile-go/android-armeabi-v7a.tar.gz --directory $PWD/templibs/mobile-go/android-armeabi-v7a
echo "430c130d870ea83c7f8ac082b02ceb62ddf8a6d8be669f12baed62fca7645c04 $PWD/templibs/mobile-go/android-armeabi-v7a.tar.gz" | sha256sum --check  - || exit 1

curl -Lo $PWD/templibs/mobile-go/android-x86.tar.gz https://github.com/DogeProtocol/go-dp/releases/download/v2.0.49/android-x86.tar.gz
tar -zxf $PWD/templibs/mobile-go/android-x86.tar.gz --directory $PWD/templibs/mobile-go/android-x86
echo "ca86766e07bb487468c7c16833e45de6909a6dcd1edd6e4e3a9ca61709b901ce $PWD/templibs/mobile-go/android-x86.tar.gz" | sha256sum --check  - || exit 1

curl -Lo $PWD/templibs/mobile-go/android-x86_64.tar.gz https://github.com/DogeProtocol/go-dp/releases/download/v2.0.49/android-x86_64.tar.gz
tar -zxf $PWD/templibs/mobile-go/android-x86_64.tar.gz --directory $PWD/templibs/mobile-go/android-x86_64
echo "22d676b41a725032dce6bf2a4bfd4fcafbcbda84587dd035425b61c6f843593e $PWD/templibs/mobile-go/android-x86_64.tar.gz" | sha256sum --check  - || exit 1

sudo cp $PWD/templibs/mobile-go/android-arm64-v8a/* $PWD/app/src/main/jniLibs/arm64-v8a/
sudo cp $PWD/templibs/mobile-go/android-armeabi-v7a/* $PWD/app/src/main/jniLibs/armeabi-v7a/
sudo cp $PWD/templibs/mobile-go/android-x86/* $PWD/app/src/main/jniLibs/x86/
sudo cp $PWD/templibs/mobile-go/android-x86_64/* $PWD/app/src/main/jniLibs/x86_64/
