package com.example.todolistapp.data

data class ToDo(
    val id: String = "",
    val userId: String ="",
    val name: String = "",
    val description: String = "",
    val location: String = "",
    val dateString: String = ""
)