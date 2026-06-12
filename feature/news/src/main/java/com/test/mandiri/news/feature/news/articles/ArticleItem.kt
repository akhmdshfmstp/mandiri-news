package com.test.mandiri.news.feature.news.articles

import com.test.mandiri.news.domain.news.model.NewsArticle

sealed class ArticleItem {
    data class Content(val article: NewsArticle) : ArticleItem()
    object Loading : ArticleItem()
}
