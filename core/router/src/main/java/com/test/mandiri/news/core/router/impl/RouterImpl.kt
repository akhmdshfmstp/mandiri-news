package com.test.mandiri.news.core.router.impl

import com.test.mandiri.news.core.router.api.Origin
import com.test.mandiri.news.core.router.api.RouteHandler
import com.test.mandiri.news.core.router.api.Router

class RouterImpl : Router {
    private val processor = Processor()

    @Volatile
    private var isProcessing: Boolean = false

    @Volatile
    private var deferredRequest: Request? = null

    @Synchronized
    override fun register(routeHandlers: List<RouteHandler>) {
        lockProcess {
            processor.register(routeHandlers)
        }
        processDeferredRequest()
    }

    private inline fun lockProcess(action: () -> Unit) {
        isProcessing = true
        action()
        isProcessing = false
    }

    private fun processDeferredRequest() {
        val request = deferredRequest ?: return
        deferredRequest = null

        route(origin = request.origin, onError = request.onError)
    }

    override fun route(origin: Origin, onError: (Throwable) -> Unit) {
        val request = Request(origin, onError)
        if (isProcessing) {
            deferredRequest = request
            return
        }

        try {
            processor.route(origin)
        } catch (e: Exception) {
            onError(e)
        }
    }
}
