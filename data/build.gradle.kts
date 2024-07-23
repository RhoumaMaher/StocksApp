plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.rhouma.data"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("String", "BASE_URL", "\"https://apidojo-yahoo-finance-v1.p.rapidapi.com/\"")
            buildConfigField("String", "API_KEY", "\"20f9a06a66msh39db6e9f2f5b115p10e18djsncdc0d357ccfc\"")
        }
        debug {
            buildConfigField("String", "BASE_URL", "\"https://apidojo-yahoo-finance-v1.p.rapidapi.com/\"")
            buildConfigField("String", "API_KEY", "\"20f9a06a66msh39db6e9f2f5b115p10e18djsncdc0d357ccfc\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(project(":domain"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    testImplementation(libs.couroutines.test)
    testImplementation(libs.mockito.core)
    testImplementation(libs.jupiter.api)
    testRuntimeOnly(libs.jupiter.engine)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Dagger Hilt
    implementation(libs.dagger.hilt.android)
    ksp(libs.dagger.hilt.android.compiler)
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.dagger.hilt.android.compiler)

    // Retrofit
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.converter.gson)

    //Logging Interceptor
    implementation(libs.squareup.logging.interceptor)


}