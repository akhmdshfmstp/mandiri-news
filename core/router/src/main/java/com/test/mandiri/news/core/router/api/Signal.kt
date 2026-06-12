package com.test.mandiri.news.core.router.api

import android.content.Context

data class Signal(
    val context: Context,
    val parameter: Map<String, Any>,
    val query: Map<String, Any>,
    val transmitter: Transmitter,
)
