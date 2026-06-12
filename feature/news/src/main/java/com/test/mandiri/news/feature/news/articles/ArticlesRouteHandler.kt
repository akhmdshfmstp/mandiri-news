package com.test.mandiri.news.feature.news.articles

import android.content.Intent
import com.test.mandiri.news.core.router.BaseRouteHandler
import com.test.mandiri.news.core.router.api.Signal
import javax.inject.Inject

class ArticlesRouteHandler @Inject constructor() : BaseRouteHandler() {

    override val paths: Set<String> = setOf(PATH)

    override fun handle(signal: Signal) {
        val intent = Intent(signal.context, ArticlesActivity::class.java).apply {
            putExtra(ArticlesActivity.EXTRA_SOURCE_ID, signal.query[ArticlesActivity.EXTRA_SOURCE_ID]?.toString() ?: "")
            putExtra(ArticlesActivity.EXTRA_SOURCE_NAME, signal.query[ArticlesActivity.EXTRA_SOURCE_NAME]?.toString() ?: "")
        }
        signal.transmitter.transmit(intent)
    }

    companion object {
        const val PATH = "/news/articles"
    }
}
