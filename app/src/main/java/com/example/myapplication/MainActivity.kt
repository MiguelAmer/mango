package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.designsystem.theme.MyApplicationTheme
import com.example.favorites.FavoritesViewModel
import com.example.myapplication.ui.navigation.AppNavGraph
import com.example.myapplication.ui.navigation.Screen
import com.example.productlist.ProductViewModel
import com.example.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                val productViewModel: ProductViewModel = hiltViewModel()
                val favoritesViewModel: FavoritesViewModel = hiltViewModel()
                val profileViewModel: ProfileViewModel = hiltViewModel()

                val snackbarHostState = remember { SnackbarHostState() }

                val productErrorMessage by productViewModel.errorMessage.collectAsStateWithLifecycle()

                LaunchedEffect(productErrorMessage) {
                    productErrorMessage?.let { message ->
                        snackbarHostState.showSnackbar(message)
                        productViewModel.clearErrorMessage()
                    }
                }

                val profileErrorMessage by profileViewModel.errorMessage.collectAsStateWithLifecycle()

                LaunchedEffect(profileErrorMessage) {
                    profileErrorMessage?.let { message ->
                        snackbarHostState.showSnackbar(message)
                        profileViewModel.clearErrorMessage()
                    }
                }

                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                    bottomBar = {
                        NavigationBar {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            val items = listOf(
                                Screen.ProductList,
                                Screen.Favorites,
                                Screen.Profile
                            )
                            items.forEach { screen ->
                                NavigationBarItem(
                                    icon = {
                                        Icon(
                                            imageVector = when (screen) {
                                                Screen.ProductList -> Icons.AutoMirrored.Filled.List
                                                Screen.Favorites -> Icons.Default.Favorite
                                                Screen.Profile -> Icons.Default.Person
                                            },
                                            contentDescription = null
                                        )
                                    },
                                    label = {
                                        Text(screen.route.replaceFirstChar { it.uppercase() }
                                            .replace("_", " "))
                                    },
                                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Surface(modifier = Modifier.padding(innerPadding)) {
                        AppNavGraph(navController = navController, productViewModel = productViewModel, favoritesViewModel = favoritesViewModel, profileViewModel = profileViewModel)
                    }
                }
            }
        }
    }
}