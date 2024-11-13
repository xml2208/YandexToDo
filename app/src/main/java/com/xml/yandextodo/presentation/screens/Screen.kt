package com.xml.yandextodo.presentation.screens

sealed class Screen(val route: String) {

    data object ToDoList : Screen("task_list")

    data object TaskDetail : Screen("add_task/{taskId}") {
        fun createRoute(taskId: String): String = "add_task/$taskId"
    }

}