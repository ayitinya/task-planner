package com.ayitinya.taskplanner.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ayitinya.taskplanner.ui.home.HomeScreen
import com.ayitinya.taskplanner.ui.list_detail.ListDetailScreen
import com.ayitinya.taskplanner.ui.task_detail.TaskDetailScreen

@Composable
fun TaskPlannerNavGraph(
    modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(modifier = modifier, navController = navController)
        }
        composable(route = Screen.ListDetailScreen.route) { navBackStackEntry ->
            navBackStackEntry.arguments?.getString("id")
                ?.let { ListDetailScreen(navController = navController) }
        }
        composable(route = Screen.TaskDetailScreen.route) { navBackStackEntry ->
            navBackStackEntry.arguments?.getString("id")
                ?.let { TaskDetailScreen(modifier = modifier, navController = navController) }
        }
    }
}