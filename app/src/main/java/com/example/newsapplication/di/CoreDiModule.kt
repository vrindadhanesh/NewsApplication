package com.example.newsapplication.di

import com.example.newsapplication.BuildConfig
import com.example.newsapplication.domain.repository.NewsRepository
import com.example.newsapplication.data.api.NewsApi
import com.example.newsapplication.data.remote.ApiConstants
import com.example.newsapplication.data.remote.RetrofitProvider
import com.example.newsapplication.data.repository.NewsRepositoryImpl
import com.example.newsapplication.domain.usecase.GetNewsHeadlinesUseCase
import com.example.newsapplication.presentation.viewmodel.NewsListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val networkModule = module {
    single { RetrofitProvider.provideRetrofit(ApiConstants.BASE_URL) }
    single { get<Retrofit>().create(NewsApi::class.java) }
}

val repositoryModule = module {

    single<NewsRepository>{
        NewsRepositoryImpl(
            api = get(),
            apiKey = BuildConfig.API_KEY
        )
    }
}

val domainModule = module {
    single { GetNewsHeadlinesUseCase(get()) }
}

val presentationModule = module {
    viewModel { NewsListViewModel(get()) }
}

val coreDiModules = listOf(networkModule, repositoryModule, domainModule, presentationModule)
