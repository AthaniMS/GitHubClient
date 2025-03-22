package com.athani.githubclient.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.athani.githubclient.ui.view.GitHubUserDetailsScreen
import com.athani.githubclient.ui.view.GitHubUserListScreen
import com.athani.githubclient.viewmodel.GitHubClientViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "userList") {
        composable("userList") {
            val viewModel: GitHubClientViewModel = hiltViewModel()
            GitHubUserListScreen(viewModel) { username ->
                navController.navigate("userDetail/$username")
            }
        }
        composable("userDetail/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            val viewModel: GitHubClientViewModel = hiltViewModel()
            GitHubUserDetailsScreen(viewModel, username)
        }
    }
}