package com.potaninpm.finaltour.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.potaninpm.core.ui.screens.ArticleWebView
import com.potaninpm.feature_feed.presentation.screens.CreatePostScreen
import com.potaninpm.feature_feed.presentation.screens.FeedScreen
import com.potaninpm.feature_finances.presentation.screens.FinancesScreen
import com.potaninpm.feature_home.presentation.screens.HomeScreen
import com.potaninpm.feature_home.presentation.screens.SearchScreen
import com.potaninpm.finaltour.R
import com.potaninpm.finaltour.navigation.bottomNav.BottomNavBar
import com.potaninpm.finaltour.navigation.bottomNav.BottomNavItem
import java.net.URLDecoder

@Composable
fun RootNavigation() {
    val rootNavController = rememberNavController()

    val navBackStackEntry by rootNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val routesWithBottomBar = listOf(
        RootNavDestinations.Home.route,
        RootNavDestinations.Finances.route,
        RootNavDestinations.Feed.route
    )

    Scaffold(
        bottomBar = {
            if (currentRoute in routesWithBottomBar) {
                BottomNavBar(
                    navController = rootNavController,
                    destinations = listOf(
                        BottomNavItem(
                            route = RootNavDestinations.Home,
                            labelRes = R.string.home,
                            selectedIcon = ImageVector.vectorResource(id = R.drawable.home_24px_filled),
                            unselectedIcon = ImageVector.vectorResource(id = R.drawable.home_24px_not_filled)
                        ),
                        BottomNavItem(
                            route = RootNavDestinations.Finances,
                            labelRes = R.string.finances,
                            selectedIcon = ImageVector.vectorResource(id = R.drawable.paid_24px_filled),
                            unselectedIcon = ImageVector.vectorResource(id = R.drawable.paid_24px_not_filled)
                        ),
                        BottomNavItem(
                            route = RootNavDestinations.Feed,
                            labelRes = R.string.posts,
                            selectedIcon = ImageVector.vectorResource(id = R.drawable.newspaper_24px_filled),
                            unselectedIcon = ImageVector.vectorResource(id = R.drawable.newspaper_24px_not_filled)
                        )
                    )
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = rootNavController,
            startDestination = RootNavDestinations.Home.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            composable(RootNavDestinations.Home.route) {
                HomeScreen(
                    rootNavController = rootNavController,
                )
            }

            composable(RootNavDestinations.Finances.route) {
                FinancesScreen(
                    modifier = Modifier
                        .padding(innerPadding)
                )
            }

            composable(RootNavDestinations.Search.route) {
                SearchScreen(rootNavController)
            }

            composable(RootNavDestinations.Feed.route) {
                FeedScreen(rootNavController)
            }

            composable(
                "${RootNavDestinations.CreatePost.route}/{url}/{title}/{webImageUrl}",
                arguments = listOf(
                    navArgument("url") { type = NavType.StringType },
                    navArgument("title") { type = NavType.StringType },
                    navArgument("webImageUrl") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val encodedUrl = backStackEntry.arguments?.getString("url") ?: ""
                val webUrl = URLDecoder.decode(encodedUrl, "UTF-8")

                val encodedImageUrl = backStackEntry.arguments?.getString("webImageUrl") ?: ""
                val webImageUrl = URLDecoder.decode(encodedImageUrl, "UTF-8")

                val title = backStackEntry.arguments?.getString("title") ?: ""

                CreatePostScreen(
                    navController = rootNavController,
                    selectedUrl = webUrl,
                    title = title,
                    webImageUrl = webImageUrl
                )
            }

            composable(
                "${RootNavDestinations.ArticleWebView.route}/{url}/{title}/{webImageUrl}",
                arguments = listOf(
                    navArgument("url") { type = NavType.StringType},
                    navArgument("title") { type = NavType.StringType },
                    navArgument("webImageUrl") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val encodedUrl = backStackEntry.arguments?.getString("url") ?: ""
                val url = URLDecoder.decode(encodedUrl, "UTF-8")

                val title = backStackEntry.arguments?.getString("title") ?: ""

                val encodedImageUrl = backStackEntry.arguments?.getString("webImageUrl") ?: ""
                val webImageUrl = URLDecoder.decode(encodedImageUrl, "UTF-8")

                ArticleWebView(
                    selectedUrl = url,
                    title = title,
                    webImageUrl = webImageUrl,
                    rootNavController = rootNavController
                )
            }
        }
    }

}