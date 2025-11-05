import java.util.Properties
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}
apply(plugin = "org.jetbrains.kotlin.plugin.parcelize")
// Create a Properties object to read the local.properties file
val localProperties = Properties() // <-- CORRECTED LINE
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}
android {
    namespace = "com.example.newsapplication"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.newsapplication"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        //buildConfigField("String", "NEWS_API_KEY", "\"YOUR_NEWSAPI_KEY\"")
        buildConfigField("String", "API_KEY", localProperties.getProperty("API_KEY", "\"\""))
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            //buildConfigField("String", "API_KEY", localProperties.getProperty("apiKey", "\"\""))
        }
        debug {
            // Expose the API key for the debug build
            //buildConfigField("String", "API_KEY", localProperties.getProperty("apiKey", "\"\""))
        }
    }
    buildFeatures {
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

  

    // Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Retrofit + Gson
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation(libs.gson)
    implementation ("com.squareup.okhttp3:logging-interceptor:4.11.0")


    // Koin for DI
    implementation ("io.insert-koin:koin-android:3.4.0")
    implementation ("io.insert-koin:koin-androidx-compose:3.4.0")


    // Navigation for Compose
    implementation ("androidx.navigation:navigation-compose:2.7.0")


    // Testing
    testImplementation (libs.junit)
    testImplementation ("io.mockk:mockk:1.13.8")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")


    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")

    //coil for image loading
    implementation(libs.coil.compose)

    // Unit Testing
    testImplementation(libs.junit)
    testImplementation("io.mockk:mockk:1.13.11") // For MockK
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0") // For testing coroutines
    testImplementation("app.cash.turbine:turbine:1.1.0") // For testing Flows
    testImplementation("io.insert-koin:koin-test-junit4:3.5.6") // Koin test dependency
    testImplementation("app.cash.turbine:turbine:1.1.0")
}