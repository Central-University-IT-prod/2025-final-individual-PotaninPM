package com.potaninpm.finaltour.navigation

sealed class RootNavDestinations(val route: String) {
    data object Home : RootNavDestinations("home")

    data object Search : RootNavDestinations("search")

    data object Finances : RootNavDestinations("finances")

    data object Feed : RootNavDestinations("feed")

    data object CreatePost : RootNavDestinations("create_post")

    data object ArticleWebView : RootNavDestinations("article_web_view")
}