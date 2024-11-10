package com.xml.yandextodo.presentation.main

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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.xml.yandextodo.presentation.add.composables.AddTaskScreen
import com.xml.yandextodo.presentation.list.composables.TaskListScreen
import com.xml.yandextodo.presentation.screens.Screen
import com.xml.yandextodo.presentation.theme.YandexToDoTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            var darkTheme by remember { mutableStateOf(false) }

            YandexToDoTheme(darkTheme = darkTheme) {

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()

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
                                    navController = navController
                                )
                            })
                        composable(
                            route = Screen.AddTask.route,
                            content = { backStackEntry ->
                                val taskItemId = backStackEntry.arguments?.getLong("taskId") ?: -1L
                                AddTaskScreen(navController = navController, taskId = taskItemId)
                            },
                            arguments = listOf(
                                navArgument(
                                    name = "taskId",
                                    builder = { type = NavType.LongType; defaultValue = -1L })
                            ),
                        )
                    }
                }
            }
        }
    }
}