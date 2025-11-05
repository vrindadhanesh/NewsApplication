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

// Replace these with your actual package imports
// import com.example.news.api.NewsApi
// import com.example.news.model.NewsResponse
// import com.example.news.util.ResultState
// import com.example.news.repository.NewsRepositoryImpl

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
        // no-op for now
    }

    @Test
    fun `getNewsHeadlines emits Loading then Success when api call is successful`() = runTest {
        // GIVEN: A real, valid response object to be returned by the mocked API
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

        // WHEN: The repository function is called
        val resultFlow = repository.getNewsHeadlines("us", 10, 1)

        // THEN: Verify the flow emits Loading, then Success, and then completes
        resultFlow.test {
            // 1. Assert the first emission is Loading
            val loadingState = awaitItem()
            assertTrue("First emission should be ResultState.Loading", loadingState is ResultState.Loading)

            // 2. Assert the second emission is Success
            val successState = awaitItem()
            assertTrue("Second emission should be ResultState.Success", successState is ResultState.Success)

            // 3. Assert the data within the Success state is correct
            val data = (successState as ResultState.Success).data
            assertEquals(1, data.articles.size)
            assertEquals("Test Title", data.articles[0].title)
            assertEquals("CNN", data.articles[0].source?.name)

            // 4. Assert that the flow has completed
            awaitComplete()
        }
    }

    @Test
    fun `getNewsHeadlines emits Loading then Error when api response is not successful`() = runTest {
        // GIVEN: An error response from the API (e.g., 404 Not Found)
        val exceptionMessage = "An unexpected error occurred"
        coEvery { newsApi.getTopHeadlines(any(), any(), any(), apiKey) } throws RuntimeException(exceptionMessage)

        // WHEN: The repository function is called
        val resultFlow = repository.getNewsHeadlines("us", 10, 1)

        // THEN: Verify the flow emits Loading, then Error
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
        // GIVEN: The API call throws an IOException
        val exceptionMessage = "No Internet"
        coEvery { newsApi.getTopHeadlines(any(), any(), any(), apiKey) } throws IOException(exceptionMessage)

        // WHEN: The repository function is called
        val resultFlow = repository.getNewsHeadlines("us", 10, 1)

        // THEN: Verify the flow emits Loading, then Error with the correct exception message
        resultFlow.test {
            assertTrue("Expected Loading state", awaitItem() is ResultState.Loading)

            val errorState = awaitItem()
            assertTrue("Expected Error state", errorState is ResultState.Error)
            assertEquals("IO error: $exceptionMessage", (errorState as ResultState.Error).message)

            awaitComplete()
        }
    }
}