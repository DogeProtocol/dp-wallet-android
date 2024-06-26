# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.18.1)

# Declares and names the project.

project("dpwallet")

add_library(lib_hybridpqc SHARED IMPORTED)
set_property(TARGET lib_hybridpqc PROPERTY IMPORTED_NO_SONAME 1)
set_target_properties(lib_hybridpqc PROPERTIES IMPORTED_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/../jniLibs/${CMAKE_ANDROID_ARCH_ABI}/libhybridpqc.so)

add_library(lib_godp SHARED IMPORTED)
set_property(TARGET lib_godp PROPERTY IMPORTED_NO_SONAME 1)
set_target_properties(lib_godp PROPERTIES IMPORTED_LOCATION ${CMAKE_CURRENT_SOURCE_DIR}/../jniLibs/${CMAKE_ANDROID_ARCH_ABI}/libgodp.so)

include_directories(
        ${CMAKE_CURRENT_SOURCE_DIR}/../jniLibs/${CMAKE_ANDROID_ARCH_ABI}/
        )

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.
add_library( # Sets the name of the library.
        dpwallet

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        hybrid-pqc-jini-libs.cpp)


# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries( # Specifies the target library.
        dpwallet
        lib_hybridpqc
        lib_godp
        # Links the target library to the log library
        # included in the NDK.
        ${log-lib})