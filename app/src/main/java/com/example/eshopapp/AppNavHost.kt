package com.example.eshopapp

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.eshopapp.presentation.cart.CartScreen
import com.example.eshopapp.presentation.catalog.CatalogScreen
import com.example.eshopapp.presentation.home.HomeScreen
import com.example.eshopapp.presentation.profile.ProfileScreen

sealed class Route(val path: String) {
    data object Home : Route("home")
    data object Category : Route("category")
    data object Cart : Route("cart")
    data object Profile : Route("profile")
}
data class BottomItem(
    val route: Route,
    val label: String,
    val icon: ImageVector
)

val bottomItems = listOf(
    BottomItem(Route.Home,    "home",   Icons.Default.Home),
    BottomItem(Route.Category,  "category",   Icons.Default.Search),
    BottomItem(Route.Cart, "cart", Icons.Default.ShoppingCart),
    BottomItem(Route.Profile, "profile", Icons.Default.Person)
)

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomItems.forEach { item ->
                    val selected = currentDestination
                        ?.hierarchy
                        ?.any { it.route == item.route.path } == true

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(item.route.path){
                                launchSingleTop = true
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(item.icon, contentDescription = null)
                        },
                        label = {
                            Text(item.label)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.Home.path,
            modifier = Modifier.padding(innerPadding)
        ){
            composable(Route.Home.path){ HomeScreen() }
            //      Потом изменить каталог на категории
            composable(Route.Category.path){ CatalogScreen() }
            composable(Route.Cart.path){ CartScreen() }
            composable(Route.Profile.path){ ProfileScreen() }
        }
    }
}