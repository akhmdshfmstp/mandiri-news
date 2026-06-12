# ============================================================
# ProGuard / R8 rules for Mandiri News
# ============================================================

# Preserve source file names and line numbers in stack traces
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Keep generic signatures (needed by Retrofit, Gson)
-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses

# ── Kotlin ───────────────────────────────────────────────────
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings { <fields>; }
-keepclassmembers class kotlin.Metadata { public <methods>; }
# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-dontwarn kotlinx.coroutines.**

# ── Hilt / Dagger ────────────────────────────────────────────
-keep class dagger.hilt.** { *; }
-keep @dagger.hilt.android.HiltAndroidApp class * { *; }
-keep @dagger.hilt.InstallIn class * { *; }
-keep @dagger.hilt.android.AndroidEntryPoint class * { *; }
-keep class * extends dagger.hilt.android.internal.managers.ActivityComponentManager { *; }

# ── Retrofit ─────────────────────────────────────────────────
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-dontwarn retrofit2.**

# ── OkHttp ───────────────────────────────────────────────────
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

# ── Gson ─────────────────────────────────────────────────────
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
# Keep fields annotated with @SerializedName so JSON mapping works after obfuscation
-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# ── JNI – NativeHelper ───────────────────────────────────────
# The native method name must NOT be obfuscated; it must match the JNI symbol
# in native-lib.cpp: Java_com_test_mandiri_news_core_network_helper_NativeHelper_appNativeValues
-keep class com.test.mandiri.news.core.network.helper.NativeHelper {
    native <methods>;
    public static <methods>;
}

# ── Application entry point ──────────────────────────────────
-keep class com.test.mandiri.news.MandiriNewsApp { *; }

# ── Data / model classes used with Gson ──────────────────────
# Keep all classes inside data packages; they must survive serialization
-keep class com.test.mandiri.news.**.data.** { *; }
-keep class com.test.mandiri.news.**.model.** { *; }

# ── ViewModels ───────────────────────────────────────────────
# Hilt generates factories; don't strip ViewModel subclasses
-keep class * extends androidx.lifecycle.ViewModel { *; }
