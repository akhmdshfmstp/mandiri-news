package com.test.mandiri.news.core.router.impl

import com.test.mandiri.news.core.router.api.RouteHandler
import com.test.mandiri.news.core.router.api.Signal
import java.util.LinkedList

internal class Motor {
    private val root: Node = Node.default

    fun updateGraph(routeHandler: RouteHandler) {
        for (path in routeHandler.paths) {
            var node = root

            val segments = path
                .trim(Char::isWhitespace)
                .split('/')
                .filter(String::isNotBlank)

            for (segment in segments) {
                val normalized = segment.trim(Char::isWhitespace)
                val child = node.edges[normalized] ?: Node.default

                node.edges[normalized] = child
                node = child
            }

            node.routeHandler = routeHandler
        }
    }

    fun transmit(packet: Packet) {
        val terminal = determineTerminal(packet)
        checkNotNull(terminal) { "Destination not found" }

        val routeHandler = terminal.node.routeHandler
        val signal = createSignal(packet, terminal)
        routeHandler?.handle(signal)
    }

    private fun determineTerminal(packet: Packet): Terminal? {
        val queue = LinkedList<Terminal>().also { queue ->
            val terminal = Terminal(
                node = root,
                index = 0,
                parameter = mapOf(),
            )
            queue.add(terminal)
        }

        while (queue.isNotEmpty()) {
            val terminal = queue.poll() ?: continue

            val node = terminal.node
            val routeHandler = node.routeHandler

            val index = terminal.index
            val parameter = terminal.parameter

            if (index == packet.paths.size
                && routeHandler != null
                && packet.scheme in routeHandler.schemes.map(String::trim)
                && packet.host in routeHandler.hosts.map(String::trim)
            ) return terminal

            if (index == packet.paths.size) continue

            val path = packet.paths[index]
            val child = node.edges[path]

            if (child != null) {
                val nextTerminal = Terminal(
                    node = child,
                    index = index + 1,
                    parameter = parameter,
                )
                queue.add(nextTerminal)
            } else {
                val candidates = node.edges.filterKeys { key ->
                    key.first() == '<' && key.last() == '>'
                }

                for ((key, vertex) in candidates) {
                    val name = key.filterIndexed { i, c ->
                        (i == 0 && c == '<').not() && (i == key.lastIndex && c == '>').not()
                    }

                    val newParameter = mutableMapOf(name to path)
                    newParameter.putAll(parameter)

                    val nextTerminal = Terminal(
                        node = vertex,
                        index = index + 1,
                        parameter = newParameter,
                    )
                    queue.add(nextTerminal)
                }
            }
        }

        return null
    }

    private fun createSignal(packet: Packet, terminal: Terminal): Signal {
        val autoParsers = packet.query.filterKeys { it.takeLast(3) == "_ap" }

        val parameter = mutableMapOf<String, Any>()
        for ((key, value) in terminal.parameter) {
            val isAutoParsed = (autoParsers["${key}_ap"] ?: "true").toBooleanStrictOrNull() ?: true
            parameter[key] = value.parseType().takeIf { isAutoParsed } ?: value
        }

        val query = mutableMapOf<String, Any>()
        for ((key, value) in packet.query - autoParsers.keys) {
            val isAutoParsed = (autoParsers["${key}_ap"] ?: "true").toBooleanStrictOrNull() ?: true
            query[key] = value.parseType().takeIf { isAutoParsed } ?: value
        }

        return Signal(
            context = packet.context,
            parameter = parameter,
            query = query,
            transmitter = packet.transmitter,
        )
    }
}
