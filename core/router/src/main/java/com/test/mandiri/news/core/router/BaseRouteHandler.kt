package com.test.mandiri.news.core.router

import com.test.mandiri.news.core.router.api.RouteHandler
import com.test.mandiri.news.core.router.impl.INTERNAL_HOST
import com.test.mandiri.news.core.router.impl.INTERNAL_SCHEME

abstract class BaseRouteHandler : RouteHandler {
    override val schemes: Set<String> = setOf(
        INTERNAL_SCHEME,
    )

    override val hosts: Set<String> = setOf(
        INTERNAL_HOST,
    )
}
