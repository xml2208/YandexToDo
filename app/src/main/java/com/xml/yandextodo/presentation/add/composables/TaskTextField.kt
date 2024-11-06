package com.xml.yandextodo.presentation.add.composables

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xml.yandextodo.R

@Composable
fun TaskTitleTextField(
    taskTitle: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        modifier = modifier.heightIn(150.dp),
        value = taskTitle,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = stringResource(R.string.to_do_title_hint),
                color = MaterialTheme.colorScheme.tertiary
            )
        },
        textStyle = TextStyle(fontSize = 16.sp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedLeadingIconColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            errorContainerColor = Color.Unspecified,
            focusedTextColor = MaterialTheme.colorScheme.primary,
            unfocusedTextColor = MaterialTheme.colorScheme.tertiary,
            unfocusedContainerColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

@Preview
@Composable
private fun TaskTitleTextFieldPreview() {
    TaskTitleTextField(
        taskTitle = "Cleaning",
        onValueChange = { _ -> },
        modifier = Modifier.padding(top = 50.dp)
    )
}