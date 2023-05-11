package com.ayitinya.taskplanner.data.todolist.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocalTodoList::class], version = 1, exportSchema = false)
abstract class TodoListDatabase: RoomDatabase() {
    abstract fun todoListDao(): TodoListDao
}