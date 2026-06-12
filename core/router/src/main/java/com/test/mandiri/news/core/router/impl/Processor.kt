package com.test.mandiri.news.core.router.impl

import android.net.Uri
import com.test.mandiri.news.core.router.api.Origin
import com.test.mandiri.news.core.router.api.RouteHandler

internal class Processor {
    private val motor = Motor()

    fun register(routeHandlers: List<RouteHandler>) {
        routeHandlers.forEach(motor::updateGraph)
    }

    fun route(origin: Origin) {
        val packet = createPacket(origin)
        motor.transmit(packet)
    }

    private fun createPacket(origin: Origin): Packet {
        val context = origin.context
        val uri = origin.uri
        val transmitter = origin.transmitter

        val data = Uri.parse(uri)
        val scheme = checkNotNull(data.scheme) { "scheme must not be null" }
        val host = checkNotNull(data.host) { "host must not be null" }

        val paths = data.path
            .orEmpty()
            .trim('/')
            .split('/')
            .dropWhile(String::isBlank)

        val query = data.encodedQuery
            .orEmpty()
            .split('&')
            .filter(String::isNotBlank)
            .associate(::mapQuery)

        return Packet(context, scheme, host, paths, query, transmitter)
    }

    private fun mapQuery(data: String): Pair<String, String> {
        val splitIndex = data.indexOf('=')
        val key = data.take(splitIndex)
        val value = data.takeLast(data.length - splitIndex - 1)

        return key to value
    }
}
