package com.test.mandiri.news.feature.news.sources

import app.cash.turbine.test
import com.test.mandiri.news.common.model.ErrorResponse
import com.test.mandiri.news.common.model.Resource
import com.test.mandiri.news.domain.news.model.NewsSource
import com.test.mandiri.news.domain.news.NewsConstants
import com.test.mandiri.news.domain.news.usecase.GetSourcesByCategoryUseCase
import com.test.mandiri.news.domain.news.usecase.SearchSourcesUseCase
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
class SourcesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getSourcesByCategoryUseCase = mockk<GetSourcesByCategoryUseCase>()
    private val searchSourcesUseCase = mockk<SearchSourcesUseCase>()
    private lateinit var viewModel: SourcesViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SourcesViewModel(getSourcesByCategoryUseCase, searchSourcesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun makeSources(count: Int) = (1..count).map { i ->
        NewsSource(
            id = "source-$i", name = "Source $i", description = "Desc $i",
            url = "https://example.com/$i", category = "general", language = "en", country = "us"
        )
    }

    @Test
    fun `initial state is empty and not loading`() {
        assertTrue(viewModel.uiState.value.sources.isEmpty())
        assertFalse(viewModel.uiState.value.isLoading)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `loadSources shows first page and hasMore when more than PAGE_SIZE`() = runTest {
        val sources = makeSources(35)
        coEvery { getSourcesByCategoryUseCase("general") } returns Resource.Completed(sources)

        viewModel.uiState.test {
            awaitItem() // initial

            viewModel.loadSources("general")
            testDispatcher.scheduler.advanceUntilIdle()

            awaitItem() // loading
            val done = awaitItem()
            assertEquals(NewsConstants.PAGE_SIZE, done.sources.size)
            assertTrue(done.hasMore)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadMore appends next batch`() = runTest {
        val sources = makeSources(35)
        coEvery { getSourcesByCategoryUseCase("general") } returns Resource.Completed(sources)

        viewModel.loadSources("general")
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(NewsConstants.PAGE_SIZE, viewModel.uiState.value.sources.size)

        viewModel.loadMore()
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(35, viewModel.uiState.value.sources.size)
        assertFalse(viewModel.uiState.value.hasMore)
    }

    @Test
    fun `loadMore does nothing when hasMore is false`() = runTest {
        val sources = makeSources(5)
        coEvery { getSourcesByCategoryUseCase("general") } returns Resource.Completed(sources)

        viewModel.loadSources("general")
        testDispatcher.scheduler.advanceUntilIdle()
        assertFalse(viewModel.uiState.value.hasMore)

        viewModel.loadMore()
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(5, viewModel.uiState.value.sources.size)
    }

    @Test
    fun `loadSources shows error when repository fails`() = runTest {
        coEvery { getSourcesByCategoryUseCase("technology") } returns
            Resource.Error(ErrorResponse("Network error", -1))

        viewModel.uiState.test {
            awaitItem()
            viewModel.loadSources("technology")
            testDispatcher.scheduler.advanceUntilIdle()

            awaitItem() // loading
            val error = awaitItem()
            assertTrue(error.error != null)
            assertFalse(error.isLoading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `search passes category to useCase and returns filtered results`() = runTest {
        val results = makeSources(2)
        coEvery { searchSourcesUseCase("cnn", "general") } returns Resource.Completed(results)

        viewModel.uiState.test {
            awaitItem()
            viewModel.loadSources("general")  // set currentCategory
            coEvery { getSourcesByCategoryUseCase("general") } returns Resource.Completed(emptyList())
            testDispatcher.scheduler.advanceUntilIdle()
            awaitItem(); awaitItem() // loading + done

            viewModel.search("cnn")
            testDispatcher.scheduler.advanceUntilIdle()

            awaitItem() // loading
            val result = awaitItem()
            assertEquals(2, result.sources.size)
            assertFalse(result.hasMore) // search results are not paginated
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `search with blank after previous search reloads category`() = runTest {
        val sources = makeSources(5)
        coEvery { getSourcesByCategoryUseCase("sports") } returns Resource.Completed(sources)
        coEvery { searchSourcesUseCase("nfl", "sports") } returns Resource.Completed(makeSources(1))

        viewModel.loadSources("sports")
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.search("nfl")
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.uiState.test {
            awaitItem()
            viewModel.search("")
            testDispatcher.scheduler.advanceUntilIdle()

            awaitItem() // loading
            val reloaded = awaitItem()
            assertEquals(5, reloaded.sources.size)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
