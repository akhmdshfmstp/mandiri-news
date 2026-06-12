import com.test.mandiri.news.buildSrc.AndroidConfig
import com.android.build.api.dsl.LibraryExtension

@Suppress("UnstableApiUsage")
val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

apply(plugin = libs.plugins.android.library.get().pluginId)
apply(plugin = libs.plugins.kotlin.android.get().pluginId)
apply(plugin = libs.plugins.kotlin.kapt.get().pluginId)
apply(plugin = libs.plugins.hilt.android.get().pluginId)

apply(from = "$rootDir/data_dependencies.gradle.kts")

configure<LibraryExtension> {
    compileSdk = AndroidConfig.compileSdk

    defaultConfig {
        minSdk = AndroidConfig.minSdkVersion
        targetSdk = AndroidConfig.targetSdkVersion
        testInstrumentationRunner = AndroidConfig.jUnitRunner
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = false
        resValues = false
    }
}

