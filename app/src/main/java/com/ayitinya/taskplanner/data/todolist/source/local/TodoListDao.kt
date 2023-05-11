package com.ayitinya.taskplanner.data.todolist.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoListDao {
    @Query("SELECT * FROM todo_list")
    fun observeAll(): Flow<List<LocalTodoList>>

    @Query("SELECT * FROM todo_list WHERE id = :id")
    fun observeTodoListById(id: String): Flow<LocalTodoList?>

    @Query("SELECT * FROM todo_list")
    suspend fun getAll(): List<LocalTodoList>

    @Query("SELECT * FROM todo_list WHERE id = :id")
    suspend fun getById(id: String): LocalTodoList?

    @Upsert
    suspend fun upsert(todoList: LocalTodoList)

    @Delete
    suspend fun delete(todoList: LocalTodoList)
}