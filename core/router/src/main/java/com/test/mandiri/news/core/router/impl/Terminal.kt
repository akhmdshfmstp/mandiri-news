package com.test.mandiri.news.core.router.impl

internal data class Terminal(
    val node: Node,
    val index: Int,
    val parameter: Map<String, String>,
)
