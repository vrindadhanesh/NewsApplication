# News App - A News Application

News App is a sample Android application built using modern Android development practices.
It demonstrates how to build a clean, scalable, and maintainable app that fetches and displays top news headlines from a remote API(public api).

The project follows the MVI (Model-View-Intent) architecture pattern on top of a multi-layered clean architecture, ensuring a clear separation of concerns and a unidirectional data flow.

##  Features

*   News Headlines: Fetches and displays a list of top news headlines from the NewsAPI.
*   Article Details: Tapping on an article opens its full content in a `WebView`.
*   Clean, Modern UI: Built with Jetpack Compose, Material 3, Android's modern toolkit for building native UI.
*   Robust Error Handling: Displays loading indicators and error messages with a retry option.
*   Secure API Key Management:The API key is stored securely and is not checked into version control.
*   MVI Architecture: Implements a unidirectional data flow for predictable state management.
*   Comprehensive Unit Tests: Includes unit tests for the ViewModel, UseCase, and Repository layers.

##  Tech Stack & Architecture

### Architecture

*   Clean Architecture: The project is divided into three main layers:
    *    data: Handles data sources (networking) and data mapping (DTOs to domain models).
    *    domain: Contains the core business logic (UseCases) and data models. It is independent of the other layers.
    *    presentation : Responsible for the UI (Jetpack Compose) and state management (ViewModel).
*   MVI (Model-View-Intent): A declarative UI architecture pattern that complements Jetpack Compose.
    *   Model: Represents the UI state (NewsListUiState).
    *   View: A composable screen that observes state changes and sends user actions as events.
    *   Intent: User actions are represented as sealed events (NewsListEvent).

### Libraries Used

| Library                                                 | Version  | Category                 | Purpose                                                                                                     |
| :------------------------------------------------------ | :------- | :----------------------- | :---------------------------------------------------------------------------------------------------------- |
| **[Jetpack Compose](https://developer.android.com/jetpack/compose)** | `1.6.7`  | UI                       | Android's modern, declarative UI toolkit used for building the entire UI.                                 |
| **[Material 3](https://m3.material.io/)**               | `1.2.1`  | UI                       | The latest version of Google's design system, providing components like `Scaffold`, `TopAppBar`, etc.         |
| **[Koin](https://insert-koin.io/)**                     | `3.5.6`  | Dependency Injection     | A lightweight and pragmatic dependency injection framework for Kotlin, used to manage dependencies across the app. |
| **[Retrofit](https://square.github.io/retrofit/)**      | `2.11.0` | Networking               | A type-safe HTTP client for Android and Java, used to make API calls to NewsAPI.                           |
| **[OkHttp](https://square.github.io/okhttp/)**          | `4.12.0` | Networking               | The underlying HTTP client for Retrofit, providing efficient network requests.                            |
| **[Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)** | `1.8.0`  | Asynchronous Programming | Used for managing background threads and handling asynchronous operations like network calls.                 |
| **[Coil](https://coil-kt.github.io/coil/)**             | `2.6.0`  | Image Loading            | An image loading library for Android backed by Kotlin Coroutines, used to load article images.              |
| **[Jetpack Navigation](https://developer.android.com/jetpack/compose/navigation)** | `2.7.7`  | Navigation               | Manages navigation between composable screens.                                                            |
| **[JUnit](https://junit.org/junit4/)**                  | `4.13.2` | Unit Testing             | The standard framework for writing unit tests in Java/Kotlin.                                               |
| **[MockK](https://mockk.io/)**                          | `1.13.11`| Unit Testing             | An advanced mocking library for Kotlin, used to create mocks for dependencies in unit tests.                |
| **[Turbine](https://github.com/cashapp/turbine)**       | `1.1.0`  | Unit Testing             | A small library for testing Kotlin Flows, making it easy to assert emissions.                               |


## Setup and Configuration

### 1. Get an API Key
This project uses the [NewsAPI](https://newsapi.org/). You will need to get a free API key to run the app.

1.  Go to [newsapi.org](https://newsapi.org/) and register for a free developer account.
2.  Find your API key on your account page.

### 2. Store the API Key
To keep the API key secure, it is not stored in the source code. You must add it to a `local.properties` file.

1.  In the root directory of the project, find or create a file named `local.properties`.
2.  Add your API key to this file with the following key name: `API_KEY`.

### 3. How the Key is Accessed (via BuildConfig)

1.  The project uses Gradle to securely expose the apiKey from local.properties to the application code without hardcoding it.
2.  The app-level build.gradle.kts file reads the value from local.properties.
3.  It then uses the buildConfigField instruction to generate a field in a special BuildConfig.java class at compile time

### 4. Build and Run
1.  After adding the API key, you can build and run the application on an Android emulator or a physical device using Android Studio.

### Running Tests
The project includes unit tests for the repository, use case, and ViewModel layers. You can run them directly from Android Studio:

1.Navigate to a test file (e.g., app/src/test/java/com/example/userlistapplication/presentation/viewmodel/NewsListViewModelTest.kt).
2.Click the green "play" icon next to the class name or a specific test function to run the tests.

### 4. Good to have
1. For now, I have added the API key in the defaultConfig, but the cleaner, 
   safer setup is to move those into the debug and release sections with their respective dev and prod keys.
   That way:
   Debug builds use the development API key, preventing accidental hits to production.
   Release builds use the production API key, keeping things isolated and secure.
2. Currently, the app only fetches the first page of results from the API. 
   Implement pagination to fetch and display more articles as the user scrolls to the bottom of the list. 
   This would involve updating the NewsListViewModel to track the current page number and modifying the LazyColumn to trigger a "load more" event
3. Good to add a Loader in the NewsDetailScreen 
