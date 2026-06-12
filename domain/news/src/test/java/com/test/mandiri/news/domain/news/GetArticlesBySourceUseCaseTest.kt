package com.test.mandiri.news.domain.news

import com.test.mandiri.news.common.model.ErrorResponse
import com.test.mandiri.news.common.model.Resource
import com.test.mandiri.news.domain.news.model.NewsArticle
import com.test.mandiri.news.domain.news.model.PaginatedResult
import com.test.mandiri.news.domain.news.repository.NewsRepository
import com.test.mandiri.news.domain.news.usecase.GetArticlesBySourceUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetArticlesBySourceUseCaseTest {

    private val repository = mockk<NewsRepository>()
    private lateinit var useCase: GetArticlesBySourceUseCase

    @Before
    fun setUp() {
        useCase = GetArticlesBySourceUseCase(repository)
    }

    private fun makeArticle(index: Int) = NewsArticle(
        sourceId = "cnn",
        sourceName = "CNN",
        author = "Author $index",
        title = "Title $index",
        description = "Description $index",
        url = "https://example.com/$index",
        urlToImage = "",
        publishedAt = "2024-01-01T00:00:00Z",
        content = ""
    )

    @Test
    fun `invoke returns paginated articles on success`() = runTest {
        val articles = (1..20).map { makeArticle(it) }
        val paginated = PaginatedResult(articles, totalResults = 50, currentPage = 1, pageSize = 20)
        coEvery { repository.getArticlesBySource("cnn", 1, 20) } returns Resource.Completed(paginated)

        val result = useCase("cnn", 1)

        assertTrue(result is Resource.Completed)
        val data = (result as Resource.Completed).data!!
        assertEquals(20, data.items.size)
        assertTrue(data.hasMore)
    }

    @Test
    fun `hasMore is false when all articles loaded`() = runTest {
        val articles = (1..10).map { makeArticle(it) }
        val paginated = PaginatedResult(articles, totalResults = 10, currentPage = 1, pageSize = 20)
        coEvery { repository.getArticlesBySource("bbc-news", 1, 20) } returns Resource.Completed(paginated)

        val result = useCase("bbc-news", 1)

        val data = (result as Resource.Completed).data!!
        assertFalse(data.hasMore)
    }

    @Test
    fun `invoke returns Error when repository fails`() = runTest {
        coEvery { repository.getArticlesBySource("bad-source", 1, 20) } returns
            Resource.Error(ErrorResponse("API error", 500))

        val result = useCase("bad-source", 1)

        assertTrue(result is Resource.Error)
        assertEquals(500, (result as Resource.Error).error?.code)
    }
}
