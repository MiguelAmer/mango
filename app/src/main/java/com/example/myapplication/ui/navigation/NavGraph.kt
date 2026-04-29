package com.example.myapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.favorites.FavoritesScreen
import com.example.favorites.FavoritesViewModel
import com.example.productlist.ProductListScreen
import com.example.productlist.ProductViewModel
import com.example.profile.ProfileScreen
import com.example.profile.ProfileViewModel

sealed class Screen(val route: String) {
    object ProductList : Screen("product_list")
    object Favorites : Screen("favorites")
    object Profile : Screen("profile")
}

@Composable
fun AppNavGraph(navController: NavHostController, productViewModel: ProductViewModel, favoritesViewModel: FavoritesViewModel, profileViewModel: ProfileViewModel) {
    NavHost(navController = navController, startDestination = Screen.ProductList.route) {
        composable(Screen.ProductList.route) {
            ProductListScreen(viewModel = productViewModel)
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen(viewModel = favoritesViewModel)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(viewModel = profileViewModel)
        }
    }
}
