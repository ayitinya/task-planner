package com.ayitinya.taskplanner.data.todos

import com.ayitinya.taskplanner.data.todos.source.local.TodoDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultTodoRepository @Inject constructor(private val localDataSource: TodoDao) :
    TodoRepository {
    override fun getTodoStream(): Flow<List<Todo>> {
        return localDataSource.observerAll().map { it.toExternal() }
    }

    override fun getTodoStreamByTodoList(todoList: String): Flow<List<Todo>> {
        return localDataSource.observeAllByTodoList(todoList = todoList).map { it.toExternal() }
    }

    override fun getTodoById(id: String): Todo? {
        return localDataSource.getTodoById(id)?.toExternal()
    }

    override fun getTodoStreamById(id: String): Flow<Todo?> {
        return localDataSource.observeTodoById(id).map { it?.toExternal() }
    }

    override suspend fun createTodo(todo: Todo) {
        localDataSource.upsert(todo.toLocal())
    }

    override suspend fun updateTodo(todo: Todo) {
        localDataSource.update(todo.toLocal())
    }

    override suspend fun deleteTodo(todo: Todo) {
        localDataSource.delete(todo.toLocal())
    }

    override suspend fun deleteMultipleTodos(todos: List<Todo>) {
        localDataSource.deleteMultiple(todos.toLocal())
    }
}