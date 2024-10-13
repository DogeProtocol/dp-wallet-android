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

curl -Lo $PWD/templibs/hybrid-pqc/includes.zip https://github.com/steveharrington10/hybrid-pqc/releases/download/v0.0.11/includes.zip
unzip $PWD/templibs/hybrid-pqc/includes.zip -d $PWD/templibs/hybrid-pqc
echo "508E0F695A5F50CE07805E52AF770B40A71E10F4E775B835263F11B919B1A543 $PWD/templibs/hybrid-pqc/includes.zip" | sha256sum --check  - || exit 1

curl -Lo $PWD/templibs/hybrid-pqc/android-arm64-v8a.tar.gz https://github.com/steveharrington10/hybrid-pqc/releases/download/v0.0.11/android-arm64-v8a.tar.gz
tar -zxf $PWD/templibs/hybrid-pqc/android-arm64-v8a.tar.gz --directory $PWD/templibs/hybrid-pqc//android-arm64-v8a
echo "76f082563cef2890ddfc68fd8d325c0d5256089b21e6a4b78e50afb55cf496b1 $PWD/templibs/hybrid-pqc/android-arm64-v8a.tar.gz" | sha256sum --check  - || exit 1

curl -Lo $PWD/templibs/hybrid-pqc/android-armeabi-v7a.tar.gz https://github.com/steveharrington10/hybrid-pqc/releases/download/v0.0.11/android-armeabi-v7a.tar.gz
tar -zxf $PWD/templibs/hybrid-pqc/android-armeabi-v7a.tar.gz --directory $PWD/templibs/hybrid-pqc/android-armeabi-v7a
echo "73155443bc14687fbe8b8c7366c927345f74202ac7c78e0f0f0c704db7ad47bd $PWD/templibs/hybrid-pqc/android-armeabi-v7a.tar.gz" | sha256sum --check  - || exit 1

curl -Lo $PWD/templibs/hybrid-pqc/android-x86.tar.gz https://github.com/steveharrington10/hybrid-pqc/releases/download/v0.0.11/android-x86.tar.gz
tar -zxf $PWD/templibs/hybrid-pqc/android-x86.tar.gz --directory $PWD/templibs/hybrid-pqc/android-x86
echo "fa91fc25663daa3fd465de8babae39070c4a8d092a142f217548fcb2e3e6213b $PWD/templibs/hybrid-pqc/android-x86.tar.gz" | sha256sum --check  - || exit 1

curl -Lo $PWD/templibs/hybrid-pqc/android-x86_64.tar.gz https://github.com/steveharrington10/hybrid-pqc/releases/download/v0.0.11/android-x86_64.tar.gz
tar -zxf $PWD/templibs/hybrid-pqc/android-x86_64.tar.gz --directory $PWD/templibs/hybrid-pqc/android-x86_64
echo "12ce9f872185c6cdb763a319a17a3ec88943cec99a487f944f53646225b0d3d2 $PWD/templibs/hybrid-pqc/android-x86_64.tar.gz" | sha256sum --check  - || exit 1

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

curl -Lo $PWD/templibs/mobile-go/android-arm64-v8a.tar.gz https://github.com/steveharrington10/go-dp/releases/download/v0.0.26/android-arm64-v8a.tar.gz
tar -zxf $PWD/templibs/mobile-go/android-arm64-v8a.tar.gz --directory $PWD/templibs/mobile-go/android-arm64-v8a
echo "3287e1f8aaf1e81a9a3c6819df84fac5c8cb300549b936813f4aa00322720444 $PWD/templibs/mobile-go/android-arm64-v8a.tar.gz" | sha256sum --check  - || exit 1

curl -Lo $PWD/templibs/mobile-go/android-armeabi-v7a.tar.gz https://github.com/steveharrington10/go-dp/releases/download/v0.0.26/android-armeabi-v7a.tar.gz
tar -zxf $PWD/templibs/mobile-go/android-armeabi-v7a.tar.gz --directory $PWD/templibs/mobile-go/android-armeabi-v7a
echo "d965f064dfa0c819523a8e606ba8f7c7cbb73c9ca275f9f934e5d4497b0ecca9 $PWD/templibs/mobile-go/android-armeabi-v7a.tar.gz" | sha256sum --check  - || exit 1

curl -Lo $PWD/templibs/mobile-go/android-x86.tar.gz https://github.com/steveharrington10/go-dp/releases/download/v0.0.26/android-x86.tar.gz
tar -zxf $PWD/templibs/mobile-go/android-x86.tar.gz --directory $PWD/templibs/mobile-go/android-x86
echo "a19a5f1cd8ac114ae5630bf6af7ef756fea74156d84575e548ea63429349a4c8 $PWD/templibs/mobile-go/android-x86.tar.gz" | sha256sum --check  - || exit 1

curl -Lo $PWD/templibs/mobile-go/android-x86_64.tar.gz https://github.com/steveharrington10/go-dp/releases/download/v0.0.26/android-x86_64.tar.gz
tar -zxf $PWD/templibs/mobile-go/android-x86_64.tar.gz --directory $PWD/templibs/mobile-go/android-x86_64
echo "6d59db9f167fe43ed6359bccbb6d3e58fc14e2c2564b2365fae1655306c1199a $PWD/templibs/mobile-go/android-x86_64.tar.gz" | sha256sum --check  - || exit 1

sudo cp $PWD/templibs/mobile-go/android-arm64-v8a/* $PWD/app/src/main/jniLibs/arm64-v8a/
sudo cp $PWD/templibs/mobile-go/android-armeabi-v7a/* $PWD/app/src/main/jniLibs/armeabi-v7a/
sudo cp $PWD/templibs/mobile-go/android-x86/* $PWD/app/src/main/jniLibs/x86/
sudo cp $PWD/templibs/mobile-go/android-x86_64/* $PWD/app/src/main/jniLibs/x86_64/
