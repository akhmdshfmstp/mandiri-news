package com.test.mandiri.news.feature.news.sources

import androidx.lifecycle.viewModelScope
import com.test.mandiri.news.common.base.BaseViewModel
import com.test.mandiri.news.common.model.Resource
import com.test.mandiri.news.domain.news.model.NewsSource
import com.test.mandiri.news.domain.news.NewsConstants
import com.test.mandiri.news.domain.news.usecase.GetSourcesByCategoryUseCase
import com.test.mandiri.news.domain.news.usecase.SearchSourcesUseCase
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
class SourcesViewModel @Inject constructor(
    private val getSourcesByCategoryUseCase: GetSourcesByCategoryUseCase,
    private val searchSourcesUseCase: SearchSourcesUseCase,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(SourcesUiState())
    val uiState: StateFlow<SourcesUiState> = _uiState.asStateFlow()

    private var allSources: List<NewsSource> = emptyList()
    private var currentCategory: String = ""
    private var searchQuery: String = ""
    private var currentPage = 0
    private var searchJob: Job? = null

    fun loadSources(category: String) {
        currentCategory = category
        searchQuery = ""
        currentPage = 0
        allSources = emptyList()
        viewModelScope.launch(exceptionHandler) {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = getSourcesByCategoryUseCase(category)) {
                is Resource.Completed -> {
                    allSources = result.data ?: emptyList()
                    currentPage = 1
                    val page = allSources.take(NewsConstants.PAGE_SIZE)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            sources = page,
                            hasMore = allSources.size > NewsConstants.PAGE_SIZE,
                            error = null,
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.error?.message ?: "Failed to load sources",
                        )
                    }
                }
                else -> Unit
            }
        }
    }

    fun loadMore() {
        if (_uiState.value.isLoadingMore || !_uiState.value.hasMore) return
        _uiState.update { it.copy(isLoadingMore = true) }
        currentPage++
        val nextItems = allSources.take(currentPage * NewsConstants.PAGE_SIZE)
        _uiState.update {
            it.copy(
                isLoadingMore = false,
                sources = nextItems,
                hasMore = nextItems.size < allSources.size,
            )
        }
    }

    fun search(query: String) {
        searchJob?.cancel()
        if (query.isBlank()) {
            if (searchQuery.isNotBlank()) {
                searchQuery = ""
                loadSources(currentCategory)
            }
            return
        }
        searchQuery = query
        searchJob = viewModelScope.launch(exceptionHandler) {
            delay(DEBOUNCE_MS)
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = searchSourcesUseCase(query, currentCategory)) {
                is Resource.Completed -> {
                    val results = result.data ?: emptyList()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            sources = results,
                            hasMore = false,
                            error = null,
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.error?.message ?: "Search failed",
                        )
                    }
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
