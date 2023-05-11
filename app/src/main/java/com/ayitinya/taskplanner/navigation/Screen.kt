package com.ayitinya.taskplanner.navigation

import androidx.navigation.NavController

sealed class Screen(val route: String) {
    object HomeScreen : Screen("home")
    object ListDetailScreen : Screen("list_detail/{id}/{title}")
    object TaskDetailScreen : Screen("task_detail/{id}")

    fun navigateToListDetailScreen(id: String, title: String, navController: NavController) {
        navController.navigate("list_detail/$id/$title")
    }

    fun navigateToTaskDetailScreen(id: String, navController: NavController) {
        navController.navigate("task_detail/$id")
    }
}