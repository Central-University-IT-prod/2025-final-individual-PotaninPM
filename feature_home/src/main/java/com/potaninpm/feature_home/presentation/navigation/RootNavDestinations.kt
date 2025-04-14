package com.potaninpm.feature_home.presentation.navigation

sealed class RootNavDestinations(val route: String) {

    data object Home : RootNavDestinations("home")

    data object Search : RootNavDestinations("search")
}