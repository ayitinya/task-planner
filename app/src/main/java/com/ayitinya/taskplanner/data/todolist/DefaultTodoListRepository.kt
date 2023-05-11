package com.ayitinya.taskplanner.data.todolist

import com.ayitinya.taskplanner.data.todolist.source.local.TodoListDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultTodoListRepository @Inject constructor(
    private val localDataSource: TodoListDao
) : TodoListRepository {
    override fun getTodoListStream(): Flow<List<TodoList>> {
        return localDataSource.observeAll().map { value -> value.toExternal() }
    }

    override fun getTodoListStreamById(id: String): Flow<TodoList?> {
        return localDataSource.observeTodoListById(id).map { it?.toExternal() }
    }

    override suspend fun getTodoListById(id: String): TodoList? {
        return withContext(Dispatchers.IO) {
            localDataSource.getById(id)?.toExternal()
        }
    }

    override suspend fun getTodoLists(): List<TodoList> {
        return withContext(Dispatchers.IO) {
            localDataSource.getAll().toExternal()
        }
    }

    override suspend fun createTodoList(title: String) {
        withContext(Dispatchers.IO) {
            val id = UUID.randomUUID().toString()
            val todoList = TodoList(title = title, id = id)
            localDataSource.upsert(todoList.toLocal())
        }
    }

    override suspend fun deleteTodoList(todoList: TodoList) {
        withContext(Dispatchers.IO) {
            localDataSource.delete(todoList = todoList.toLocal())
        }
    }
}