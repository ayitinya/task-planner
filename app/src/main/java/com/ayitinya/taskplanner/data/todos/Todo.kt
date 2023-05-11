package com.ayitinya.taskplanner.data.todos

import java.time.ZonedDateTime

data class Todo(
    val id: String,
    val todoList: String,
    val title: String,
    val isCompleted: Boolean,
    val date: ZonedDateTime,
    val note: String? = null,
    val dueDate: ZonedDateTime? = null,
    val reminder: ZonedDateTime? = null
)
