package com.test.mandiri.news.domain.news.repository

import com.test.mandiri.news.common.model.Resource
import com.test.mandiri.news.domain.news.model.NewsArticle
import com.test.mandiri.news.domain.news.model.NewsSource
import com.test.mandiri.news.domain.news.model.PaginatedResult

interface NewsRepository {
    suspend fun getSourcesByCategory(category: String): Resource<List<NewsSource>>
    suspend fun getArticlesBySource(
        sourceId: String,
        page: Int,
        pageSize: Int
    ): Resource<PaginatedResult<NewsArticle>>
    suspend fun searchSources(query: String, category: String = ""): Resource<List<NewsSource>>
    suspend fun searchArticles(
        query: String,
        page: Int,
        pageSize: Int
    ): Resource<PaginatedResult<NewsArticle>>
}
