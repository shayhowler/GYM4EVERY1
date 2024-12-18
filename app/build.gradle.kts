import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version("2.1.0")
}

android {
    namespace = "com.gym4every1"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.gym4every1"
        minSdk = 33
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        //load the values from .properties file
        val keystoreFile = project.rootProject.file("apikeys.properties")
        val properties = Properties()
        properties.load(keystoreFile.inputStream())

        buildFeatures {
            buildConfig = true
        }

        //return empty key in case something goes wrong
        val googleClientId = properties.getProperty("GOOGLE_CLIENT_ID") ?: ""
        buildConfigField(
            type = "String",
            name = "GOOGLE_CLIENT_ID",
            value = googleClientId
        )

        val supabaseUrl = properties.getProperty("SUPABASE_URL") ?: ""
        buildConfigField(
            type = "String",
            name = "SUPABASE_URL",
            value = supabaseUrl
        )

        val supabaseAnonKey = properties.getProperty("SUPABASE_ANON_KEY") ?: ""
        buildConfigField(
            type = "String",
            name = "SUPABASE_ANON_KEY",
            value = supabaseAnonKey
        )

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
    // Credentials dependencies
    implementation(libs.androidx.credentials)
    implementation(libs.googleid)
    // For devices running Android 13 and below
    implementation(libs.androidx.credentials.play.services.auth)

    // Supabase dependencies
    implementation(platform("io.github.jan-tennert.supabase:bom:3.0.3"))
    implementation("io.github.jan-tennert.supabase:auth-kt:3.0.3")
    implementation("io.github.jan-tennert.supabase:postgrest-kt:3.0.3")
    implementation("io.github.jan-tennert.supabase:storage-kt:3.0.3")
    implementation("io.github.jan-tennert.supabase:realtime-kt:3.0.3")

    // Ktor client for HTTP requests
    implementation("io.ktor:ktor-client-okhttp:3.0.2")

    // Material 3 for Jetpack Compose
    implementation(libs.androidx.material3)

    // MPAndroidChart for charts
    implementation(libs.mpandroidchart)

    // FontAwesome Compose for icons
    implementation(libs.fontawesomecompose)

    // Navigation Compose for navigation
    implementation(libs.androidx.navigation.compose)

    // Core AndroidX libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // Compose UI libraries
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)

    // Test dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Jetpack Compose testing
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // Debug dependencies
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}