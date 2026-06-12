package com.test.mandiri.news.core.router.api

import android.content.Context

data class Origin(
    val context: Context,
    val uri: String,
    val transmitter: Transmitter,
)
