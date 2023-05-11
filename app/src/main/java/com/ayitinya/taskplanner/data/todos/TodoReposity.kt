package com.ayitinya.taskplanner.data.todos

import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getTodoStream(): Flow<List<Todo>>

    fun getTodoStreamByTodoList(todoList: String): Flow<List<Todo>>

    fun getTodoById(id: String): Todo?

    fun getTodoStreamById(id: String): Flow<Todo?>

    suspend fun createTodo(todo: Todo)

    suspend fun updateTodo(todo: Todo)

    suspend fun deleteTodo(todo: Todo)

    suspend fun deleteMultipleTodos(todos: List<Todo>)
}