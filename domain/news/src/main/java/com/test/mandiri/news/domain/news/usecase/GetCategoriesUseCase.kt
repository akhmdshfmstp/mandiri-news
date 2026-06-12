package com.test.mandiri.news.domain.news.usecase

import com.test.mandiri.news.domain.news.model.NewsCategories
import com.test.mandiri.news.domain.news.model.NewsCategory
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor() {
    operator fun invoke(): List<NewsCategory> = NewsCategories.all
}
