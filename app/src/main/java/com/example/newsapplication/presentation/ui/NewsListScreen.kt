package com.example.newsapplication.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.newsapplication.R
import com.example.newsapplication.presentation.viewmodel.NewsListEvent
import com.example.newsapplication.presentation.viewmodel.NewsListViewModel
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class) // Required for TopAppBar and Scaffold
@Composable
fun NewsListScreen(
    viewModel: NewsListViewModel = getViewModel(),
    navigateToDetail: (String) -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.navigationEffect.collect { route ->
            navigateToDetail(route)
        }
    }

    Scaffold(topBar = { TopAppBar(title = { Text("News Headlines") }) }) { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()) {
            when {
                uiState.value.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                uiState.value.error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Error: ${uiState.value.error}")
                        Spacer(Modifier.height(8.dp))
                    //need to implement retry functionaity also
                    }
                }

                else -> {
                    LazyColumn(contentPadding = PaddingValues(8.dp)) {
                        items(uiState.value.articles) { article ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.onEvent(
                                            NewsListEvent.OnArticleClick(
                                                article
                                            )
                                        )
                                    }
                                    .padding(8.dp)
                            ) {
                                if (!article.urlToImage.isNullOrEmpty()) {
                                    AsyncImage(
                                        model = article.urlToImage,
                                        contentDescription = article.title,
                                        modifier = Modifier
                                            .size(100.dp)
                                            .padding(end = 8.dp)
                                    )
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = article.title ?: stringResource(id = R.string.article_with_no_title),
                                        style = MaterialTheme.typography.titleMedium,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(Modifier.height(4.dp))
                                    Text(
                                        text = article.description ?: "",
                                        style = MaterialTheme.typography.bodyMedium,
                                        maxLines = 3,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(Modifier.height(6.dp))
                                    Text(
                                        text = article.publishedAt ?: "",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}
