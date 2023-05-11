package com.ayitinya.taskplanner.data.todos.source.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo")
    fun observerAll(): Flow<List<LocalTodo>>

    @Query("SELECT * FROM todo WHERE todoList = :todoList")
    fun observeAllByTodoList(todoList: String): Flow<List<LocalTodo>>

    @Query("SELECT * FROM todo WHERE id =:id")
    fun getTodoById(id: String): LocalTodo?

    @Query("SELECT * FROM todo WHERE id =:id")
    fun observeTodoById(id: String): Flow<LocalTodo?>

    @Upsert
    suspend fun upsert(todo: LocalTodo)

    @Update
    suspend fun update(todo: LocalTodo)

    @Delete
    suspend fun delete(todo: LocalTodo)

    @Delete
    suspend fun deleteMultiple(todos: List<LocalTodo>)
}