package com.xml.yandextodo.presentation.list.composables

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.xml.yandextodo.R
import com.xml.yandextodo.domain.model.TodoItemUiModel
import com.xml.yandextodo.presentation.add.composables.LoadingContent
import com.xml.yandextodo.presentation.list.view_model.TaskListEvent
import com.xml.yandextodo.presentation.list.view_model.TaskListState
import com.xml.yandextodo.presentation.list.view_model.TaskListViewModel
import com.xml.yandextodo.presentation.main.TASK_ID_KEY
import com.xml.yandextodo.presentation.screens.Screen
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun TaskListScreen(
    navController: NavHostController,
    onThemeUpdated: () -> Unit,
    viewModel: TaskListViewModel = koinViewModel(),
) {
    val listState = rememberLazyListState()
    val viewState = viewModel.viewState.collectAsStateWithLifecycle().value
    val isInternetAvailable = viewModel.internetAvailable.collectAsState(false).value
    val isToolbarVisible by remember { derivedStateOf { listState.firstVisibleItemScrollOffset == 0 } }
    val snackBarHostState = remember { SnackbarHostState() }
    val refreshState = rememberSwipeRefreshState(isRefreshing = viewState == TaskListState.Loading)
    val context = LocalContext.current


    LaunchedEffect(Unit) {
        viewModel.subscribeToEvents()
    }
    LaunchedEffect(Unit) {
        viewModel.setEvent(TaskListEvent.RefreshTodos)
    }

    when (viewState) {
        TaskListState.Loading -> LoadingContent()

        is TaskListState.Content -> {
            TaskListContent(
                isToolbarVisible = isToolbarVisible,
                taskList = viewState.taskList,
                onThemeUpdated = onThemeUpdated,
                snackBarHostState = snackBarHostState,
                navController = navController,
                isInternetAvailable = isInternetAvailable,
                refreshState = refreshState,
                listState = listState,
                setEvent = viewModel::setEvent
            )
        }

        is TaskListState.Error -> {
            Toast.makeText(context, viewState.error, Toast.LENGTH_SHORT).show()
        }
    }

}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun TaskListContent(
    isToolbarVisible: Boolean,
    taskList: List<TodoItemUiModel>,
    onThemeUpdated: () -> Unit,
    snackBarHostState: SnackbarHostState,
    navController: NavHostController,
    isInternetAvailable: Boolean,
    refreshState: SwipeRefreshState,
    listState: LazyListState,
    setEvent: (TaskListEvent) -> Unit,
) {
    val scope = rememberCoroutineScope()

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
                tasks = taskList,
                onHideCompletedClicked = {},
                onThemeUpdated = onThemeUpdated,
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) },
        floatingActionButton = { AddButton(navController) }) { padding ->
        when {
            !isInternetAvailable -> {
                scope.launch {
                    snackBarHostState.showSnackbar(
                        withDismissAction = true,
                        message = "You are offline",
                        actionLabel = "Retry",
                        duration = SnackbarDuration.Indefinite
                    ).also { result ->
                        if (result == SnackbarResult.ActionPerformed) {
                            setEvent(TaskListEvent.RefreshTodos)
                        }
                    }
                }
            }

            else -> {
                TaskListContainer(
                    tasks = taskList,
                    listState = listState,
                    onCheckedChange = { setEvent(TaskListEvent.OnCheckedChange(it)) },
                    onEditTask = {
                        setEvent(TaskListEvent.GetTask(id = it))
                        navController.navigate(Screen.TaskDetail.createRoute(taskId = it))
                    },
                    onRefresh = { setEvent(TaskListEvent.RefreshTodos) },
                    swipeRefreshState = refreshState,
                    modifier = Modifier
                        .padding(padding)
                        .padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun AddButton(
    navController: NavHostController,
) {
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
}

@Preview
@Composable
private fun MainAppUiPreview() {
    TaskListScreen(
        navController = rememberNavController(),
        onThemeUpdated = {}
    )
}