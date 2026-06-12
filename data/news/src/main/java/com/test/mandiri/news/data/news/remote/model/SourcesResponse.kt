package com.test.mandiri.news.data.news.remote.model

import com.google.gson.annotations.SerializedName

data class SourcesResponse(
    @SerializedName("status") val status: String,
    @SerializedName("sources") val sources: List<SourceResponse>?,
    @SerializedName("message") val message: String?
)

