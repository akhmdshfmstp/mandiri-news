package com.test.mandiri.news.domain.news

import com.test.mandiri.news.common.model.ErrorResponse
import com.test.mandiri.news.common.model.Resource
import com.test.mandiri.news.domain.news.model.NewsSource
import com.test.mandiri.news.domain.news.repository.NewsRepository
import com.test.mandiri.news.domain.news.usecase.GetSourcesByCategoryUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetSourcesByCategoryUseCaseTest {

    private val repository = mockk<NewsRepository>()
    private lateinit var useCase: GetSourcesByCategoryUseCase

    @Before
    fun setUp() {
        useCase = GetSourcesByCategoryUseCase(repository)
    }

    @Test
    fun `invoke returns Completed with list of sources on success`() = runTest {
        val sources = listOf(
            NewsSource("cnn", "CNN", "Cable News Network", "https://cnn.com", "general", "en", "us"),
            NewsSource("bbc-news", "BBC News", "British Broadcasting Corp.", "https://bbc.co.uk", "general", "en", "gb"),
        )
        coEvery { repository.getSourcesByCategory("general") } returns Resource.Completed(sources)

        val result = useCase("general")

        assertTrue(result is Resource.Completed)
        assertEquals(2, (result as Resource.Completed).data?.size)
        assertEquals("cnn", result.data?.first()?.id)
    }

    @Test
    fun `invoke returns Error when repository fails`() = runTest {
        coEvery { repository.getSourcesByCategory("technology") } returns
            Resource.Error(ErrorResponse("Network error", -1))

        val result = useCase("technology")

        assertTrue(result is Resource.Error)
        assertEquals("Network error", (result as Resource.Error).error?.message)
    }

    @Test
    fun `invoke returns Completed with empty list when no sources found`() = runTest {
        coEvery { repository.getSourcesByCategory("unknown") } returns Resource.Completed(emptyList())

        val result = useCase("unknown")

        assertTrue(result is Resource.Completed)
        assertTrue((result as Resource.Completed).data?.isEmpty() == true)
    }
}
