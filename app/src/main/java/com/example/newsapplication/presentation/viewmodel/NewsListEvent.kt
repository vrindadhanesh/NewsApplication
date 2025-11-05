package com.example.newsapplication.presentation.viewmodel

import com.example.newsapplication.domain.model.Article

/**
 * Represents all possible user actions and events that can be sent from the UI
 *
 * This sealed class follows the MVI (Model-View-Intent) pattern
 */
sealed class NewsListEvent {
    /**
     * An event triggered when a user clicks on a single news article in the list.
     */
    data class OnArticleClick(val article: Article) : NewsListEvent()
}
