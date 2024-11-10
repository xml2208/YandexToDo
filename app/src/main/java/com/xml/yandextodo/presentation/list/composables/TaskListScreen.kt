package com.xml.yandextodo.presentation.list.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.xml.yandextodo.R
import com.xml.yandextodo.presentation.list.view_model.TaskListViewModel
import com.xml.yandextodo.presentation.screens.Screen
import org.koin.androidx.compose.koinViewModel

@Composable
fun TaskListScreen(
    navController: NavHostController,
    onThemeUpdated: () -> Unit,
    viewModel: TaskListViewModel = koinViewModel(),
) {

    var isToolbarVisible by remember { mutableStateOf(true) }
    val listState = rememberLazyListState()

    val tasks by viewModel.tasks.collectAsState()

    LaunchedEffect(listState.firstVisibleItemScrollOffset) {
        isToolbarVisible = listState.firstVisibleItemScrollOffset == 0
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CustomTopBar(
                modifier = Modifier.padding(
                    start = if (isToolbarVisible) 60.dp else 16.dp,
                    top = if (isToolbarVisible) 82.dp else 16.dp
                ),
                isToolbarVisible = isToolbarVisible,
                showCompleted = false,
                tasks = tasks,
                onHideCompletedClicked = {},
                onThemeUpdated = onThemeUpdated,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(end = 12.dp, bottom = 40.dp),
                onClick = { navController.navigate(Screen.AddTask.createRoute(-1)) },
                shape = RoundedCornerShape(35.dp),
                content = {
                    Icon(
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = null
                    )
                },
                containerColor = MaterialTheme.colorScheme.surface,
            )
        }) { padding ->
        TaskListContainer(
            tasks = tasks,
            listState = listState,
            onCheckedChange = { viewModel.toggleTaskCompletion(it) },
            onEditTask = { navController.navigate(Screen.AddTask.createRoute(taskId = it)) },
            modifier = Modifier
                .padding(padding)
                .padding(8.dp)
        )
    }
}

@Preview
@Composable
private fun MainAppUiPreview() {
    TaskListScreen(
        navController = rememberNavController(),
        onThemeUpdated = {}
    )
}