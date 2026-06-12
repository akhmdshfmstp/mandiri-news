package com.test.mandiri.news.common.model

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("message") val message: String,
    @SerializedName("code") val code: Int = -1,
    @SerializedName("detail") val detail: String = "",
    @SerializedName("hint") val hint: String = ""
)
