package com.test.mandiri.news.feature.news.articledetail

import android.content.Intent
import com.test.mandiri.news.core.router.BaseRouteHandler
import com.test.mandiri.news.core.router.api.Signal
import javax.inject.Inject

class ArticleDetailRouteHandler @Inject constructor() : BaseRouteHandler() {

    override val paths: Set<String> = setOf(PATH)

    override fun handle(signal: Signal) {
        val intent = Intent(signal.context, ArticleDetailActivity::class.java).apply {
            putExtra(ArticleDetailActivity.EXTRA_URL, signal.query[ArticleDetailActivity.EXTRA_URL]?.toString() ?: "")
            putExtra(ArticleDetailActivity.EXTRA_TITLE, signal.query[ArticleDetailActivity.EXTRA_TITLE]?.toString() ?: "")
        }
        signal.transmitter.transmit(intent)
    }

    companion object {
        const val PATH = "/news/article-detail"
    }
}
