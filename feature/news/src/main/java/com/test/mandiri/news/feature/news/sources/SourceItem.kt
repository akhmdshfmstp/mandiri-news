package com.test.mandiri.news.feature.news.sources

import com.test.mandiri.news.domain.news.model.NewsSource

sealed class SourceItem {
    data class Content(val source: NewsSource) : SourceItem()
    object Loading : SourceItem()
}
