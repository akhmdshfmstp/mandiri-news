package com.test.mandiri.news.domain.news.model

data class PaginatedResult<T>(
    val items: List<T>,
    val totalResults: Int,
    val currentPage: Int,
    val pageSize: Int
) {
    val hasMore: Boolean get() = (currentPage * pageSize) < totalResults
}
