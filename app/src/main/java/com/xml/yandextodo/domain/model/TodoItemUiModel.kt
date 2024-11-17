package com.xml.yandextodo.domain.model

import androidx.compose.ui.graphics.Color
import java.util.Date
import kotlin.random.Random

data class TodoItemUiModel(
    val id: String,
    val text: String,
    val importance: Importance,
    val deadline: Date?,
    val isCompleted: Boolean,
    val createdAt: Date? = null,
    val changed_at: Date? = null,
    val last_updated_by: String = "",
) {
    companion object {
        val initialTask = TodoItemUiModel(
            id = Random.nextInt().toString(),
            text = "",
            importance = Importance.BASIC,
            deadline = Date(),
            isCompleted = false,
            createdAt = Date()
        )
//        val initialTaskEntity = Mapper().toEntity(initialTask)
    }
}

enum class Importance(val text: String) {
    BASIC("Нет"),
    LOW("Низкий"),
    IMPORTANT("Высокий");

    fun toColor(initialColor: Color, lowColor: Color, highColor: Color): Color = when (this) {
        BASIC -> initialColor
        LOW -> lowColor
        IMPORTANT -> highColor
    }
}