plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)

}

android {
    namespace = "gps.trackerid.location"
    compileSdk = 36

    android {
        signingConfigs {
            create("release") {
                storeFile = file("../app/key_debug")
                storePassword = "gps.trackerid.location"
                keyAlias = "key0"
                keyPassword = "gps.trackerid.location"
            }
        }
    }

    defaultConfig {
        applicationId = "gps.trackerid.location"
        minSdk = 24
        targetSdk = 36
        versionCode = 9
        versionName = "1.0.9"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        signingConfig = signingConfigs.getByName("release")
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isShrinkResources = false

            manifestPlaceholders["ad_app_id"] = "ca-app-pub-3940256099942544~3347511713"
            buildConfigField("String", "inter_splash", "\"ca-app-pub-3940256099942544/1033173712\"")
            buildConfigField("String", "banner_splash", "\"ca-app-pub-3940256099942544/2014213617\"")
            buildConfigField("String", "native_language_1", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "native_language_1_click", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "native_language_2", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "native_language_2_click", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "native_onboarding_1_1", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "native_onboarding_2_1", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "native_onboarding_1_4", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "native_onboarding_2_4", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "native_onboarding_fullscreen_1_2", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "native_onboarding_fullscreen_2_2", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "open_resume", "\"ca-app-pub-3940256099942544/9257395921\"")
            buildConfigField("String", "inter_onboarding", "\"ca-app-pub-3940256099942544/1033173712\"")
            buildConfigField("String", "inter_home", "\"ca-app-pub-3940256099942544/1033173712\"")
            buildConfigField("String", "native_home", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "banner_collap_home", "\"ca-app-pub-3940256099942544/2014213617\"")
            buildConfigField("String", "inter_back", "\"ca-app-pub-3940256099942544/1033173712\"")
            buildConfigField("String", "banner_all", "\"ca-app-pub-3940256099942544/2014213617\"")
            buildConfigField("String", "native_phone_locator", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "inter_splash_uninstall", "\"ca-app-pub-3940256099942544/1033173712\"")
            buildConfigField("String", "banner_splash_uninstall", "\"ca-app-pub-3940256099942544/2014213617\"")
            buildConfigField("String", "native_uninstall", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "native_survey_uninstall", "\"ca-app-pub-3940256099942544/2247696110\"")
            buildConfigField("String", "native_setting", "\"ca-app-pub-3940256099942544/2247696110\"")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true

            manifestPlaceholders["ad_app_id"] = "ca-app-pub-2864154863223892~9366593122"
            buildConfigField("String", "inter_splash", "\"ca-app-pub-2864154863223892/1887558354\"")
            buildConfigField("String", "banner_splash", "\"ca-app-pub-2864154863223892/9574476683\"")
            buildConfigField("String", "native_language_1", "\"ca-app-pub-2864154863223892/1481002276\"")
            buildConfigField("String", "native_language_1_click", "\"ca-app-pub-2864154863223892/9167920602\"")
            buildConfigField("String", "native_language_2", "\"ca-app-pub-2864154863223892/4186418638\"")
            buildConfigField("String", "native_language_2_click", "\"ca-app-pub-2864154863223892/3349863581\"")
            buildConfigField("String", "native_onboarding_1_1", "\"ca-app-pub-2864154863223892/5213609593\"")
            buildConfigField("String", "native_onboarding_2_1", "\"ca-app-pub-2864154863223892/9247173622\"")
            buildConfigField("String", "native_onboarding_1_4", "\"ca-app-pub-2864154863223892/5667021269\"")
            buildConfigField("String", "native_onboarding_2_4", "\"ca-app-pub-2864154863223892/4353939592\"")
            buildConfigField("String", "native_onboarding_fullscreen_1_2", "\"ca-app-pub-2864154863223892/3760927121\"")
            buildConfigField("String", "native_onboarding_fullscreen_2_2", "\"ca-app-pub-2864154863223892/9382904998\"")
            buildConfigField("String", "open_resume", "\"ca-app-pub-2864154863223892/9055601933\"")
            buildConfigField("String", "inter_onboarding", "\"ca-app-pub-2864154863223892/2754967211\"")
            buildConfigField("String", "inter_home", "\"ca-app-pub-2864154863223892/7742520266\"")
            buildConfigField("String", "native_home", "\"ca-app-pub-2864154863223892/6195518772\"")
            buildConfigField("String", "banner_collap_home", "\"ca-app-pub-2864154863223892/9414694589\"")
            buildConfigField("String", "inter_back", "\"ca-app-pub-2864154863223892/6429438590\"")
            buildConfigField("String", "banner_all", "\"ca-app-pub-2864154863223892/5443659987\"")
            buildConfigField("String", "native_phone_locator", "\"ca-app-pub-2864154863223892/9128803875\"")
            buildConfigField("String", "inter_splash_uninstall", "\"ca-app-pub-2864154863223892/4162367906\"")
            buildConfigField("String", "banner_splash_uninstall", "\"ca-app-pub-2864154863223892/9606333485\"")
            buildConfigField("String", "native_uninstall", "\"ca-app-pub-2864154863223892/5220258110\"")
            buildConfigField("String", "native_survey_uninstall", "\"ca-app-pub-2864154863223892/5615409167\"")
            buildConfigField("String", "native_setting", "\"ca-app-pub-2864154863223892/1999597789\"")

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    packagingOptions {
        //Important to add when using oneconnect library
        jniLibs {
            useLegacyPackaging = true
        }
    }
    buildFeatures {
        buildConfig = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("com.tbuonomo:dotsindicator:5.1.0")
    implementation("com.karumi:dexter:6.2.3")
    implementation("com.google.android.gms:play-services-maps:19.2.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.google.android.gms:play-services-places:17.1.0")
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.8.4")
    implementation("androidx.lifecycle:lifecycle-livedata:2.8.4")

    implementation("com.google.dagger:hilt-android:2.51")
    annotationProcessor("com.google.dagger:hilt-compiler:2.51")
    implementation("com.google.zxing:core:3.5.2")
    implementation("com.google.code.gson:gson:2.10.1")

    implementation("io.github.g00fy2.quickie:quickie-bundled:1.11.0")
    // Firebase BOM
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.config)
    implementation(libs.shimmer)
    implementation(libs.play.services.ads)
    implementation(libs.firebase.crashlytics)

    // Firebase Realtime Database
    implementation("com.google.firebase:firebase-database")
    // Firebase Analytics
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.hbb20:ccp:2.7.3")
    implementation ("com.airbnb.android:lottie:4.2.1")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("com.github.trongluan99:ERain-Studio:1.2")

}