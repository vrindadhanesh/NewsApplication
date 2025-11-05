package com.example.newsapplication.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)

@Parcelize
data class Source(
    val id: String? = null,
    val name: String? = null
) : Parcelable

@Parcelize
data class Article(
    val source: Source? = null,
    val author: String? = null,
    val title: String? = null,
    val description: String? = null,
    val url: String? = null,
    val urlToImage: String? = null,
    val publishedAt: String? = null,
    val content: String? = null
) : Parcelable