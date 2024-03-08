#!/bin/bash

apt-get install -qq -y unzip
apt-get update -qq -y

mkdir templibs
mkdir templibs/hybrid-pqc
mkdir templibs/mobile-go

//mkdir templibs/hybrid-pqc/android-arm64-v8a
//mkdir templibs/hybrid-pqc/android-armeabi-v7a
//mkdir templibs/hybrid-pqc/android-x86
//mkdir templibs/hybrid-pqc/android-x86_64

//mkdir templibs/mobile-go/android-arm64-v8a
//mkdir templibs/mobile-go/android-armeabi-v7a
//mkdir templibs/mobile-go/android-x86
//mkdir templibs/mobile-go/android-x86_64

rm -rf $PWD/app/src/main/jniLibs/arm64-v8a/
rm -rf $PWD/app/src/main/jniLibs/armeabi-v7a/
rm -rf $PWD/app/src/main/jniLibs/x86/
rm -rf $PWD/app/src/main/jniLibs/x86_64/

curl -Lo $PWD/templibs/hybrid-pqc/includes.zip https://github.com/DogeProtocol/hybrid-pqc/releases/download/v0.1.23/includes.zip
unzip $PWD/templibs/hybrid-pqc/includes.zip -d $PWD/templibs/hybrid-pqc
echo "DBD4E19F2C6E083897F0C42DD771D4150755FAD9FEB4623CAC891E392557DB14 $PWD/templibs/hybrid-pqc/includes.zip" | sha256sum --check  - || exit 1

curl -Lo $PWD/templibs/hybrid-pqc/android-arm64-v8a.tar.gz https://github.com/DogeProtocol/hybrid-pqc/releases/download/v0.1.23/android-arm64-v8a.tar.gz
tar -czf $PWD/templibs/hybrid-pqc/android-arm64-v8a.tar.gz $PWD/templibs/hybrid-pqc/.
echo "86888ac1323738ca882e00c379adc6ffa06722d207ac6468450fe7a556a8be67 $PWD/templibs/hybrid-pqc/android-arm64-v8a.tar.gz" | sha256sum --check  - || exit 1

curl -Lo $PWD/templibs/hybrid-pqc/android-armeabi-v7a.tar.gz https://github.com/DogeProtocol/hybrid-pqc/releases/download/v0.1.23/android-armeabi-v7a.tar.gz
tar -czf $PWD/templibs/hybrid-pqc/android-armeabi-v7a.tar.gz $PWD/hybrid-pqc/.
echo "e0ec9c6e15155e4ec533083e33f0f197e9a5d2412c50bae979e0c8c858166fb8 $PWD/templibs/hybrid-pqc/android-armeabi-v7a.tar.gz" | sha256sum --check  - || exit 1

curl -Lo $PWD/templibs/hybrid-pqc/android-x86.tar.gz https://github.com/DogeProtocol/hybrid-pqc/releases/download/v0.1.23/android-x86.tar.gz
tar -czf $PWD/templibs/hybrid-pqc/android-x86.tar.gz $PWD/templibs/hybrid-pqc/.
echo "4c7513c1adac7af4371f79544d8d36917a7e04d4a675d3911895f56a73f91818 $PWD/templibs/hybrid-pqc/android-x86.tar.gz" | sha256sum --check  - || exit 1

curl -Lo $PWD/templibs/hybrid-pqc/android-x86_64.tar.gz https://github.com/DogeProtocol/hybrid-pqc/releases/download/v0.1.23/android-x86_64.tar.gz
tar -czf $PWD/templibs/hybrid-pqc/android-x86_64.tar.gz $PWD/templibs/hybrid-pqc/.
echo "4e406a956eba914243a95d67d118f4e82d832cab2f51e87d865507cf06f43541 $PWD/templibs/hybrid-pqc/android-x86_64.tar.gz" | sha256sum --check  - || exit 1

ls $PWD/templibs/hybrid-pqc

ls $PWD/templibs/hybrid-pqc/android-arm64-v8a
ls $PWD/templibs/hybrid-pqc/android-armeabi-v7a
ls $PWD/templibs/hybrid-pqc/android-x86
ls $PWD/templibs/hybrid-pqc/android-x86_64

