package com.test.mandiri.news.core.router.impl

import com.test.mandiri.news.core.router.api.Origin

internal data class Request(
    val origin: Origin,
    val onError: (Throwable) -> Unit,
)
