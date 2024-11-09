package com.xml.yandextodo.presentation.add.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xml.yandextodo.R

@Composable
fun AddTaskTopBar(
    onExit: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(vertical = 16.dp)
    ) {
        Icon(
            modifier = Modifier.clickable(onClick = onExit),
            painter = painterResource(R.drawable.ic_close),
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = null
        )
        Spacer(Modifier.weight(1f))
        Text(
            modifier = Modifier.clickable { onSave() },
            text = stringResource(R.string.save).uppercase(),
            color = MaterialTheme.colorScheme.surface,
            fontWeight = FontWeight.W500,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
        )
    }
}