package com.example.todolistapp.screens.todo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.data.ToDo
import com.example.todolistapp.data.ToDoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoListViewModel @Inject constructor(
    private val toDoRepository: ToDoRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ToDoListState())
    val state: StateFlow<ToDoListState> = _state.asStateFlow()

    fun loadToDosForDate(dateString: String) {
        viewModelScope.launch {
            val todos = toDoRepository.getTodosForDate(dateString)
            _state.value = _state.value.copy(todos = todos)
        }
    }

    fun onAddToDoClicked() {
        _state.value = _state.value.copy(showAddToDoDialog = true)
    }

    fun onAddToDoDismiss() {
        _state.value = _state.value.copy(showAddToDoDialog = false)
    }

    fun onAddToDo(todo: ToDo) {
        viewModelScope.launch {
            toDoRepository.addTodo(todo)
            loadToDosForDate(todo.dateString)
        }
    }

    fun onDeleteToDoClicked(todo: ToDo) {
        viewModelScope.launch {
            toDoRepository.deleteTodo(todo)
            loadToDosForDate(todo.dateString)
        }
    }

    fun startEditing(todo: ToDo) {
        _state.value = _state.value.copy(editedToDo = todo)
    }

    fun stopEditing() {
        _state.value = _state.value.copy(editedToDo = null)
    }

    fun onUpdateToDo(updatedToDo: ToDo) {
        viewModelScope.launch {
            toDoRepository.updateTodo(updatedToDo)
            loadToDosForDate(updatedToDo.dateString)
            stopEditing()
        }
    }
}

data class ToDoListState(
    val todos: List<ToDo> = emptyList(),
    val showAddToDoDialog: Boolean = false,
    val editedToDo: ToDo? = null
)
