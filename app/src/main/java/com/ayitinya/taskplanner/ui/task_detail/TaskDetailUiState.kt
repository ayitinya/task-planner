package com.ayitinya.taskplanner.ui.task_detail

import com.ayitinya.taskplanner.data.todolist.TodoList
import com.ayitinya.taskplanner.data.todos.Todo

data class TaskDetailUiState(
    val todo: Todo? = null, val todoList: TodoList? = null, val isTodoDeleted: Boolean = false
)
