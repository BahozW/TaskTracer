package com.example.todolistapp.data

interface ToDoRepository {
    suspend fun getTodosForDate(dateString: String): List<ToDo>
    suspend fun addTodo(todo: ToDo)
    suspend fun updateTodo(todo: ToDo)
    suspend fun deleteTodo(todo: ToDo)

}