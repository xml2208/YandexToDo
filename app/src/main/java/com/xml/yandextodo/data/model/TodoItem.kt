package com.xml.yandextodo.data.model

import androidx.compose.ui.graphics.Color
import com.xml.yandextodo.data.mapper.Mapper
import java.util.Date

data class TodoItem(
    val id: Long?,
    val text: String,
    val importance: Importance,
    val deadline: Date?,
    val isCompleted: Boolean,
    val createdAt: Date?,
    val modifiedAt: Date? = null,
) {
    companion object {
        val initialTask = TodoItem(
            id = 0L,
            text = "",
            importance = Importance.Initial,
            deadline = Date(),
            isCompleted = false,
            createdAt = null
        )
        val initialTaskEntity = Mapper().toEntity(initialTask)
    }
}

enum class Importance(val text: String) {
    Initial("Нет"),
    Low("Низкий"),
    High("Высокий");

    fun toColor(initialColor: Color, lowColor: Color, highColor: Color): Color = when (this) {
        Initial -> initialColor
        Low -> lowColor
        High -> highColor
    }
}