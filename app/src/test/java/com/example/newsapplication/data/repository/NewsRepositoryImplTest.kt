package com.example.newsapplication.data.repository

import app.cash.turbine.test
import com.example.newsapplication.data.api.NewsApi
import com.example.newsapplication.domain.model.Article
import com.example.newsapplication.domain.model.NewsResponse
import com.example.newsapplication.domain.model.Source
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException


class NewsRepositoryImplTest {

    private var newsApi: NewsApi = mockk()
    private val apiKey = "TEST_KEY"
    private lateinit var repository: NewsRepositoryImpl

    @Before
    fun setup() {
        newsApi = mockk()
        repository = NewsRepositoryImpl(newsApi, apiKey)
    }

    @After
    fun tearDown() {

    }

    @Test
    fun `getNewsHeadlines emits Loading then Success when api call is successful`() = runTest {

        val mockArticle = Article(
            source = Source(id = "cnn", name = "CNN"),
            author = "John Doe",
            title = "Test Title",
            description = "Test Description",
            url = "http://test.com",
            urlToImage = "http://test.com/image.png",
            publishedAt = "2025-11-05T10:00:00Z",
            content = "Test Content"
        )
        val mockApiResponse = NewsResponse(
            status = "ok",
            totalResults = 1,
            articles = listOf(mockArticle)
        )
        coEvery { newsApi.getTopHeadlines(any(), any(), any(), apiKey) } returns Response.success(mockApiResponse)

        val resultFlow = repository.getNewsHeadlines("us", 10, 1)

        resultFlow.test {
            val loadingState = awaitItem()
            assertTrue("First emission should be ResultState.Loading", loadingState is ResultState.Loading)

            val successState = awaitItem()
            assertTrue("Second emission should be ResultState.Success", successState is ResultState.Success)

            val data = (successState as ResultState.Success).data
            assertEquals(1, data.articles.size)
            assertEquals("Test Title", data.articles[0].title)
            assertEquals("CNN", data.articles[0].source?.name)

            awaitComplete()
        }
    }

    @Test
    fun `getNewsHeadlines emits Loading then Error when api response is not successful`() = runTest {

        val exceptionMessage = "An unexpected error occurred"
        coEvery { newsApi.getTopHeadlines(any(), any(), any(), apiKey) } throws RuntimeException(exceptionMessage)


        val resultFlow = repository.getNewsHeadlines("us", 10, 1)

        resultFlow.test {
            assertTrue("Expected Loading state", awaitItem() is ResultState.Loading)

            val errorState = awaitItem()
            assertTrue("Expected Error state", errorState is ResultState.Error)
            assertEquals("Unknown error: An unexpected error occurred", (errorState as ResultState.Error).message)

            awaitComplete()
        }
    }

    @Test
    fun `getNewsHeadlines emits Loading then Error when a network exception occurs`() = runTest {

        val exceptionMessage = "No Internet"
        coEvery { newsApi.getTopHeadlines(any(), any(), any(), apiKey) } throws IOException(exceptionMessage)


        val resultFlow = repository.getNewsHeadlines("us", 10, 1)

        
        resultFlow.test {
            assertTrue("Expected Loading state", awaitItem() is ResultState.Loading)

            val errorState = awaitItem()
            assertTrue("Expected Error state", errorState is ResultState.Error)
            assertEquals("IO error: $exceptionMessage", (errorState as ResultState.Error).message)

            awaitComplete()
        }
    }
}