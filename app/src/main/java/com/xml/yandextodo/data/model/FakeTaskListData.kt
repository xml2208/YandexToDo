package com.xml.yandextodo.data.model

import java.util.Calendar
import java.util.Date
import kotlin.random.Random

object FakeTaskListData {

    val harCodedList =
        mutableListOf(
            TaskItem(
                id = 0,
                text = "Buy groceries",
                importance = Importance.Low,
                deadline = null,
                isCompleted = false,
                createdAt = null
            ),
            TaskItem(
                id = 1,
                text = "Go to the bank",
                importance = Importance.High,
                deadline = null,
                isCompleted = true,
                createdAt = null
            ),
            TaskItem(
                id = 2,
                text = "Sell products",
                importance = Importance.Low,
                deadline = null,
                isCompleted = true,
                createdAt = null
            ),
            TaskItem(
                id = 3,
                text = "Visit Grandparents'",
                importance = Importance.High,
                deadline = null,
                isCompleted = false,
                createdAt = null
            ),
            TaskItem(
                id = 4,
                text = "Buy cosmetics",
                importance = Importance.Low,
                deadline = null,
                isCompleted = true,
                createdAt = null
            ),
            TaskItem(
                id = 5,
                text = "Drink",
                importance = Importance.Low,
                deadline = null,
                isCompleted = false,
                createdAt = null
            ),
            TaskItem(
                id = 6,
                text = "Buy groceries",
                importance = Importance.High,
                deadline = null,
                isCompleted = false,
                createdAt = null
            ),
            TaskItem(
                id = 7,
                text = "Buy groceries",
                importance = Importance.Low,
                deadline = null,
                isCompleted = true,
                createdAt = null
            ),
            TaskItem(
                id = 8,
                text = "Buy groceries",
                importance = Importance.Initial,
                deadline = null,
                isCompleted = false,
                createdAt = null
            ),
            TaskItem(
                id = 9,
                text = "Buy groceries",
                importance = Importance.Initial,
                deadline = null,
                isCompleted = false,
                createdAt = null
            ),
            TaskItem(
                id = 10,
                text = "Buy groceries",
                importance = Importance.Initial,
                deadline = null,
                isCompleted = false,
                createdAt = null
            ),
            TaskItem(
                id = 11,
                text = "Buy groceries",
                importance = Importance.Initial,
                deadline = null,
                isCompleted = false,
                createdAt = null
            ),
            TaskItem(
                id = 12,
                text = "Buy groceries",
                importance = Importance.High,
                deadline = null,
                isCompleted = false,
                createdAt = null
            ),
            TaskItem(
                id = 13,
                text = "Buy groceries",
                importance = Importance.High,
                deadline = null,
                isCompleted = false,
                createdAt = null
            ),
            TaskItem(
                id = 14,
                text = "Buy groceries",
                importance = Importance.Initial,
                deadline = null,
                isCompleted = false,
                createdAt = null
            ),
            TaskItem(
                id = 15,
                text = "Buy groceries",
                importance = Importance.Initial,
                deadline = null,
                isCompleted = false,
                createdAt = null
            ),
            TaskItem(
                id = 16,
                text = "Buy groceries",
                importance = Importance.Initial,
                deadline = null,
                isCompleted = false,
                createdAt = null
            ),
            TaskItem(
                id = 17,
                text = "Buy groceries",
                importance = Importance.High,
                deadline = null,
                isCompleted = false,
                createdAt = null
            ),
        )

    fun generateTaskList(): List<TaskItem> {
        val tasks = mutableListOf<TaskItem>()
        val now = Date()
        val calendar = Calendar.getInstance()

        repeat(20) { i ->
            calendar.time = now
            calendar.add(
                Calendar.DAY_OF_YEAR,
                Random.nextInt(1, 30)
            ) // Random deadline within 30 days
            val deadline = calendar.time

            tasks.add(
                TaskItem(
                    id = i.toLong(),
                    text = "Task ${i + 1}",
                    importance = Importance.entries.toTypedArray().random(),
                    deadline = deadline,
                    isCompleted = Random.nextBoolean(),
                    createdAt = now
                    // modifiedAt = now
                )
            )
        }
        return tasks
    }
}