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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.xml.yandextodo.R
import com.xml.yandextodo.presentation.add.composables.LoadingContent
import com.xml.yandextodo.presentation.list.view_model.TaskListContract
import com.xml.yandextodo.presentation.list.view_model.TaskListViewModel
import com.xml.yandextodo.presentation.main.TASK_ID_KEY
import com.xml.yandextodo.presentation.main.isInternetAvailable
import com.xml.yandextodo.presentation.screens.Screen
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun TaskListScreen(
    navController: NavHostController,
    onThemeUpdated: () -> Unit,
    viewModel: TaskListViewModel = koinViewModel(),
) {

    var isToolbarVisible by remember { mutableStateOf(true) }
    val listState = rememberLazyListState()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val refreshState = rememberSwipeRefreshState(isRefreshing = viewModel.viewState.value.loading)

    LaunchedEffect(key1 = listState.firstVisibleItemScrollOffset) {
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
                tasks = viewModel.viewState.value.taskList,
                onHideCompletedClicked = {},
                onThemeUpdated = onThemeUpdated,
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(end = 12.dp, bottom = 40.dp),
                onClick = { navController.navigate(Screen.TaskDetail.createRoute(TASK_ID_KEY)) },
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
        when {
            viewModel.viewState.value.loading -> {
                LoadingContent(scaffoldPadding = padding)
            }

            !isInternetAvailable() -> {
                scope.launch {
                    snackBarHostState.showSnackbar(
                        withDismissAction = true,
                        message = "You are offline",
                        actionLabel = "Retry",
                        duration = SnackbarDuration.Indefinite
                    ).also { result ->
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.setEvent(TaskListContract.TaskListEvent.RefreshTodos)
                        }
                    }

                }
            }

            else -> {
                TaskListContainer(
                    tasks = viewModel.viewState.value.taskList,
                    listState = listState,
                    onCheckedChange = {
                        viewModel.setEvent(
                            TaskListContract.TaskListEvent.OnCheckedChange(it)
                        )
                    },
                    onEditTask = {
                        viewModel.setEvent(TaskListContract.TaskListEvent.GetTask(id = it))
                        navController.navigate(Screen.TaskDetail.createRoute(taskId = it))
                    },
                    onRefresh = { viewModel.setEvent(TaskListContract.TaskListEvent.RefreshTodos) },
                    swipeRefreshState = refreshState,
                    modifier = Modifier
                        .padding(padding)
                        .padding(8.dp)
                )
            }

        }

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