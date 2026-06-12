package com.test.mandiri.news.feature.news.articles

import com.test.mandiri.news.domain.news.model.NewsArticle

data class ArticlesUiState(
    val articles: List<NewsArticle> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val hasMore: Boolean = false,
)