cp $PWD/templibs/hybrid-pqc/includes/build/include/dilithium/hybrid.h $PWD/app/src/main/jniLibs/arm64-v8a/hybrid.h
cp $PWD/templibs/hybrid-pqc/includes/build/include/dilithium/hybrid.h $PWD/app/src/main/jniLibs/armeabi-v7a/hybrid.h
cp $PWD/templibs/hybrid-pqc/includes/build/include/dilithium/hybrid.h $PWD/app/src/main/jniLibs/x86/hybrid.h
cp $PWD/templibs/hybrid-pqc/includes/build/include/dilithium/hybrid.h $PWD/app/src/main/jniLibs/x86_64/hybrid.h



cp $PWD/templibs/hybrid-pqc/android-arm64-v8a/libhybridpqc.so $PWD/app/src/main/jniLibs/arm64-v8a/libhybridpqc.so
cp $PWD/templibs/hybrid-pqc/android-armeabi-v7a/libhybridpqc.so $PWD/app/src/main/jniLibs/armeabi-v7a/libhybridpqc.so
cp $PWD/templibs/hybrid-pqc/android-x86/libhybridpqc.so $PWD/app/src/main/jniLibs/x86/libhybridpqc.so
cp $PWD/templibs/hybrid-pqc/android-x86_64/libhybridpqc.so $PWD/app/src/main/jniLibs/x86_64/libhybridpqc.so

curl -Lo $PWD/templibs/mobile-go/android-arm64-v8a.tar.gz https://github.com/steveharrington10/go-dp/releases/download/v0.0.7/android-arm64-v8a.tar.gz
tar -czf $PWD/templibs/mobile-go/android-arm64-v8a.tar.gz $PWD/templibs/mobile-go/.
echo "d29042cb0aeec07ff31e0de6ab6527f8c4503e816907411289cd4f44b7248367 $PWD/templibs/mobile-go/android-arm64-v8a.tar.gz" | sha256sum --check  - || exit 1

curl -Lo $PWD/templibs/mobile-go/android-armeabi-v7a.tar.gz https://github.com/steveharrington10/go-dp/releases/download/v0.0.7/android-armeabi-v7a.tar.gz
tar -czf $PWD/templibs/mobile-go/android-armeabi-v7a.tar.gz $PWD/templibs/mobile-go/.
echo "ff64c53a14b1a5706a1c107e65a05986ccd305c8d7e5b61c7babc1021104c8ff $PWD/templibs/mobile-go/android-armeabi-v7a.tar.gz" | sha256sum --check  - || exit 1

curl -Lo $PWD/templibs/mobile-go/android-x86.tar.gz https://github.com/steveharrington10/go-dp/releases/download/v0.0.7/android-x86.tar.gz
tar -czf $PWD/templibs/mobile-go/android-x86.tar.gz $PWD/templibs/mobile-go/.
echo "fd70b0124260e92d9fdcad2d609d022757cf556d02eaed9488205c47fc2e1467 $PWD/templibs/mobile-go/android-x86.tar.gz" | sha256sum --check  - || exit 1

curl -Lo $PWD/templibs/mobile-go/android-x86_64.tar.gz https://github.com/steveharrington10/go-dp/releases/download/v0.0.7/android-x86_64.tar.gz
tar -czf $PWD/templibs/mobile-go/android-x86_64.tar.gz $PWD/templibs/mobile-go/.
echo "ca40d57bd95c8eeca8830218f0b88a8bd44cd60b2d0021104ccfa7270420542a $PWD/templibs/mobile-go/android-x86_64.tar.gz" | sha256sum --check  - || exit 1

ls $PWD/templibs/mobile-go

cp $PWD/templibs/mobile-go/android-arm64-v8a/. $PWD/app/src/main/jniLibs/arm64-v8a/
cp $PWD/templibs/mobile-go/android-armeabi-v7a/. $PWD/app/src/main/jniLibs/armeabi-v7a/
cp $PWD/templibs/mobile-go/android-x86/. $PWD/app/src/main/jniLibs/x86/
cp $PWD/templibs/mobile-go/android-x86_64/. $PWD/app/src/main/jniLibs/x86_64/

ls $PWD/app/src/main/jniLibs/arm64-v8a/
ls $PWD/app/src/main/jniLibs/armeabi-v7a/
ls $PWD/app/src/main/jniLibs/x86/
ls $PWD/app/src/main/jniLibs/x86_64/
