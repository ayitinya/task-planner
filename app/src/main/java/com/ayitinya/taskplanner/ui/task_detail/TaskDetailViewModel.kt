package com.ayitinya.taskplanner.ui.task_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayitinya.taskplanner.data.todolist.TodoListRepository
import com.ayitinya.taskplanner.data.todos.Todo
import com.ayitinya.taskplanner.data.todos.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    private val todoListRepository: TodoListRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskDetailUiState())
    val uiState: StateFlow<TaskDetailUiState> = _uiState

    private val _todoId: String = checkNotNull(savedStateHandle["id"])


    init {
        viewModelScope.launch {
            todoRepository.getTodoStreamById(_todoId).collect { todo ->
                val todoList = todo?.let { todoListRepository.getTodoListById(it.todoList) }
                _uiState.update { it.copy(todo = todo, todoList = todoList) }
            }
        }
    }

    suspend fun updateTodo(todo: Todo) {
        withContext(Dispatchers.IO) {
            todoRepository.updateTodo(todo)
        }
    }

//    suspend fun deleteTodo(todo: Todo) {
//        withContext(Dispatchers.IO) {
//            todoRepository.deleteTodo(todo)
//            _uiState.update { it.copy(isTodoDeleted = true) }
//        }
//    }

    suspend fun deleteTodo(todo: Todo) = viewModelScope.launch {
        todoRepository.deleteTodo(todo)
        _uiState.update { it.copy(isTodoDeleted = true) }
    }
}