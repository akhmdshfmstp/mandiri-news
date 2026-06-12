package com.test.mandiri.news.domain.news.usecase

import com.test.mandiri.news.common.model.Resource
import com.test.mandiri.news.domain.news.model.NewsSource
import com.test.mandiri.news.domain.news.repository.NewsRepository
import javax.inject.Inject

class GetSourcesByCategoryUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(category: String): Resource<List<NewsSource>> =
        repository.getSourcesByCategory(category)
}
