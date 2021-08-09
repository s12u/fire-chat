plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    id("dagger.hilt.android.plugin")
    id("org.jlleitschuh.gradle.ktlint") version Versions.KTLINT
}

android {
    compileSdk = Versions.COMPILE_SDK
    buildToolsVersion = Versions.BUILD_TOOLS

    defaultConfig {
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK
        versionCode = Versions.VERSION_CODE
        versionName = Versions.VERSION_NAME
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.incremental"] = "true"
            }
        }

        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("debug") {}
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    lint {
        isCheckDependencies = true
    }

    buildFeatures {
        dataBinding = true
    }
}
dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Libs.CORE_KTX)

    // kotlin
    implementation(Libs.KOTLIN_STDLIB)
    implementation(Libs.KOTLIN_REFLECT)
    implementation(Libs.COROUTINES)
    implementation(Libs.COROUTINES_GOOGLE_PLAY)
    testImplementation(Libs.COROUTINES_TEST)

    // UI
    implementation(Libs.APPCOMPAT)
    implementation(Libs.FRAGMENT_KTX)
    implementation(Libs.CONSTRAINT_LAYOUT)
    implementation(Libs.VIEWPAGER2)
    implementation(Libs.MATERIAL_COMPONENTS)
    implementation(Libs.ANDROIDX_BROWSER)

    // Architecture components
    testImplementation(Libs.ARCH_TESTING)
    implementation(Libs.LIFECYCLE_LIVEDATA_KTX)
    implementation(Libs.LIFECYCLE_VIEWMODEL_KTX)
    implementation(Libs.LIFECYCLE_SAVEDSTATE)

    implementation(Libs.ROOM_RUNTIME)
    kapt(Libs.ROOM_COMPILER)
    implementation(Libs.ROOM_KTX)

    implementation(Libs.NAVIGATION_UI_KTX)
    implementation(Libs.NAVIGATION_FRAGMENT_KTX)

    implementation(Libs.PAGING_RUNTIME_KTX)
    implementation(Libs.PAGING_COMMON_KTX)

    // Dagger Hilt
    implementation(Libs.HILT_ANDROID)
    implementation(Libs.HILT_VIEWMODEL)
    androidTestImplementation(Libs.HILT_TESTING)
    kapt(Libs.HILT_COMPILER)
    kapt(Libs.ANDROIDX_HILT_COMPILER)
    kaptAndroidTest(Libs.HILT_COMPILER)
    kaptAndroidTest(Libs.ANDROIDX_HILT_COMPILER)

    // Firebase
    implementation(Libs.FIREBASE_AUTH_KTX)
    implementation(Libs.FIREBASE_FIRESTORE_KTX)
    implementation(Libs.FIREBASE_STORAGE_KTX)

    // Glide
    implementation(Libs.GLIDE)
    implementation(Libs.GLIDE_COMPILER)

    // ETC
    implementation(Libs.TIMBER)
    implementation(Libs.FLOW_BINDING)
    coreLibraryDesugaring(Libs.DESUGAR_LIBS)

    // Instumention tests
    androidTestImplementation(Libs.ESPRESSO_CORE)
    androidTestImplementation(Libs.EXT_JUNIT_KTX)
    androidTestImplementation(Libs.TEST_RUNNER)
    androidTestImplementation(Libs.TEST_RULES)

    // Unit tests
    testImplementation(Libs.JUNIT)
    testImplementation(Libs.MOCKITO_CORE)
    testImplementation(Libs.MOCKITO_KOTLIN)
    testImplementation(Libs.HAMCREST)
}

apply(plugin = "com.google.gms.google-services")
