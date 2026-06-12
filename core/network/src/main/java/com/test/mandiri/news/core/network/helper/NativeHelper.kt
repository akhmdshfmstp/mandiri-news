package com.test.mandiri.news.core.network.helper

import com.test.mandiri.news.core.network.BuildConfig

object NativeHelper {

    init {
        System.loadLibrary("native-lib")
    }

    private external fun appNativeValues(): Map<String, String>

    private fun get(key: String): String {
        return appNativeValues()[key]
            ?: error("Key '$key' not found in native-lib.cpp")
    }

    fun getBaseUrl(): String = if (BuildConfig.DEBUG) get("BASE_DEV_URL") else get("BASE_PROD_URL")

    fun getNewsApiKey(): String = get("NEWS_API_KEY")

}
