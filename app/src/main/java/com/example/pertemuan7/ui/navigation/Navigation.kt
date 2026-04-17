package com.example.pertemuan7.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pertemuan7.ui.screens.TaskListScreen
import com.example.pertemuan7.ui.screens.TaskFormScreen
import com.example.pertemuan7.viewmodel.TaskViewModel

sealed class Screen(val route: String) {
    object TaskList : Screen("task_list")
    object TaskForm : Screen("task_form")
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    viewModel: TaskViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.TaskList.route
    ) {
        composable(Screen.TaskList.route) {
            TaskListScreen(
                viewModel = viewModel,
                onAddTask = { navController.navigate(Screen.TaskForm.route) },
                onEditTask = { task -> 
                    navController.navigate(Screen.TaskForm.route)
                }
            )
        }
        composable(Screen.TaskForm.route) {
            TaskFormScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
