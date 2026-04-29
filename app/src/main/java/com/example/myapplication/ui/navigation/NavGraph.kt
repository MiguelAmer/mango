package com.example.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.ui.favorites.FavoritesScreen
import com.example.myapplication.ui.product_list.ProductListScreen
import com.example.myapplication.ui.profile.ProfileScreen
import com.example.myapplication.ui.viewmodel.ProductViewModel

sealed class Screen(val route: String) {
    object ProductList : Screen("product_list")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
}

@Composable
fun AppNavGraph(navController: NavHostController, viewModel: ProductViewModel) {
    NavHost(navController = navController, startDestination = Screen.ProductList.route) {
        composable(Screen.ProductList.route) {
            ProductListScreen(viewModel = viewModel)
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen(viewModel = viewModel)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(viewModel = viewModel)
        }
    }
}
