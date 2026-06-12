import com.test.mandiri.news.buildSrc.AndroidConfig
import com.test.mandiri.news.buildSrc.applyBuildTypes

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt.android)
}

apply(from = "$rootDir/library_dependencies.gradle.kts")

android {
    compileSdk = AndroidConfig.compileSdk
    namespace = AndroidConfig.applicationId

    signingConfigs {
        getByName("debug") {
            storeFile = file("${System.getProperty("user.home")}/.android/debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    defaultConfig {
        applicationId = AndroidConfig.applicationId
        minSdk = AndroidConfig.minSdkVersion
        targetSdk = AndroidConfig.targetSdkVersion
        versionCode = AndroidConfig.versionCode
        versionName = AndroidConfig.versionName
        multiDexEnabled = true

        testInstrumentationRunner = AndroidConfig.jUnitRunner
        signingConfig = signingConfigs.getByName("debug")
    }

    applyBuildTypes()

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
    }

    lint {
        disable += setOf("NullSafeMutableLiveData", "Instantiatable", "MissingTranslation", "InvalidPackage")
        checkReleaseBuilds = false
        abortOnError = false
    }

    packaging {
        resources.excludes += setOf(
            "META-INF/DEPENDENCIES",
            "META-INF/LICENSE",
            "META-INF/LICENSE.txt",
            "META-INF/license.txt",
            "META-INF/NOTICE",
            "META-INF/NOTICE.txt",
            "META-INF/notice.txt",
            "META-INF/ASL2.0",
            "META-INF/LGPL2.1",
            "META-INF/AL2.0",
            "META-INF/*.kotlin_module"
        )
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

kapt {
    useBuildCache = true
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    implementation(project(":core:router"))
    implementation(project(":core:network"))

    implementation(project(":common-android"))

    implementation(project(":domain:news"))

    implementation(project(":data:news"))

    implementation(project(":feature:news"))
}
