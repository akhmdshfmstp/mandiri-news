@Suppress("UnstableApiUsage")
val libs = the<org.gradle.accessors.dm.LibrariesForLibs>()

dependencies {
    /* Kotlin */
    add("implementation", libs.jetbrains.kotlin.stdlib)

    /* AndroidX */
    add("implementation", libs.androidx.core.ktx)
    add("implementation", libs.androidx.appcompat)
    add("implementation", libs.androidx.activity.ktx)
    add("implementation", libs.androidx.constraintlayout)
    add("implementation", libs.androidx.lifecycle.viewmodel)
    add("implementation", libs.androidx.lifecycle.livedata)
    add("implementation", libs.androidx.lifecycle.runtime)
    add("implementation", libs.androidx.multidex)
    add("implementation", libs.androidx.swiperefresh)
    add("implementation", libs.androidx.recyclerview)
    add("implementation", libs.androidx.paging)

    /* Google */
    add("implementation", libs.google.material)
    add("implementation", libs.google.hilt.android)
    add("kapt", libs.google.hilt.compiler)

    /* Jetbrains */
    add("implementation", libs.jetbrains.coroutines.core)
    add("implementation", libs.jetbrains.coroutines.android)

    /* Glide */
    add("implementation", libs.glide)
    add("kapt", libs.glide.compiler)

    /* Airbnb */
    add("implementation", libs.airbnb.lottie)

    /** Testing **/
    add("androidTestImplementation", libs.androidx.test.ext.junit)
    add("androidTestImplementation", libs.androidx.test.espresso)

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
    add("testImplementation", libs.turbine)
}
