package com.example.newsapplication.domain.repository

import com.example.newsapplication.data.repository.ResultState
import com.example.newsapplication.domain.model.NewsResponse
import kotlinx.coroutines.flow.Flow


/**
 * Interface for the news data repository.
 */
interface NewsRepository {
/**
 * Fetches a flow of top news headlines for a given country.
 *
 * This function returns a Flow that emits ResultState objects, representing the
 * different stages of the data-fetching process (Loading, Success, Error)
 * *
 * */
    fun getNewsHeadlines(country: String = "us", pageSize: Int = 20, page: Int = 1)
    : Flow<ResultState<NewsResponse>>
}
