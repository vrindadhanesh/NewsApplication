package com.example.newsapplication.data.repository

import com.example.newsapplication.domain.repository.NewsRepository
import com.example.newsapplication.data.api.NewsApi
import com.example.newsapplication.domain.model.NewsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import java.io.IOException

/**
 * Implementation of the [NewsRepository] interface.
 *
 * This class is responsible for fetching news data from the remote data source (NewsApi)
 * and exposing it as a reactive stream of states using Kotlin's Flow. It handles
 * the different states of a network request: loading, success, and various error types.
 *
 */
class NewsRepositoryImpl(private val api: NewsApi, private val apiKey: String): NewsRepository {

    override fun getNewsHeadlines(country: String, pageSize: Int, page: Int): Flow<ResultState<NewsResponse>> =
        flow {
            emit(ResultState.Loading)
            try {
                val resp: Response<NewsResponse> = api.getTopHeadlines(country, pageSize, page, apiKey)
                if (resp.isSuccessful) {
                    resp.body()?.let {
                        emit(ResultState.Success(it))
                    } ?: emit(ResultState.Error("Empty response body"))
                } else {
                    emit(ResultState.Error("Network error: ${resp.code()} ${resp.message()}"))
                }
            } catch (e: IOException) {
                emit(ResultState.Error("IO error: ${e.localizedMessage}"))
            } catch (t: Throwable) {
                emit(ResultState.Error("Unknown error: ${t.localizedMessage}"))
            }
        }.flowOn(Dispatchers.IO)
}
sealed class ResultState<out T> {
    object Loading : ResultState<Nothing>()
    data class Success<T>(val data: T) : ResultState<T>()
    data class Error(val message: String) : ResultState<Nothing>()
}

