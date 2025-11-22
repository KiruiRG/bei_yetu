plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android") version "2.51.1" // ✅ add version
    id("org.jetbrains.kotlin.kapt")

}

android {
    namespace = "com.example.projectdraft"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.projectdraft"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}


    dependencies {
        // --- Compose and Core Libraries ---
        implementation(platform("androidx.compose:compose-bom:2024.04.00"))
        implementation("androidx.compose.ui:ui")
        implementation("androidx.compose.ui:ui-graphics")
        implementation("androidx.compose.ui:ui-tooling-preview")
        implementation("androidx.compose.material3:material3")
        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2")
        implementation(libs.androidx.activity.ktx)
        implementation(libs.material3) // ViewModel integration

        // --- Networking (for product data) ---
        // Use the latest stable versions of Ktor or Retrofit + OkHttp
        val retrofitVersion = "2.11.0"
        val okhttpVersion = "4.12.0"
        implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
        implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
        implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
        implementation("com.squareup.okhttp3:logging-interceptor:$okhttpVersion")

        // --- Dependency Injection (Hilt is recommended for complex apps) ---
        // Use the latest stable versions
        val hiltVersion = "2.51.1"
        implementation("com.google.dagger:hilt-android:$hiltVersion")
        kapt("com.google.dagger:hilt-compiler:$hiltVersion")
        implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

        // --- Firebase (for Authentication and Database) ---
        // Use the Firebase Bill of Materials (BoM) for version management
        implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
        implementation("com.google.firebase:firebase-auth-ktx")
        implementation("com.google.firebase:firebase-firestore-ktx")

        // ... other dependencies

        // For Compose Previews
        implementation("androidx.compose.ui:ui-tooling-preview:1.6.0")
        debugImplementation("androidx.compose.ui:ui-tooling:1.6.0")

    }



