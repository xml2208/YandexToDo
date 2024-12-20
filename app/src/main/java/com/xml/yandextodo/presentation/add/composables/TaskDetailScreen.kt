package com.xml.yandextodo.presentation.add.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.xml.yandextodo.R
import com.xml.yandextodo.domain.model.Importance
import com.xml.yandextodo.domain.model.TodoItemUiModel
import com.xml.yandextodo.presentation.add.view_model.TaskDetailEvent
import com.xml.yandextodo.presentation.add.view_model.TaskDetailState
import com.xml.yandextodo.presentation.add.view_model.TaskDetailViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.util.Date

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun TaskDetailScreen(
    navController: NavHostController,
    taskId: String,
    viewModel: TaskDetailViewModel = koinViewModel(),
) {
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val viewState = viewModel.viewState.collectAsState().value

    LaunchedEffect(key1 = taskId) {
        viewModel.subscribeToEvents()
        viewModel.setEvent(TaskDetailEvent.OnLoad(id = taskId))
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
    ) { padding ->
        when (viewState) {
            TaskDetailState.Loading -> {
                LoadingContent(modifier = Modifier.padding(top = 24.dp))
            }

            is TaskDetailState.Content -> {
                TaskDetailContent(
                    viewState = viewState,
                    setEvent = viewModel::setEvent,
                    taskId = taskId,
                    navigateUp = { navController.navigateUp() },
                    togglePriority = viewModel::togglePriority,
                    formatDate = viewModel::formatDate,
                )
            }

            is TaskDetailState.Error -> {
                scope.launch {
                    snackBarHostState.showSnackbar(
                        withDismissAction = true,
                        message = viewState.message.orEmpty(),
                        actionLabel = if (viewState.isNetworkError) "Retry" else null,
                        duration = SnackbarDuration.Indefinite
                    ).also { result ->
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.setEvent(TaskDetailEvent.OnLoad(id = taskId))
                        }
                    }
                }
            }

            TaskDetailState.OnDone -> {
                navController.navigateUp()
            }
        }
    }
}

@Composable
private fun TaskDetailContent(
    viewState: TaskDetailState.Content,
    taskId: String,
    setEvent: (TaskDetailEvent) -> Unit,
    navigateUp: () -> Unit,
    togglePriority: (Importance) -> Unit,
    formatDate: (Date?) -> String?,
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
    ) {
        AddTaskTopBar(
            onExit = navigateUp,
            onSave = {
                if (viewState.taskTitle.isNotBlank()) {
                    setEvent(
                        TaskDetailEvent.OnSave(
                            tasItemUiModel = TodoItemUiModel(
                                id = viewState.id,
                                text = viewState.taskTitle,
                                importance = viewState.importance,
                                deadline = viewState.deadline,
                                isCompleted = viewState.isCompleted,
                            )
                        )
                    )
                } else {
                    setEvent(TaskDetailEvent.OnError(message = "Имя задачи пустое, пожалуйста, добавьте"))
                }
            },
            modifier = Modifier.padding(16.dp)
        )

        TaskTitleTextField(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 28.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onBackground),
            taskTitle = viewState.taskTitle,
            onValueChange = {
                setEvent(TaskDetailEvent.OnTitleValueChanged(it))
            }
        )

        Text(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            text = stringResource(R.string.priority),
            fontSize = 16.sp,
            fontWeight = FontWeight.W400,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colorScheme.primary,
        )

        PriorityDropdownMenu(
            selectedText = viewState.importance,
            onPrioritySelected = { importance -> togglePriority(importance) },
            modifier = Modifier.padding(top = 4.dp, start = 16.dp, bottom = 16.dp)
        )

        HorizontalDivider()

        DatePickerComponent(
            selectedDate = formatDate(viewState.deadline).orEmpty(),
            saveDate = { setEvent(TaskDetailEvent.OnDeadlineChanged(it)) },
            modifier = Modifier.padding(16.dp)
        )

        Spacer(Modifier.height(24.dp))

        HorizontalDivider()

        DeleteTaskRow(
            modifier = Modifier.padding(vertical = 20.dp, horizontal = 16.dp),
            deleteTask = {
                if (taskId.isNotBlank()) {
                    setEvent(TaskDetailEvent.DeleteTask(taskId))
                }
            },
            isEnabled = taskId.isNotBlank(),
        )
    }
}


@Composable
private fun PriorityDropdownMenu(
    selectedText: Importance,
    onPrioritySelected: (Importance) -> Unit,
    modifier: Modifier = Modifier,
) {

    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = selectedText.text,
            fontSize = 14.sp,
            fontWeight = FontWeight.W400,
            color = selectedText.toColor(
                initialColor = MaterialTheme.colorScheme.tertiary,
                lowColor = MaterialTheme.colorScheme.primary,
                highColor = MaterialTheme.colorScheme.errorContainer
            ),
            modifier = Modifier
                .clickable { expanded = true }
                .fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.onSurface)
        ) {
            Importance.entries.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option.text,
                            color = option.toColor(
                                initialColor = MaterialTheme.colorScheme.primary,
                                lowColor = MaterialTheme.colorScheme.primary,
                                highColor = MaterialTheme.colorScheme.errorContainer
                            ),
                            fontSize = 16.sp,
                            fontWeight = if (option == Importance.IMPORTANT) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    onClick = {
                        onPrioritySelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun DeleteTaskRow(
    isEnabled: Boolean,
    deleteTask: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clickable(enabled = isEnabled) { deleteTask() }
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_delete),
            tint = if (isEnabled) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.onErrorContainer,
            contentDescription = null,
        )

        Text(
            text = stringResource(R.string.delete),
            fontWeight = FontWeight.W400,
            fontSize = 16.sp,
            color = if (isEnabled) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.onErrorContainer
        )
    }
}


@Preview
@Composable
private fun AddTaskScreenPreview() {
    TaskDetailScreen(navController = rememberNavController(), "")
}

@Preview(showBackground = true)
@Composable
private fun DeleteTaskRowPreview() {
    DeleteTaskRow(
        deleteTask = {},
        isEnabled = false,
    )
}