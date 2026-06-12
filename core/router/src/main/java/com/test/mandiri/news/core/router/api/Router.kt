package com.test.mandiri.news.core.router.api

interface Router {
    /** Register available [routeHandlers] into graph. */
    fun register(routeHandlers: List<RouteHandler>)

    /** Route to destination with [origin] information. */
    fun route(
        origin: Origin,
        onError: (Throwable) -> Unit,
    )
}
