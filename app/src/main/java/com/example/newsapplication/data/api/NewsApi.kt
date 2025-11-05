package com.example.newsapplication.data.api

import com.example.newsapplication.domain.model.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String = "us",
        @Query("pageSize") pageSize: Int = 20,
        @Query("page") page: Int = 1,
        @Query("apiKey") apiKey: String
    ): Response<NewsResponse>

}
