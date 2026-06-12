package com.test.mandiri.news.feature.news.categories

import com.test.mandiri.news.common.base.BaseViewModel
import com.test.mandiri.news.domain.news.model.NewsCategory
import com.test.mandiri.news.domain.news.usecase.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : BaseViewModel() {

    private val _categories = MutableStateFlow<List<NewsCategory>>(emptyList())
    val categories: StateFlow<List<NewsCategory>> = _categories.asStateFlow()

    init {
        _categories.value = getCategoriesUseCase()
    }
}
