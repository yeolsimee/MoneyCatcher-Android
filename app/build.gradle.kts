import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
}

val signingProperties = Properties()
readProperties(rootProject.file("signing.properties"), signingProperties)

val _keyAlias = signingProperties.getProperty("key.alias")
val _keyPassword = signingProperties.getProperty("key.password")
val _storePassword = signingProperties.getProperty("store.password")

val _naverClientId = signingProperties.getProperty("naver.client.id")
val _naverClientSecret = signingProperties.getProperty("naver.client.secret")
val _naverClientName = signingProperties.getProperty("naver.client.name")

fun readProperties(propertiesFile: File, properties: Properties) = properties.apply {
    propertiesFile.inputStream().use { fis ->
        load(fis)
    }
}

android {
    namespace = "com.yeolsimee.moneysaving"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.yeolsimee.moneysaving"
        minSdk = 24
        targetSdk = 33
        versionCode = 25
        versionName = "1.0.25"
        ndk.debugSymbolLevel = "FULL"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "OAUTH_CLIENT_ID", "\"$_naverClientId\"")
        buildConfigField("String", "OAUTH_CLIENT_SECRET", "\"$_naverClientSecret\"")
        buildConfigField("String", "OAUTH_CLIENT_NAME", "\"$_naverClientName\"")

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        register("release") {
            keyAlias = _keyAlias
            keyPassword = _keyPassword
            storeFile = file("../keystore/moneysaving_key")
            storePassword = _storePassword
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles("proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }

    kotlin {
        jvmToolchain(17)
    }

    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))

    val composeVersion = "1.4.3"
    val hiltVersion = "2.46.1"

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.compose.material3:material3:1.2.0-alpha02")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")

    // ui
    implementation("androidx.core:core-splashscreen:1.0.1")

    // status bar color
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.25.1")

    // Firebase 로그인, Functions, Dynamic Links
    implementation(platform("com.google.firebase:firebase-bom:32.1.1"))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-functions-ktx")
    implementation("com.google.firebase:firebase-dynamic-links-ktx")

    // Google 로그인
    implementation("com.google.android.gms:play-services-auth:20.5.0")

    // naver
    implementation("com.navercorp.nid:oauth:5.6.0")

    // ViewModel-Compose 사용
    val lifecycleVersion = "2.6.1"
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")

    // hilt, dagger
    implementation("androidx.navigation:navigation-compose:2.6.0")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")

    // Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    // orbit mvi
    implementation("org.orbit-mvi:orbit-compose:6.0.0")
    implementation("org.orbit-mvi:orbit-viewmodel:6.0.0")

    // orbit mvi Tests
    testImplementation("org.orbit-mvi:orbit-test:6.0.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.4.3")

    // kotest
    testImplementation("io.kotest:kotest-runner-junit5:5.6.2")
}

configurations.all {
    exclude("com.google.android.gms:play-services-safetynet")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
