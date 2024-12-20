package com.xml.yandextodo.presentation.list.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.xml.yandextodo.R
import com.xml.yandextodo.domain.model.Importance
import com.xml.yandextodo.domain.model.TodoItemUiModel

@Composable
fun TaskListContainer(
    listState: LazyListState,
    swipeRefreshState: SwipeRefreshState,
    tasks: List<TodoItemUiModel>,
    onCheckedChange: (TodoItemUiModel) -> Unit,
    onRefresh: () -> Unit,
    onEditTask: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    SwipeRefresh(state = swipeRefreshState, onRefresh = onRefresh) {
        Column(
            modifier = modifier.background(
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(8.dp)
            )
        ) {
            Spacer(Modifier.height(10.dp))
            LazyColumn(state = listState) {
                items(items = tasks) { task ->
                    TaskItemRow(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .background(MaterialTheme.colorScheme.onBackground),
                        todoItemUiModel = task,
                        isChecked = task.isCompleted,
                        onCheckedChange = { onCheckedChange(task) },
                        onEditTask = { onEditTask(task.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskItemRow(
    todoItemUiModel: TodoItemUiModel,
    isChecked: Boolean,
    onCheckedChange: (TodoItemUiModel) -> Unit,
    onEditTask: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = { onCheckedChange(todoItemUiModel) },
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.inverseSurface,
                uncheckedColor = if (todoItemUiModel.importance == Importance.IMPORTANT) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.inversePrimary,
                checkmarkColor = MaterialTheme.colorScheme.background
            )
        )

        Row(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    Log.d("xml22", "TaskItemRow: ${todoItemUiModel.id}")
                    onEditTask(todoItemUiModel.id)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (todoItemUiModel.importance == Importance.IMPORTANT) {
                Text(
                    text = "!!",
                    color = MaterialTheme.colorScheme.errorContainer,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400,
                )
            } else if (todoItemUiModel.importance == Importance.LOW) {
                Text(
                    text = "↓",
                    color = MaterialTheme.colorScheme.surfaceContainer,
                    fontWeight = FontWeight.Bold,
                )
            }
            Text(
                text = todoItemUiModel.text,
                color = if (isChecked) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
                fontSize = 16.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.W400,
                style = TextStyle(textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None)
            )
        }

        Icon(
            painter = painterResource(R.drawable.ic_info),
            tint = MaterialTheme.colorScheme.tertiary,
            contentDescription = null,
        )
    }
}


@Preview
@Composable
private fun TaskItemWithHighImportance() {
    TaskItemRow(
        todoItemUiModel = TodoItemUiModel(
            id = "0",
            text = "Go to the bank",
            importance = Importance.IMPORTANT,
            deadline = null,
            isCompleted = true,
            createdAt = null
        ),
        onEditTask = {},
        isChecked = true,
        onCheckedChange = {}
    )
}

@Preview
@Composable
private fun TaskItemWithLowImportance() {
    TaskItemRow(
        todoItemUiModel = TodoItemUiModel(
            id = "",
            text = "Go to the bank",
            importance = Importance.LOW,
            deadline = null,
            isCompleted = true,
            createdAt = null
        ),
        isChecked = true,
        onCheckedChange = {},
        onEditTask = {}
    )
}

@Preview
@Composable
private fun UnCheckedTaskItem() {
    TaskItemRow(
        todoItemUiModel = TodoItemUiModel(
            id = "0",
            text = "Go to the bank",
            importance = Importance.LOW,
            deadline = null,
            isCompleted = false,
            createdAt = null
        ),
        isChecked = false,
        onEditTask = {},
        onCheckedChange = {}
    )
}