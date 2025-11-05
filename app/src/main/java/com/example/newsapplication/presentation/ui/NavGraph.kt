package com.example.newsapplication.presentation.ui

import NewsDetailScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            NewsListScreen(
                navigateToDetail = { route ->
                    navController.navigate(route)
                }
            )
        }

        composable(
            route = "detail/{articleUrl}",
            arguments = listOf(navArgument("articleUrl") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedUrl = backStackEntry.arguments?.getString("articleUrl") ?: ""
            val decodedUrl = URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString())

            NewsDetailScreen(
                articleUrl = decodedUrl,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

