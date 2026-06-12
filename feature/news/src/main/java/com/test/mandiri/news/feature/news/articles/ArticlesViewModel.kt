package com.test.mandiri.news.feature.news.articles

import androidx.lifecycle.viewModelScope
import com.test.mandiri.news.common.base.BaseViewModel
import com.test.mandiri.news.common.model.Resource
import com.test.mandiri.news.domain.news.usecase.GetArticlesBySourceUseCase
import com.test.mandiri.news.domain.news.usecase.SearchArticlesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticlesViewModel @Inject constructor(
    private val getArticlesBySourceUseCase: GetArticlesBySourceUseCase,
    private val searchArticlesUseCase: SearchArticlesUseCase,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ArticlesUiState())
    val uiState: StateFlow<ArticlesUiState> = _uiState.asStateFlow()

    private var currentSourceId: String = ""
    private var currentPage = 1
    private var searchQuery: String = ""
    private var searchJob: Job? = null

    fun loadArticles(sourceId: String) {
        currentSourceId = sourceId
        currentPage = 1
        searchQuery = ""
        fetch(reset = true)
    }

    fun loadMore() {
        if (_uiState.value.isLoadingMore || !_uiState.value.hasMore) return
        currentPage++
        fetch(reset = false)
    }

    fun refresh() {
        currentPage = 1
        fetch(reset = true)
    }

    fun search(query: String) {
        searchJob?.cancel()
        if (query.isBlank()) {
            if (searchQuery.isNotBlank()) {
                searchQuery = ""
                loadArticles(currentSourceId)
            }
            return
        }
        searchQuery = query
        searchJob = viewModelScope.launch(exceptionHandler) {
            delay(DEBOUNCE_MS)
            currentPage = 1
            fetch(reset = true)
        }
    }

    private fun fetch(reset: Boolean) {
        viewModelScope.launch(exceptionHandler) {
            if (reset) _uiState.update { it.copy(isLoading = true, error = null) }
            else _uiState.update { it.copy(isLoadingMore = true) }

            val result = if (searchQuery.isBlank()) {
                getArticlesBySourceUseCase(currentSourceId, currentPage)
            } else {
                searchArticlesUseCase(searchQuery, currentPage)
            }

            when (result) {
                is Resource.Completed -> {
                    val paginated = result.data!!
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            articles = if (reset) paginated.items else it.articles + paginated.items,
                            hasMore = paginated.hasMore,
                            error = null,
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            error = result.error?.message ?: "Failed to load articles",
                        )
                    }
                    if (!reset) currentPage--
                }
                else -> Unit
            }
        }
    }

    override fun handleError(throwable: Throwable) {
        _uiState.update {
            it.copy(
                isLoading = false,
                isLoadingMore = false,
                error = throwable.message ?: "Unknown error",
            )
        }
    }

    companion object {
        private const val DEBOUNCE_MS = 400L
    }
}
