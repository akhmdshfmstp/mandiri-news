package com.test.mandiri.news.common.model

sealed class Resource<T>(val data: T? = null, val error: ErrorResponse? = null) {
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Empty<T>(data: T? = null) : Resource<T>(data)
    class Completed<T>(data: T) : Resource<T>(data)
    class Error<T>(error: ErrorResponse, data: T? = null) : Resource<T>(data, error)
}
