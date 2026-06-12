package com.test.mandiri.news.feature.news.sources

import com.test.mandiri.news.domain.news.model.NewsSource

data class SourcesUiState(
    val sources: List<NewsSource> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val hasMore: Boolean = false,
)
