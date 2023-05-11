package com.ayitinya.taskplanner.ui.list_detail

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
import java.time.ZonedDateTime
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ListDetailViewModel @Inject constructor(
    private val todoListRepository: TodoListRepository,
    private val todoRepository: TodoRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _todoListId: String = checkNotNull(savedStateHandle["id"])

    private val _uiState = MutableStateFlow(ListDetailUiState())
    val uiState: StateFlow<ListDetailUiState> = _uiState

    init {
        viewModelScope.launch {
            todoListRepository.getTodoListStreamById(_todoListId).collect { todoList ->
                todoRepository.getTodoStreamByTodoList(_todoListId).collect { todos ->
                    val completedTodos = mutableListOf<Todo>()
                    val uncompletedTodos = mutableListOf<Todo>()
                    todos.forEach { todo ->
                        if (todo.isCompleted) completedTodos.add(todo) else uncompletedTodos.add(todo)
                    }
                    _uiState.update {
                        it.copy(
                            todoList = todoList, todos = uncompletedTodos, completedTodos = completedTodos
                        )
                    }
                }
            }
        }
    }

    suspend fun addTodo(title: String, isCompleted: Boolean = false) {
        withContext(Dispatchers.IO) {
            val id = UUID.randomUUID().toString()
            val todo = Todo(
                id = id, todoList = _todoListId, title = title, isCompleted = isCompleted, date = ZonedDateTime.now())
            todoRepository.createTodo(todo = todo)
        }
    }

    suspend fun toggleTodoCompletionState(todo: Todo, isCompleted: Boolean) {
        withContext(Dispatchers.IO) {
            todoRepository.updateTodo(todo.copy(isCompleted = isCompleted))
        }
    }

    suspend fun deleteList() = viewModelScope.launch {
        uiState.value.todoList?.let { todoListRepository.deleteTodoList(it) }
        _uiState.update { it.copy(isTodoListDeleted = true) }
        todoRepository.deleteMultipleTodos(uiState.value.todos)
    }

}