package com.test.mandiri.news.data.news.remote.model

import com.google.gson.annotations.SerializedName

data class ArticlesResponse(
    @SerializedName("status") val status: String,
    @SerializedName("totalResults") val totalResults: Int?,
    @SerializedName("articles") val articles: List<ArticleResponse>?,
    @SerializedName("message") val message: String?
)