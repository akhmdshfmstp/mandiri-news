package com.test.mandiri.news.domain.news.usecase

import com.test.mandiri.news.common.model.Resource
import com.test.mandiri.news.domain.news.model.NewsSource
import com.test.mandiri.news.domain.news.repository.NewsRepository
import javax.inject.Inject

class SearchSourcesUseCase @Inject constructor(
    private val repository: NewsRepository
) {
    suspend operator fun invoke(query: String, category: String = ""): Resource<List<NewsSource>> =
        repository.searchSources(query, category)
}
