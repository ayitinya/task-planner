package com.ayitinya.taskplanner.data.todolist

import com.ayitinya.taskplanner.data.todolist.source.local.LocalTodoList

fun TodoList.toLocal() = LocalTodoList(
    id = id,
    title = title
)

fun LocalTodoList.toExternal() = TodoList(
    id = id,
    title = title
)

@JvmName("LocalToExternal")
fun List<LocalTodoList>.toExternal() = map(LocalTodoList::toExternal)