package com.xml.yandextodo.presentation.add.composables

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.xml.yandextodo.R
import java.util.Calendar
import java.util.Date

@Composable
fun DatePickerComponent(
    saveDate: (Date) -> Unit,
    selectedDate: String,
    modifier: Modifier = Modifier,
) {
    var onSwitchClicked by remember { mutableStateOf(false) }
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            saveDate(calendar.time)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier
            .weight(1f)
            .clickable { datePickerDialog.show() }) {
            Text(
                text = stringResource(R.string.deadline_text),
                fontWeight = FontWeight.W400,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = selectedDate,
                fontWeight = FontWeight.W400,
                color = MaterialTheme.colorScheme.surface,
                fontSize = 14.sp,
            )

        }
        if (selectedDate.isEmpty()) {
            Icon(
                modifier = Modifier.clickable {
                    onSwitchClicked = !onSwitchClicked
                    datePickerDialog.show()
                },
                painter = painterResource(R.drawable.ic_switch_off),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = null
            )
        } else {
            Icon(
                modifier = Modifier.clickable { onSwitchClicked = !onSwitchClicked },
                painter = painterResource(R.drawable.ic_switch_on),
                tint = MaterialTheme.colorScheme.surface,
                contentDescription = null
            )
        }
    }
}