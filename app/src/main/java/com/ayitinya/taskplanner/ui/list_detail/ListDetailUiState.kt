package com.ayitinya.taskplanner.ui.list_detail

import com.ayitinya.taskplanner.data.todolist.TodoList
import com.ayitinya.taskplanner.data.todos.Todo

data class ListDetailUiState(
    val todoList: TodoList? = null,
    val todos: List<Todo> = emptyList(),
    val completedTodos: List<Todo> = emptyList(),
    val isTodoListDeleted: Boolean = false
)