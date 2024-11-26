package com.xml.yandextodo.presentation.main

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.xml.yandextodo.YandexToDoApp
import com.xml.yandextodo.presentation.add.composables.TaskDetailScreen
import com.xml.yandextodo.presentation.add.view_model.TaskDetailViewModel
import com.xml.yandextodo.presentation.list.composables.TaskListScreen
import com.xml.yandextodo.presentation.list.view_model.TaskListViewModel
import com.xml.yandextodo.presentation.screens.Screen
import com.xml.yandextodo.presentation.theme.YandexToDoTheme
import javax.inject.Inject

const val TASK_ID_KEY = "taskId"

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var taskListViewModel: TaskListViewModel

    @Inject
    lateinit var taskDetailViewModel: TaskDetailViewModel

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (application as YandexToDoApp).appComponent.inject(this)

        enableEdgeToEdge()
        setContent {
            var darkTheme by remember { mutableStateOf(false) }
            val navController = rememberNavController()

            YandexToDoTheme(darkTheme = darkTheme) {

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = Screen.ToDoList.route
                    ) {
                        composable(
                            route = Screen.ToDoList.route,
                            content = {
                                TaskListScreen(
                                    onThemeUpdated = { darkTheme = !darkTheme },
                                    navController = navController,
                                    viewModel = taskListViewModel,
                                )
                            })
                        composable(
                            route = Screen.TaskDetail.route,
                            content = { backStackEntry ->
                                val taskItemId =
                                    backStackEntry.arguments?.getString(TASK_ID_KEY)
                                        .orEmpty()
                                TaskDetailScreen(
                                    navController = navController,
                                    taskId = taskItemId,
                                    viewModel = taskDetailViewModel
                                )
                            },
                            arguments = listOf(
                                navArgument(
                                    name = TASK_ID_KEY,
                                    builder = {
                                        type = NavType.StringType; defaultValue = ""
                                    })
                            ),
                        )
                    }
                }
            }
        }
    }
}