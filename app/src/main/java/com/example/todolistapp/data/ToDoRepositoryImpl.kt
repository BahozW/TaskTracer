package com.example.todolistapp.data

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class ToDoRepositoryImpl : ToDoRepository {
    private val auth = Firebase.auth
    private val firestore: FirebaseFirestore = Firebase.firestore
    private val todoCollection = firestore.collection("todos")

    override suspend fun getTodosForDate(dateString: String): List<ToDo> {
        val userId = getCurrentUserId() ?: return emptyList()
        val query = todoCollection
            .whereEqualTo("userId", userId)
            .whereEqualTo("dateString", dateString)
            .get()
            .await()
        return query.toObjects(ToDo::class.java)
    }

    private fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    override suspend fun addTodo(todo: ToDo) {
        val userId = getCurrentUserId() ?: return
        val newTodoDoc = todoCollection.document()
        val todoWithId = todo.copy(id = newTodoDoc.id, userId = userId)
        newTodoDoc.set(todoWithId).await()
    }

    override suspend fun updateTodo(todo: ToDo) {
        val userId = getCurrentUserId() ?: return
        if (todo.userId == userId) {
            todoCollection.document(todo.id).set(todo).await()
        } else {
            Log.e("ToDoRepository", "Unauthorized attempt to update ToDo")
        }
    }

    override suspend fun deleteTodo(todo: ToDo) {
        val userId = getCurrentUserId() ?: return
        if (todo.userId == userId) {
            todoCollection.document(todo.id).delete().await()
        } else {
            Log.e("ToDoRepository", "Unauthorized attempt to delete ToDo")
        }
    }
}
