package com.test.mandiri.news.core.router.impl

import android.content.Context
import com.test.mandiri.news.core.router.api.Transmitter

internal data class Packet(
    val context: Context,
    val scheme: String,
    val host: String,
    val paths: List<String>,
    val query: Map<String, String>,
    val transmitter: Transmitter,
)
