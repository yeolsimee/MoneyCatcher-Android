import java.util.Properties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
}

val serverProperties = Properties()
readProperties(rootProject.file("server.properties"), serverProperties)

fun readProperties(propertiesFile: File, properties: Properties) = properties.apply {
    propertiesFile.inputStream().use { fis ->
        load(fis)
    }
}

val mockUrl: String = serverProperties.getProperty("MOCK_URL")
val realUrl: String = serverProperties.getProperty("REAL_URL")

android {
    namespace = "com.yeolsimee.moneysaving.data"
    compileSdk = 33

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "MOCK_URL", "\"$mockUrl\"")
        buildConfigField("String", "REAL_URL", "\"$realUrl\"")
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    kotlin {
        jvmToolchain(17)
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":domain"))

    val hiltVersion = "2.47"
    implementation("androidx.core:core-ktx:1.10.1")

    // Firebase Auth
    implementation(platform("com.google.firebase:firebase-bom:32.1.1"))
    implementation("com.google.firebase:firebase-auth-ktx")

    // Retrofit2
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // hilt, dagger
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")

    // Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // room DB
    val roomVersion = "2.5.2"
    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // datastore
    implementation("androidx.datastore:datastore:1.0.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("androidx.arch.core:core-testing:2.2.0")

    // kotest
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.10")
    testImplementation("io.kotest:kotest-runner-junit5:5.6.2")
    testImplementation("io.mockk:mockk:1.13.5")

}

configurations.all {
    exclude("com.google.android.gms:play-services-safetynet")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
