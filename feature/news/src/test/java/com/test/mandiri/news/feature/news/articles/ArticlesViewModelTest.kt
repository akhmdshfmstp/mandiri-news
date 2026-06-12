package com.test.mandiri.news.feature.news.articles

import app.cash.turbine.test
import com.test.mandiri.news.common.model.ErrorResponse
import com.test.mandiri.news.common.model.Resource
import com.test.mandiri.news.domain.news.model.NewsArticle
import com.test.mandiri.news.domain.news.model.PaginatedResult
import com.test.mandiri.news.domain.news.usecase.GetArticlesBySourceUseCase
import com.test.mandiri.news.domain.news.usecase.SearchArticlesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ArticlesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getArticlesBySourceUseCase = mockk<GetArticlesBySourceUseCase>()
    private val searchArticlesUseCase = mockk<SearchArticlesUseCase>()
    private lateinit var viewModel: ArticlesViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ArticlesViewModel(getArticlesBySourceUseCase, searchArticlesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun makeArticle(index: Int) = NewsArticle(
        sourceId = "cnn", sourceName = "CNN", author = "Author",
        title = "Title $index", description = "Desc $index",
        url = "https://example.com/$index", urlToImage = "",
        publishedAt = "2024-01-01T00:00:00Z", content = ""
    )

    private fun makePaginated(count: Int, total: Int, page: Int = 1) = PaginatedResult(
        items = (1..count).map { makeArticle(it + (page - 1) * 20) },
        totalResults = total,
        currentPage = page,
        pageSize = 20
    )

    @Test
    fun `initial state has empty articles`() {
        assertTrue(viewModel.uiState.value.articles.isEmpty())
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `loadArticles shows loading then articles on success`() = runTest {
        coEvery { getArticlesBySourceUseCase("cnn", 1) } returns
            Resource.Completed(makePaginated(20, 50))

        viewModel.uiState.test {
            awaitItem() // initial

            viewModel.loadArticles("cnn")
            testDispatcher.scheduler.advanceUntilIdle()

            val loading = awaitItem()
            assertTrue(loading.isLoading)

            val done = awaitItem()
            assertFalse(done.isLoading)
            assertEquals(20, done.articles.size)
            assertTrue(done.hasMore)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadMore appends next page to articles`() = runTest {
        coEvery { getArticlesBySourceUseCase("cnn", 1) } returns
            Resource.Completed(makePaginated(20, 50, page = 1))
        coEvery { getArticlesBySourceUseCase("cnn", 2) } returns
            Resource.Completed(makePaginated(20, 50, page = 2))

        viewModel.loadArticles("cnn")
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(20, viewModel.uiState.value.articles.size)

        viewModel.loadMore()
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(40, viewModel.uiState.value.articles.size)
    }

    @Test
    fun `loadArticles shows error state when repository fails`() = runTest {
        coEvery { getArticlesBySourceUseCase("bad", 1) } returns
            Resource.Error(ErrorResponse("API error", 500))

        viewModel.uiState.test {
            awaitItem()

            viewModel.loadArticles("bad")
            testDispatcher.scheduler.advanceUntilIdle()

            awaitItem() // loading
            val error = awaitItem()
            assertTrue(error.error != null)
            assertFalse(error.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `search returns articles matching query`() = runTest {
        coEvery { searchArticlesUseCase("kotlin", 1) } returns
            Resource.Completed(makePaginated(5, 5))

        viewModel.uiState.test {
            awaitItem()

            viewModel.search("kotlin")
            testDispatcher.scheduler.advanceUntilIdle()

            awaitItem() // loading
            val result = awaitItem()
            assertEquals(5, result.articles.size)
            assertFalse(result.hasMore)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadMore does nothing when hasMore is false`() = runTest {
        coEvery { getArticlesBySourceUseCase("cnn", 1) } returns
            Resource.Completed(makePaginated(5, 5))

        viewModel.loadArticles("cnn")
        testDispatcher.scheduler.advanceUntilIdle()
        val articleCountBefore = viewModel.uiState.value.articles.size

        viewModel.loadMore()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(articleCountBefore, viewModel.uiState.value.articles.size)
    }
}
