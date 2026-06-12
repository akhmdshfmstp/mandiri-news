package com.test.mandiri.news.feature.news.sources

import android.content.Intent
import com.test.mandiri.news.core.router.BaseRouteHandler
import com.test.mandiri.news.core.router.api.Signal
import javax.inject.Inject

class SourcesRouteHandler @Inject constructor() : BaseRouteHandler() {

    override val paths: Set<String> = setOf(PATH)

    override fun handle(signal: Signal) {
        val intent = Intent(signal.context, SourcesActivity::class.java).apply {
            putExtra(SourcesActivity.EXTRA_CATEGORY_ID, signal.query[SourcesActivity.EXTRA_CATEGORY_ID]?.toString() ?: "")
            putExtra(SourcesActivity.EXTRA_CATEGORY_NAME, signal.query[SourcesActivity.EXTRA_CATEGORY_NAME]?.toString() ?: "")
        }
        signal.transmitter.transmit(intent)
    }

    companion object {
        const val PATH = "/news/sources"
    }
}
