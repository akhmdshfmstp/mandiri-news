package com.test.mandiri.news.core.router.api

interface RouteHandler {
    val schemes: Set<String>
    val hosts: Set<String>
    val paths: Set<String>
    fun handle(signal: Signal)
}
