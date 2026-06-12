package com.test.mandiri.news.data.news.repository

import com.test.mandiri.news.common.model.ErrorResponse
import com.test.mandiri.news.common.model.Resource
import com.test.mandiri.news.core.network.safeApiCall
import com.test.mandiri.news.data.news.remote.NewsApiService
import com.test.mandiri.news.data.news.remote.model.toNewsArticle
import com.test.mandiri.news.data.news.remote.model.toNewsSource
import com.test.mandiri.news.domain.news.model.NewsArticle
import com.test.mandiri.news.domain.news.model.NewsSource
import com.test.mandiri.news.domain.news.model.PaginatedResult
import com.test.mandiri.news.domain.news.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val service: NewsApiService
) : NewsRepository {

    override suspend fun getSourcesByCategory(category: String): Resource<List<NewsSource>> =
        safeApiCall({ service.getSources(category = category) }) { body ->
            if (body.status == "ok") Resource.Completed(body.sources?.mapNotNull { it.toNewsSource() } ?: emptyList())
            else Resource.Error(ErrorResponse(message = body.message ?: "Unknown error"))
        }

    override suspend fun getArticlesBySource(
        sourceId: String,
        page: Int,
        pageSize: Int
    ): Resource<PaginatedResult<NewsArticle>> =
        safeApiCall({ service.getArticlesBySource(sources = sourceId, page = page, pageSize = pageSize) }) { body ->
            if (body.status == "ok") Resource.Completed(
                PaginatedResult(
                    items = body.articles?.mapNotNull { it.toNewsArticle() } ?: emptyList(),
                    totalResults = body.totalResults ?: 0,
                    currentPage = page,
                    pageSize = pageSize
                )
            ) else Resource.Error(ErrorResponse(message = body.message ?: "Unknown error"))
        }

    override suspend fun searchSources(query: String, category: String): Resource<List<NewsSource>> =
        safeApiCall({ service.getSources(category = category.ifBlank { null }) }) { body ->
            if (body.status == "ok") Resource.Completed(
                body.sources
                    ?.mapNotNull { it.toNewsSource() }
                    ?.filter { it.name.contains(query, ignoreCase = true) || it.description.contains(query, ignoreCase = true) }
                    ?: emptyList()
            ) else Resource.Error(ErrorResponse(message = body.message ?: "Unknown error"))
        }

    override suspend fun searchArticles(
        query: String,
        page: Int,
        pageSize: Int
    ): Resource<PaginatedResult<NewsArticle>> =
        safeApiCall({ service.searchArticles(query = query, page = page, pageSize = pageSize) }) { body ->
            if (body.status == "ok") Resource.Completed(
                PaginatedResult(
                    items = body.articles?.mapNotNull { it.toNewsArticle() } ?: emptyList(),
                    totalResults = body.totalResults ?: 0,
                    currentPage = page,
                    pageSize = pageSize
                )
            ) else Resource.Error(ErrorResponse(message = body.message ?: "Unknown error"))
        }
}
