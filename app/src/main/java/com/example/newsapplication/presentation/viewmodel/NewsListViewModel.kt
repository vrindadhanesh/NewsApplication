package com.example.newsapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapplication.data.repository.ResultState
import com.example.newsapplication.domain.model.Article
import com.example.newsapplication.domain.usecase.GetNewsHeadlinesUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Represents the state of the NewsListScreen UI.
 */

data class NewsUiState(
    val isLoading: Boolean = false,
    val articles: List<Article> = emptyList(),
    val error: String? = null
)

/**
 * The ViewModel for the NewsListScreen.
 *
 * This class is responsible for managing the UI state,
 * and handling user events according to the MVI (Model-View-Intent) architecture.
 * It connects the UI layer (View) with the business logic layer (UseCases).
 *
 * getTopHeadlinesUseCase The use case responsible for fetching news headlines.
 */
class NewsListViewModel(private val getTopHeadlinesUseCase: GetNewsHeadlinesUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(NewsUiState())
    val uiState: StateFlow<NewsUiState> = _uiState

    private val _navigationEffect = MutableSharedFlow<String>()
    val navigationEffect = _navigationEffect.asSharedFlow()


    init {
        fetchNewsHeadlines()
    }

    fun onEvent(event: NewsListEvent) {
        when (event) {
            is NewsListEvent.OnArticleClick -> {
                viewModelScope.launch {
                    val encodedUrl = URLEncoder.encode(event.article.url, StandardCharsets.UTF_8.toString())
                    _navigationEffect.emit("detail/$encodedUrl")
                }
            }
        }
    }

    private fun fetchNewsHeadlines() {
        viewModelScope.launch {
                getTopHeadlinesUseCase().collect { result ->
                    when (result) {
                        is ResultState.Loading -> _uiState.value = NewsUiState(isLoading = true, error = null)
                        is ResultState.Success -> _uiState.value = NewsUiState(isLoading = false, articles = result.data.articles)
                        is ResultState.Error -> _uiState.value = NewsUiState(isLoading = false, articles = emptyList(), error = result.message)
                    }
                }
        }
    }
}
