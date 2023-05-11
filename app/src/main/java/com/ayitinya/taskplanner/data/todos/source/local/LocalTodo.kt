package com.ayitinya.taskplanner.data.todos.source.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.ZonedDateTime

@Entity(tableName = "todo")
data class LocalTodo(
    @PrimaryKey val id: String,
    val todoList: String,
    val title: String,
    val isCompleted: Boolean,
    val date: ZonedDateTime,
    val note: String? = null,
    val dueDate: ZonedDateTime? = null,
    val reminder: ZonedDateTime? = null
)
