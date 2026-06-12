package com.test.mandiri.news.feature.news.categories

import android.content.Intent
import com.test.mandiri.news.core.router.BaseRouteHandler
import com.test.mandiri.news.core.router.api.Signal
import javax.inject.Inject

class CategoriesRouteHandler @Inject constructor() : BaseRouteHandler() {

    override val paths: Set<String> = setOf(PATH)

    override fun handle(signal: Signal) {
        signal.transmitter.transmit(Intent(signal.context, CategoriesActivity::class.java))
    }

    companion object {
        const val PATH = "/news/categories"
    }
}
