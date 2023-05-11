package com.ayitinya.taskplanner.data.todolist.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_list")
data class LocalTodoList(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "title") val title: String,
)