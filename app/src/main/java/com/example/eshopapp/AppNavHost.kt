package com.example.eshopapp

import android.net.Uri
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.eshopapp.presentation.cart.CartScreen
import com.example.eshopapp.presentation.cart.CartViewModel
import com.example.eshopapp.presentation.category.AllCategoriesScreen
import com.example.eshopapp.presentation.category.CatalogScreen
import com.example.eshopapp.presentation.category.ProductInfo
import com.example.eshopapp.presentation.favorite.FavoriteScreen
import com.example.eshopapp.presentation.favorite.FavoriteViewModel
import com.example.eshopapp.presentation.home.HomeScreen
import com.example.eshopapp.presentation.home.SearchProductScreen
import com.example.eshopapp.presentation.profile.ProfileScreen

sealed class Route(val path: String) {
    data object Home : Route("home")
    data object Category : Route("category")
    data object Cart : Route("cart")
    data object Favorite : Route("favorite")
    data object Profile : Route("profile")

    //побочные
    data object ProductInfo : Route("product/{id}"){
        fun createRoute(id: Int) = "product/$id"
        const val ARG_ID = "id"
    }
    data object CategoryProducts : Route("categories/{slug}"){
        fun createRoute(slug: String) = "categories/$slug"
        const val ARG_NAME = "slug"
    }
    data object SearchProducts : Route("search?query={query}"){
        fun createRoute(query: String): String {
            val encodedQuery = Uri.encode(query)
            return "search?query=$encodedQuery"
        }
        const val ARG_NAME = "query"
    }
}
data class BottomItem(
    val route: Route,
    val label: String,
    val icon: ImageVector
)

val bottomItems = listOf(
    BottomItem(Route.Home, "home", Icons.Default.Home),
    BottomItem(Route.Category,  "category", Icons.Default.Search),
    BottomItem(Route.Cart, "cart", Icons.Default.ShoppingCart),
    BottomItem(Route.Favorite, "favorite", Icons.Default.Favorite),
    BottomItem(Route.Profile, "profile", Icons.Default.Person)
)

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val cartVm: CartViewModel = hiltViewModel()
    val favoriteVm: FavoriteViewModel = hiltViewModel()

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            NavigationBar {
                bottomItems.forEach { item ->
                    val selected = currentDestination
                        ?.hierarchy
                        ?.any { it.route == item.route.path } == true

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            val popped = navController.popBackStack(item.route.path, inclusive = false)
                            if (!popped) {
                                navController.navigate(item.route.path){
                                    launchSingleTop = true
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    restoreState = true
                                }
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
            composable(Route.Home.path) {
                HomeScreen(
                    onProductClick = { id ->
                        navController.navigate(Route.ProductInfo.createRoute(id))
                    },
                    onCategoryClick = { slug ->
                        navController.navigate(Route.CategoryProducts.createRoute(slug))
                    },
                    onAllCategoryClick = {
                        navController.navigate(Route.Category.path)
                    },
                    onSearch = { query ->
                        navController.navigate(Route.SearchProducts.createRoute(query))
                    },
                    cartVm = cartVm,
                    favoriteVm = favoriteVm
                )
            }

            composable(Route.Category.path){
                AllCategoriesScreen(
                    onCategoryClick = { slug ->
                        navController.navigate(Route.CategoryProducts.createRoute(slug))
                    },
                    onSearch = { query ->
                        navController.navigate(Route.SearchProducts.createRoute(query))
                    }
                )
            }

            composable(Route.Cart.path){
                CartScreen(
                    cartVm = cartVm,
                    clearCart = { cartVm.clearCart() },
                    onGoToHome = {
                        navController.navigate(Route.Home.path){
                            launchSingleTop = true
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            restoreState = false
                        }
                    }
                )
            }

            composable(Route.Favorite.path){
                FavoriteScreen(
                    vm = favoriteVm,
                    cartVm = cartVm,
                    onProductClick = { id ->
                        navController.navigate(Route.ProductInfo.createRoute(id))
                    }
                )
            }

            composable(Route.Profile.path){ ProfileScreen() }

            //побочные
            composable(
                route = Route.ProductInfo.path,
                arguments = listOf(navArgument(Route.ProductInfo.ARG_ID){
                    type = NavType.IntType
                })
            ){ backStackEntry ->
                val id = backStackEntry.arguments?.getInt(Route.ProductInfo.ARG_ID) ?: return@composable
                ProductInfo(
                    favoriteVm = favoriteVm,
                    productId = id,
                    onBackClick = { navController.popBackStack() }
                )
            }

            composable(
                route = Route.CategoryProducts.path,
                arguments = listOf(navArgument(Route.CategoryProducts.ARG_NAME){
                    type = NavType.StringType
                })
            ){ backStackEntry ->
                val category = backStackEntry.arguments?.getString(Route.CategoryProducts.ARG_NAME) ?: return@composable
                CatalogScreen(
                    category = category,
                    onProductClick = { id ->
                        navController.navigate(Route.ProductInfo.createRoute(id))
                    },
                    onBackClick = { navController.popBackStack() },
                    cartVm = cartVm,
                    favoriteVm = favoriteVm
                )
            }

            composable(
                route = Route.SearchProducts.path,
                arguments = listOf(navArgument(Route.SearchProducts.ARG_NAME){
                    type = NavType.StringType
                })
            ){ backStackEntry ->
                val query = backStackEntry.arguments?.getString(Route.SearchProducts.ARG_NAME) ?: return@composable
                SearchProductScreen(
                    query = query,
                    onProductClick = { id ->
                        navController.navigate(Route.ProductInfo.createRoute(id))
                    },
                    onBackClick = { navController.popBackStack() },
                    cartVm = cartVm,
                    favoriteVm = favoriteVm
                )
            }
        }
    }
}