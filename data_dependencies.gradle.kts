@Suppress("UnstableApiUsage")
val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

dependencies {
    add("implementation", libs.google.hilt.android)
    add("kapt", libs.google.hilt.compiler)
    add("implementation", libs.google.gson)

    add("implementation", libs.jetbrains.coroutines.core)

    /* Square Up */
    add("implementation", libs.squareup.retrofit)
    add("implementation", libs.squareup.retrofit.gson)
    add("implementation", libs.squareup.retrofit.scalars)
    add("implementation", libs.squareup.okhttp)
    add("implementation", libs.squareup.okhttp.logging)

    /** Testing **/
    add("androidTestImplementation", libs.androidx.test.ext.junit)

    add("testImplementation", libs.androidx.lifecycle.livedata.core)
    add("testImplementation", libs.junit)
    add("testImplementation", libs.mockk)
    add("testImplementation", libs.mockito.core)
    add("testImplementation", libs.mockito.inline)
    add("testImplementation", libs.jetbrains.coroutines.test)
    (add("testImplementation", libs.androidx.core.testing.get()) as org.gradle.api.artifacts.ExternalModuleDependency).apply {
        exclude(group = "com.android.support", module = "support-compat")
        exclude(group = "com.android.support", module = "support-annotations")
        exclude(group = "com.android.support", module = "support-core-utils")
        exclude(group = "org.mockito", module = "mockito-core")
    }
}
