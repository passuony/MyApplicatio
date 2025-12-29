package com.example.myapplication.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.di.AppContainer
import com.example.myapplication.ui.list.BooksListScreen
import com.example.myapplication.ui.favorites.FavoritesScreen
import com.example.myapplication.ui.profile.ProfileTabScreen
import com.example.myapplication.ui.profile.EditProfileScreen
import com.example.myapplication.viewmodel.ProfileViewModel
import com.example.myapplication.viewmodel.ProfileViewModelFactory

@Composable
fun MainScreen(appContainer: AppContainer) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == "books",
                    onClick = { navController.navigate("books") },
                    icon = { Icon(Icons.AutoMirrored.Filled.List, null) },
                    label = { Text("Books") }
                )
                NavigationBarItem(
                    selected = currentRoute == "favorites",
                    onClick = { navController.navigate("favorites") },
                    icon = { Icon(Icons.Filled.Favorite, null) },
                    label = { Text("Favorites") }
                )
                NavigationBarItem(
                    selected = currentRoute == "profile",
                    onClick = { navController.navigate("profile") },
                    icon = { Icon(Icons.Filled.Person, null) },
                    label = { Text("Profile") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(navController, "books", Modifier.padding(innerPadding)) {
            composable("books") {
                BooksListScreen(viewModel(factory = appContainer.getBooksListViewModelFactory()), onBookClick = { /*...*/ })
            }
            composable("favorites") {
                FavoritesScreen(appContainer, onBookClick = { /*...*/ })
            }
            composable("profile") {
                val vm: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(appContainer.profileDataStore))
                ProfileTabScreen(vm) { navController.navigate("edit_profile") }
            }
            composable("edit_profile") {
                val vm: ProfileViewModel = viewModel(factory = ProfileViewModelFactory(appContainer.profileDataStore))
                EditProfileScreen(vm, onDone = { navController.popBackStack() }, onCancel = { navController.popBackStack() })
            }
        }
    }
}