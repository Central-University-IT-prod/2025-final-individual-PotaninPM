package com.potaninpm.finaltour.navigation.bottomNav

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.potaninpm.finaltour.navigation.RootNavDestinations

@Composable
fun BottomNavBar(
    navController: NavHostController,
    destinations: List<BottomNavItem<RootNavDestinations>>
) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        destinations.forEach { destination ->
            val isSelected = currentRoute == destination.route.route

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(destination.route.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) destination.selectedIcon else destination.unselectedIcon,
                        contentDescription = stringResource(id = destination.labelRes)
                    )
                },
                label = { Text(text = stringResource(id = destination.labelRes)) }
            )
        }
    }
}
