package com.potaninpm.feature_home.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.potaninpm.feature_home.presentation.screens.HomeScreen
import com.potaninpm.feature_home.presentation.screens.SearchScreen

@Composable
fun HomeNavHost() {
    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = RootNavDestinations.Home.route
    ) {
        composable(RootNavDestinations.Home.route) {
            HomeScreen(rootNavController = rootNavController)
        }

        composable(RootNavDestinations.Search.route) {
            SearchScreen(rootNavController)
        }
    }
}