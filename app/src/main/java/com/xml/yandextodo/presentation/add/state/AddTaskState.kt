package com.xml.yandextodo.presentation.add.state

import com.xml.yandextodo.data.model.Importance
import java.util.Date

data class AddTaskState(
    val id: Long? = null,
    val text: String = "",
    val importance: Importance = Importance.Initial, // Default priority
    val deadline: Date? = null,
    val isCompleted: Boolean = false,
    val createdAt: Date? = null
)