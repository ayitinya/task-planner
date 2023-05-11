package com.ayitinya.taskplanner.data.todolist

import kotlinx.coroutines.flow.Flow

interface TodoListRepository {
    fun getTodoListStream(): Flow<List<TodoList>>

    fun getTodoListStreamById(id: String): Flow<TodoList?>

    suspend fun getTodoListById(id: String): TodoList?

    suspend fun getTodoLists(): List<TodoList>

    suspend fun createTodoList(title: String)

    suspend fun deleteTodoList(todoList: TodoList)
}