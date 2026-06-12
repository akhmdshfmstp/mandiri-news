package com.test.mandiri.news.core.network

import com.test.mandiri.news.common.model.ErrorResponse
import com.test.mandiri.news.common.model.Resource
import retrofit2.Response

suspend fun <T, R> safeApiCall(
    apiCall: suspend () -> Response<T>,
    onSuccess: (T) -> Resource<R>
): Resource<R> = try {
    val response = apiCall()
    if (response.isSuccessful) {
        response.body()?.let { onSuccess(it) }
            ?: Resource.Error(ErrorResponse(message = "Empty response"))
    } else {
        Resource.Error(ErrorResponse(message = "HTTP ${response.code()}: ${response.message()}"))
    }
} catch (e: Exception) {
    Resource.Error(ErrorResponse(message = e.message ?: "Network error"))
}
