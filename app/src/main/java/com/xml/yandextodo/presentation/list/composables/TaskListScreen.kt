package com.xml.yandextodo.presentation.list.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.xml.yandextodo.R
import com.xml.yandextodo.presentation.add.composables.LoadingContent
import com.xml.yandextodo.presentation.list.view_model.TaskListEvent
import com.xml.yandextodo.presentation.list.view_model.TaskListState
import com.xml.yandextodo.presentation.list.view_model.TaskListViewModel
import com.xml.yandextodo.presentation.screens.Screen
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun TaskListScreen(
    navController: NavHostController,
    onThemeUpdated: () -> Unit,
    viewModel: TaskListViewModel,
) {
    val listState = rememberLazyListState()
    val viewState = viewModel.viewState.collectAsStateWithLifecycle().value
    val isToolbarVisible by remember { derivedStateOf { listState.firstVisibleItemScrollOffset == 0 } }
    val snackBarHostState = remember { SnackbarHostState() }
    val refreshState = rememberSwipeRefreshState(isRefreshing = viewState == TaskListState.Loading)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.subscribeToEvents()
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
                tasks = emptyList(),
                onHideCompletedClicked = {},
                onThemeUpdated = onThemeUpdated,
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        floatingActionButton = { AddButton(navController) }) { padding ->
        when (viewState) {
            TaskListState.Loading -> LoadingContent(modifier = Modifier.padding(padding))

            is TaskListState.Content -> {
                TaskListContainer(
                    tasks = viewState.taskList,
                    listState = listState,
                    onCheckedChange = { viewModel.setEvent(TaskListEvent.OnCheckedChange(it)) },
                    onEditTask = {
                        viewModel.setEvent(TaskListEvent.GetTask(id = it))
                        navController.navigate(Screen.TaskDetail.createRoute(taskId = it))
                    },
                    onRefresh = { viewModel.setEvent(TaskListEvent.RefreshTodos) },
                    swipeRefreshState = refreshState,
                    modifier = Modifier
                        .padding(padding)
                        .padding(8.dp),
                )
            }

            is TaskListState.Error -> {
                scope.launch {
                    snackBarHostState.showSnackbar(
                        withDismissAction = true,
                        message = viewState.error.orEmpty(),
                        actionLabel = if (viewState.isNetworkError) "Retry" else null,
                        duration = if (viewState.isNetworkError) SnackbarDuration.Long else SnackbarDuration.Short
                    ).also { result ->
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.setEvent(TaskListEvent.RefreshTodos)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AddButton(
    navController: NavHostController,
) {
    FloatingActionButton(
        modifier = Modifier.padding(end = 12.dp, bottom = 40.dp),
        onClick = { navController.navigate(Screen.TaskDetail.createRoute("")) },
        shape = RoundedCornerShape(35.dp),
        content = {
            Icon(
                painter = painterResource(R.drawable.ic_add),
                contentDescription = null
            )
        },
        containerColor = MaterialTheme.colorScheme.surface,
    )
}

//@Preview
//@Composable
//private fun MainAppUiPreview() {
//    TaskListScreen(
//        navController = rememberNavController(),
//        onThemeUpdated = {},
//        factory =
//    )
//}