package com.example.tuan_6_task_1.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tuan_6_task_1.ui.screens.TaskDetailScreen
import com.example.tuan_6_task_1.ui.screens.TaskListScreen
import com.example.tuan_6_task_1.ui.viewmodel.TaskViewModel

@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    val taskViewModel: TaskViewModel = viewModel()

    NavHost(navController = navController, startDestination = "taskList") {
        composable("taskList") {
            TaskListScreen(navController = navController, viewModel = taskViewModel)
        }
        composable(
            route = "taskDetail/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.StringType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")
            requireNotNull(taskId)
            TaskDetailScreen(navController = navController, viewModel = taskViewModel, taskId = taskId)
        }
    }
}