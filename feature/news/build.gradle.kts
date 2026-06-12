import com.test.mandiri.news.buildSrc.AndroidConfig

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.android)
}

apply(from = "$rootDir/feature_dependencies.gradle.kts")

android {
    compileSdk = AndroidConfig.compileSdk
    namespace = "com.test.mandiri.news.feature.news"

    defaultConfig {
        minSdk = AndroidConfig.minSdkVersion
        targetSdk = AndroidConfig.targetSdkVersion
        testInstrumentationRunner = AndroidConfig.jUnitRunner
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    implementation(project(":common-android"))
    implementation(project(":core:router"))
    implementation(project(":domain:news"))

    implementation(libs.coil)
}
