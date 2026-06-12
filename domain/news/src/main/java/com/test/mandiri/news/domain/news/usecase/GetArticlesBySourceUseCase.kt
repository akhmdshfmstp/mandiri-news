package com.test.mandiri.news.domain.news.usecase

import com.test.mandiri.news.common.model.Resource
import com.test.mandiri.news.domain.news.model.NewsArticle
import com.test.mandiri.news.domain.news.model.PaginatedResult
import com.test.mandiri.news.domain.news.NewsConstants
import com.test.mandiri.news.domain.news.repository.NewsRepository
import javax.inject.Inject

class GetArticlesBySourceUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(
        sourceId: String,
        page: Int,
        pageSize: Int = NewsConstants.PAGE_SIZE
    ): Resource<PaginatedResult<NewsArticle>> =
        repository.getArticlesBySource(sourceId, page, pageSize)
}
