package com.example.newsapplication.domain.usecase

import app.cash.turbine.test
import com.example.newsapplication.data.repository.ResultState
import com.example.newsapplication.domain.model.NewsResponse
import com.example.newsapplication.domain.repository.NewsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertEquals

class GetNewsHeadlinesUseCaseTest {

   private lateinit var newsRepository: NewsRepository
   private lateinit var getNewsHeadlinesUseCase: GetNewsHeadlinesUseCase

    @Before
    fun setUp() {
        newsRepository = mockk()
        getNewsHeadlinesUseCase = GetNewsHeadlinesUseCase(newsRepository)
    }

    @Test
    fun `invoke should call repository and return its flow`() = runTest {
        val mockFlow = flowOf(ResultState.Success(mockk<NewsResponse>()))

        coEvery {
            newsRepository.getNewsHeadlines(any(), any(), any())
        } returns mockFlow

        val resultFlow = getNewsHeadlinesUseCase(
            country = "us",
            pageSize = 10,
            page = 1
        )

        coVerify(exactly = 1) {
            newsRepository.getNewsHeadlines(
                country = "us",
                pageSize = 10,
                page = 1
            )
        }

        resultFlow.test {
            val item = awaitItem()
            assertEquals(true, item is ResultState.Success)
            awaitComplete()
        }
    }
}