import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("androidx.navigation.safeargs")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.emrekizil.videocallapp"
    compileSdk = 34

    val file = rootProject.file("local.properties")
    val properties = Properties()
    properties.load(FileInputStream(file))

    defaultConfig {
        applicationId = "com.emrekizil.videocallapp"
        minSdk = 21
        buildConfigField("String", "appId", properties.getProperty("appId"))
        buildConfigField("String", "certificate", properties.getProperty("certificate"))
        buildConfigField("String", "channelName", properties.getProperty("channelName"))
        buildConfigField("String", "appKey", properties.getProperty("appKey"))
        buildConfigField("String", "orgName", properties.getProperty("orgName"))
        buildConfigField("String", "appName", properties.getProperty("appName"))
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)


    // Navigation
    implementation("androidx.navigation:navigation-fragment:2.8.1")
    implementation("androidx.navigation:navigation-ui:2.8.1")
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)

    // Agora
    implementation ("io.agora.rtc:full-sdk:4.3.2")
    implementation ("io.agora.rtc:chat-sdk:1.0.9")

    //DataStore
    implementation ("androidx.datastore:datastore-preferences:1.1.1")

    // Hilt
    implementation ("com.google.dagger:hilt-android:2.52")
    kapt ("com.google.dagger:hilt-compiler:2.52")

    //Codec
    implementation("commons-codec:commons-codec:1.16.0")

    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    //Database
    implementation ("androidx.room:room-runtime:2.6.1")
    kapt ("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    //Splash
    implementation ("androidx.core:core-splashscreen:1.0.0")
    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}