package com.test.mandiri.news.core.router.impl

import com.test.mandiri.news.core.router.api.RouteHandler

internal data class Node(
    val edges: MutableMap<String, Node>,
    var routeHandler: RouteHandler? = null,
) {
    companion object {
        val default: Node
            get() = Node(
                edges = mutableMapOf(),
                routeHandler = null,
            )
    }
}
