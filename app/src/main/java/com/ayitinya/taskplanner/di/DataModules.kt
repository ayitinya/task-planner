package com.ayitinya.taskplanner.di

import android.content.Context
import androidx.room.Room
import com.ayitinya.taskplanner.data.todolist.DefaultTodoListRepository
import com.ayitinya.taskplanner.data.todolist.TodoListRepository
import com.ayitinya.taskplanner.data.todolist.source.local.TodoListDao
import com.ayitinya.taskplanner.data.todolist.source.local.TodoListDatabase
import com.ayitinya.taskplanner.data.todos.DefaultTodoRepository
import com.ayitinya.taskplanner.data.todos.TodoRepository
import com.ayitinya.taskplanner.data.todos.source.local.TodoDao
import com.ayitinya.taskplanner.data.todos.source.local.TodoDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindTodoListRepository(repository: DefaultTodoListRepository): TodoListRepository

    @Singleton
    @Binds
    abstract fun binTodoRepository(repository: DefaultTodoRepository): TodoRepository

}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideTodoListDatabase(@ApplicationContext context: Context): TodoListDatabase {
        return Room.databaseBuilder(
            context.applicationContext, TodoListDatabase::class.java, "todo_list.db"
        ).build()
    }

    @Provides
    fun provideTodoListDao(database: TodoListDatabase): TodoListDao = database.todoListDao()


    @Singleton
    @Provides
    fun provideTodoDatabase(@ApplicationContext context: Context): TodoDatabase {
        return Room.databaseBuilder(context.applicationContext, TodoDatabase::class.java, "todo.db").build()
    }

    @Provides
    fun provideTodoDao(database: TodoDatabase): TodoDao = database.todoDao()
}