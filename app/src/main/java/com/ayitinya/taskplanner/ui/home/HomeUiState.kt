package com.ayitinya.taskplanner.ui.home

import com.ayitinya.taskplanner.data.todolist.TodoList


data class HomeUiState(
    val todoLists: List<TodoList> = emptyList()
)
