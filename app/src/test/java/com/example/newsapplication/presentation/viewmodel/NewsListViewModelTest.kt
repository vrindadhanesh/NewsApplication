package com.example.newsapplication.presentation.viewmodel

import app.cash.turbine.test
import com.example.newsapplication.data.repository.ResultState
import com.example.newsapplication.domain.model.Article
import com.example.newsapplication.domain.model.NewsResponse
import com.example.newsapplication.domain.usecase.GetNewsHeadlinesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalCoroutinesApi::class)
class NewsListViewModelTest {

    private lateinit var getTopHeadlinesUseCase: GetNewsHeadlinesUseCase
    private lateinit var viewModel: NewsListViewModel

    @Before
    fun setUp() {
        // create new mock per test
        getTopHeadlinesUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should fetch headlines and update state from Loading to Success`() = runTest {
        // make Main use this test's scheduler
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))

        // GIVEN
        val mockArticle = mockk<Article>(relaxed = true)
        val mockResponse = NewsResponse(status = "ok", totalResults = 1, articles = listOf(mockArticle))
        val successFlow = flowOf(
            ResultState.Loading,
            ResultState.Success(mockResponse)
        )
        coEvery { getTopHeadlinesUseCase() } returns successFlow

        viewModel = NewsListViewModel(getTopHeadlinesUseCase)

        advanceUntilIdle()

        val final = viewModel.uiState.value
        assertFalse(final.isLoading)
        assertEquals(1, final.articles.size)
        assertEquals(mockArticle, final.articles[0])
        assertNull(final.error)
    }

    @Test
    fun `init should fetch headlines and update state from Loading to Error`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))


        val errorMessage = "Network Error"
        val errorFlow = flowOf(
            ResultState.Loading,
            ResultState.Error(errorMessage)
        )
        coEvery { getTopHeadlinesUseCase() } returns errorFlow

        // WHEN
        viewModel = NewsListViewModel(getTopHeadlinesUseCase)
        advanceUntilIdle()


        val final = viewModel.uiState.value
        assertFalse(final.isLoading)
        assertTrue(final.articles.isEmpty())
        assertEquals(errorMessage, final.error)
    }

    @Test
    fun `onEvent OnArticleClick should emit correct navigation effect`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))


        coEvery { getTopHeadlinesUseCase() } returns flowOf(ResultState.Loading)

        viewModel = NewsListViewModel(getTopHeadlinesUseCase)
        advanceUntilIdle()

        val article = Article(
            url = "https://example.com/test-article",
            title = "Test",
            source = null,
            author = null,
            description = null,
            urlToImage = null,
            publishedAt = null,
            content = null
        )
        val event = NewsListEvent.OnArticleClick(article)


        viewModel.navigationEffect.test {
            viewModel.onEvent(event)

            val expectedUrl = URLEncoder.encode(article.url, StandardCharsets.UTF_8.toString())
            val expectedRoute = "detail/$expectedUrl"
            assertEquals(expectedRoute, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }
}