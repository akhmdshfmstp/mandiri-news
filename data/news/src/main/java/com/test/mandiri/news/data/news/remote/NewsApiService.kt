package com.test.mandiri.news.data.news.remote

import com.test.mandiri.news.data.news.remote.model.ArticlesResponse
import com.test.mandiri.news.data.news.remote.model.SourcesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("top-headlines/sources")
    suspend fun getSources(
        @Query("category") category: String? = null,
        @Query("language") language: String? = null
    ): Response<SourcesResponse>

    @GET("top-headlines")
    suspend fun getArticlesBySource(
        @Query("sources") sources: String,
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 20
    ): Response<ArticlesResponse>

    @GET("everything")
    suspend fun searchArticles(
        @Query("q") query: String,
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 20,
        @Query("sortBy") sortBy: String = "publishedAt"
    ): Response<ArticlesResponse>
}
