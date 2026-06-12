import com.android.build.api.dsl.LibraryExtension

apply(from = "$rootDir/data_config.gradle.kts")

configure<LibraryExtension> {
    namespace = "com.test.mandiri.news.data.news"

    testOptions {
        unitTests.isReturnDefaultValues = true
    }
}

dependencies {
    add("implementation", project(":common"))
    add("implementation", project(":core:network"))
    add("implementation", project(":domain:news"))
}
