package com.xml.yandextodo.presentation.list.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xml.yandextodo.R
import com.xml.yandextodo.domain.model.TodoItemUiModel

@Composable
fun CustomTopBar(
    isToolbarVisible: Boolean,
    showCompleted: Boolean,
    tasks: List<TodoItemUiModel>,
    onThemeUpdated: () -> Unit,
    onHideCompletedClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        CustomThemeSwitcher(
            modifier = Modifier
                .align(Alignment.End)
                .padding(bottom = 16.dp),
            onClick = onThemeUpdated
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.my_works),
                style = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.W500,
                    fontSize = if (isToolbarVisible) 32.sp else 20.sp,
                )
            )
            // Icon only shown in collapsed mode
            if (!isToolbarVisible) {
                Spacer(Modifier.weight(1f))
                VisibilityIcon(
                    onHideCompletedClicked = onHideCompletedClicked,
                    showCompleted = showCompleted,
                )
            }
        }

        AnimatedVisibility(
            visible = isToolbarVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.completed) + " - ${tasks.count { it.isCompleted }}",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.W500,
                        fontSize = 16.sp,
                    ),
                    modifier = Modifier.weight(1f)
                )

                VisibilityIcon(
                    onHideCompletedClicked = onHideCompletedClicked,
                    showCompleted = showCompleted,
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
private fun VisibilityIcon(
    onHideCompletedClicked: () -> Unit,
    showCompleted: Boolean,
    modifier: Modifier = Modifier
) {
    Icon(
        modifier = modifier.clickable(onClick = onHideCompletedClicked),
        painter = if (showCompleted) {
            painterResource(R.drawable.ic_visibility)
        } else {
            painterResource(R.drawable.ic_visibility_off)
        },
        tint = MaterialTheme.colorScheme.surface,
        contentDescription = "Toggle completed tasks"
    )
}

@Preview
@Composable
private fun CustomTopBarPreview() {
    CustomTopBar(
        isToolbarVisible = true,
        showCompleted = false,
        tasks = emptyList(),
        onThemeUpdated = {  },
        onHideCompletedClicked = {},
    )
}