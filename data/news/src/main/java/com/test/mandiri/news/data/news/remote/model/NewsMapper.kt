package com.test.mandiri.news.data.news.remote.model

import com.test.mandiri.news.domain.news.model.NewsArticle
import com.test.mandiri.news.domain.news.model.NewsSource

fun SourceResponse.toNewsSource(): NewsSource? {
    val sourceId = id ?: return null
    return NewsSource(
        id = sourceId,
        name = name ?: "",
        description = description ?: "",
        url = url ?: "",
        category = category ?: "",
        language = language ?: "",
        country = country ?: ""
    )
}

fun ArticleResponse.toNewsArticle(): NewsArticle? {
    val articleUrl = url?.takeIf { it.isNotBlank() } ?: return null
    val articleTitle = title?.takeIf { it.isNotBlank() && it != "[Removed]" } ?: return null
    return NewsArticle(
        sourceId = source?.id,
        sourceName = source?.name ?: "",
        author = author,
        title = articleTitle,
        description = description,
        url = articleUrl,
        urlToImage = urlToImage,
        publishedAt = publishedAt ?: "",
        content = content
    )
}
