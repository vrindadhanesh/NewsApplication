package com.example.newsapplication.domain.usecase

import com.example.newsapplication.data.repository.ResultState
import com.example.newsapplication.domain.model.NewsResponse
import com.example.newsapplication.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

/**
 * A use case responsible for fetching the top news headlines.
 *
 * This class encapsulates a single, specific business rule: getting news headlines.
 * It acts as an intermediary between the ViewModel and the Repository, ensuring that
 * the ViewModel doesn't directly access the data layer. This follows the principles
 * of Clean Architecture.
 *
 * The invoke operator is overridden to allow the class instance to be called as if it
 * were a function, providing a clean and concise syntax.
 *
 */
class GetNewsHeadlinesUseCase(private val repository: NewsRepository) {
    operator fun invoke(
        country: String = "us",
        pageSize: Int = 20,
        page: Int = 1
    ): Flow<ResultState<NewsResponse>> {
        return repository.getNewsHeadlines(country = country, pageSize = pageSize, page = page)
    }
}
