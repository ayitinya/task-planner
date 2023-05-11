package com.ayitinya.taskplanner.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayitinya.taskplanner.data.todolist.TodoListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val todoListRepository: TodoListRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    init {
        viewModelScope.launch {
            todoListRepository.getTodoListStream().collect { todoList ->
                _uiState.update {
                    it.copy(
                        todoLists = todoList
                    )
                }
            }
        }
    }


    suspend fun createTodoList(title: String) {
        todoListRepository.createTodoList(title)
    }
}
