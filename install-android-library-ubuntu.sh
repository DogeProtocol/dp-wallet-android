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

curl -Lo $PWD/templibs/hybrid-pqc/includes.zip https://github.com/DogeProtocol/hybrid-pqc/releases/download/v0.1.23/includes.zip
unzip $PWD/templibs/hybrid-pqc/includes.zip -d $PWD/templibs/hybrid-pqc
echo "DBD4E19F2C6E083897F0C42DD771D4150755FAD9FEB4623CAC891E392557DB14 $PWD/templibs/hybrid-pqc/includes.zip" | sha256sum --check  - || exit 1

curl -Lo $PWD/templibs/hybrid-pqc/android-arm64-v8a.tar.gz https://github.com/DogeProtocol/hybrid-pqc/releases/download/v0.1.23/android-arm64-v8a.tar.gz
tar -zxf $PWD/templibs/hybrid-pqc/android-arm64-v8a.tar.gz --directory $PWD/templibs/hybrid-pqc//android-arm64-v8a
echo "86888ac1323738ca882e00c379adc6ffa06722d207ac6468450fe7a556a8be67 $PWD/templibs/hybrid-pqc/android-arm64-v8a.tar.gz" | sha256sum --check  - || exit 1

curl -Lo $PWD/templibs/hybrid-pqc/android-armeabi-v7a.tar.gz https://github.com/DogeProtocol/hybrid-pqc/releases/download/v0.1.23/android-armeabi-v7a.tar.gz
tar -zxf $PWD/templibs/hybrid-pqc/android-armeabi-v7a.tar.gz --directory $PWD/templibs/hybrid-pqc/android-armeabi-v7a
echo "e0ec9c6e15155e4ec533083e33f0f197e9a5d2412c50bae979e0c8c858166fb8 $PWD/templibs/hybrid-pqc/android-armeabi-v7a.tar.gz" | sha256sum --check  - || exit 1

curl -Lo $PWD/templibs/hybrid-pqc/android-x86.tar.gz https://github.com/DogeProtocol/hybrid-pqc/releases/download/v0.1.23/android-x86.tar.gz
tar -zxf $PWD/templibs/hybrid-pqc/android-x86.tar.gz --directory $PWD/templibs/hybrid-pqc/android-x86
echo "4c7513c1adac7af4371f79544d8d36917a7e04d4a675d3911895f56a73f91818 $PWD/templibs/hybrid-pqc/android-x86.tar.gz" | sha256sum --check  - || exit 1

curl -Lo $PWD/templibs/hybrid-pqc/android-x86_64.tar.gz https://github.com/DogeProtocol/hybrid-pqc/releases/download/v0.1.23/android-x86_64.tar.gz
tar -zxf $PWD/templibs/hybrid-pqc/android-x86_64.tar.gz --directory $PWD/templibs/hybrid-pqc/android-x86_64
echo "4e406a956eba914243a95d67d118f4e82d832cab2f51e87d865507cf06f43541 $PWD/templibs/hybrid-pqc/android-x86_64.tar.gz" | sha256sum --check  - || exit 1

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

sudo cp $PWD/templibs/hybrid-pqc/android-arm64-v8a/libhybridpqc.so $PWD/app/src/main/jniLibs/arm64-v8a/libhybridpqc.so
sudo cp $PWD/templibs/hybrid-pqc/android-armeabi-v7a/libhybridpqc.so $PWD/app/src/main/jniLibs/armeabi-v7a/libhybridpqc.so
sudo cp $PWD/templibs/hybrid-pqc/android-x86/libhybridpqc.so $PWD/app/src/main/jniLibs/x86/libhybridpqc.so
sudo cp $PWD/templibs/hybrid-pqc/android-x86_64/libhybridpqc.so $PWD/app/src/main/jniLibs/x86_64/libhybridpqc.so

curl -Lo $PWD/templibs/mobile-go/android-arm64-v8a.tar.gz https://github.com/steveharrington10/go-dp/releases/download/v0.0.18/android-arm64-v8a.tar.gz
tar -zxf $PWD/templibs/mobile-go/android-arm64-v8a.tar.gz --directory $PWD/templibs/mobile-go/android-arm64-v8a
echo "ce67f0c40427704efaf4238fbe9de93fb1cc532f1ff4c865e45ddde342cc0182 $PWD/templibs/mobile-go/android-arm64-v8a.tar.gz" | sha256sum --check  - || exit 1

curl -Lo $PWD/templibs/mobile-go/android-armeabi-v7a.tar.gz https://github.com/steveharrington10/go-dp/releases/download/v0.0.18/android-armeabi-v7a.tar.gz
tar -zxf $PWD/templibs/mobile-go/android-armeabi-v7a.tar.gz --directory $PWD/templibs/mobile-go/android-armeabi-v7a
echo "0a0423fb0bac81bfe432b5d666ef7fec106dfde60179f8873c996b61957a28e1 $PWD/templibs/mobile-go/android-armeabi-v7a.tar.gz" | sha256sum --check  - || exit 1

curl -Lo $PWD/templibs/mobile-go/android-x86.tar.gz https://github.com/steveharrington10/go-dp/releases/download/v0.0.18/android-x86.tar.gz
tar -zxf $PWD/templibs/mobile-go/android-x86.tar.gz --directory $PWD/templibs/mobile-go/android-x86
echo "810af483ac829203bfd0b86465ed6b5150b964cc95886dbed7c0ad21e41308d4 $PWD/templibs/mobile-go/android-x86.tar.gz" | sha256sum --check  - || exit 1

curl -Lo $PWD/templibs/mobile-go/android-x86_64.tar.gz https://github.com/steveharrington10/go-dp/releases/download/v0.0.18/android-x86_64.tar.gz
tar -zxf $PWD/templibs/mobile-go/android-x86_64.tar.gz --directory $PWD/templibs/mobile-go/android-x86_64
echo "1ef55aa42e0f2f8315f02dab9eb46e96ff63046638cef51973b48ceb036be380 $PWD/templibs/mobile-go/android-x86_64.tar.gz" | sha256sum --check  - || exit 1

sudo cp $PWD/templibs/mobile-go/android-arm64-v8a/* $PWD/app/src/main/jniLibs/arm64-v8a/
sudo cp $PWD/templibs/mobile-go/android-armeabi-v7a/* $PWD/app/src/main/jniLibs/armeabi-v7a/
sudo cp $PWD/templibs/mobile-go/android-x86/* $PWD/app/src/main/jniLibs/x86/
sudo cp $PWD/templibs/mobile-go/android-x86_64/* $PWD/app/src/main/jniLibs/x86_64/
