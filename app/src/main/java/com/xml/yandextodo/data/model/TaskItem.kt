package com.xml.yandextodo.data.model

import androidx.compose.ui.graphics.Color
import java.util.Date

data class TaskItem(
    val id: Long?,
    var text: String,
    val importance: Importance,
    val deadline: Date?,
    var isCompleted: Boolean,
    val createdAt: Date?,
) {
    companion object {
        val initialTask = TaskItem(
            id = 0L,
            text = "",
            importance = Importance.Initial,
            deadline = Date(),
            isCompleted = false,
            createdAt = null
        )
    }
}

enum class Importance {
    Initial,
    Low,
    High;

    fun toText(): String = when (this) {
        Initial -> "Нет"
        Low -> "Низкий"
        High -> "Высокий"
    }

    fun toColor(initialColor: Color, lowColor: Color, highColor: Color): Color = when (this) {
        Initial -> initialColor
        Low -> lowColor
        High -> highColor
    }
}