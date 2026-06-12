plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // Keep in sync with [versions] agp in gradle/libs.versions.toml
    // buildSrc cannot access the version catalog, so this must be updated manually.
    implementation("com.android.tools.build:gradle:8.6.0")
    // Force JavaPoet 1.13.0+ so Hilt Gradle plugin (2.51.1) can call ClassName.canonicalName()
    // AGP 8.x pulls an older JavaPoet transitively; this overrides it on the plugin classpath.
    implementation("com.squareup:javapoet:1.13.0")
}
