package com.example.todolistapp.screens.todo_list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todolistapp.data.ToDo
import com.example.todolistapp.ui.components.BackButton
import com.example.todolistapp.utils.Constants.TODO_LIST_SCREEN

@Composable
fun ToDoListScreen(
    date: String,
    navigateBack: () -> Unit
) {
    val viewModel: ToDoListViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(date) {
        viewModel.loadToDosForDate(date)
    }

    Scaffold(
        scaffoldState = rememberScaffoldState(),
        topBar = {
            ToDoListTopBar(
                navigateBack = navigateBack)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onAddToDoClicked() },
                modifier = Modifier.padding(16.dp),
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add ToDo")
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                LazyColumn {
                    items(state.todos) { todo ->
                        ToDoListItem(
                            todo = todo,
                            onEditClicked = { viewModel.startEditing(todo) },
                            onDeleteClicked = { viewModel.onDeleteToDoClicked(todo) }
                        )
                    }
                }
            }
        }
    )

    if (state.showAddToDoDialog) {
        AddToDoDialog(
            onDismiss = { viewModel.onAddToDoDismiss() },
            onAddToDo = { name, description, location ->
                viewModel.onAddToDo(
                    ToDo(
                        id = "",
                        name = name,
                        description = description,
                        location = location,
                        dateString = date
                    )
                )
            }
        )
    }

    state.editedToDo?.let { editedToDo ->
        EditToDoDialog(
            todo = editedToDo,
            onDismiss = { viewModel.stopEditing() },
            onEditToDo = { updatedName, updatedDescription, updatedLocation ->
                viewModel.onUpdateToDo(
                    editedToDo.copy(
                        name = updatedName,
                        description = updatedDescription,
                        location = updatedLocation
                    )
                )
            }
        )
    }
}

@Composable
fun EditToDoDialog(
    todo: ToDo,
    onDismiss: () -> Unit,
    onEditToDo: (String, String, String) -> Unit
) {
    var name by rememberSaveable { mutableStateOf(todo.name) }
    var description by rememberSaveable { mutableStateOf(todo.description) }
    var location by rememberSaveable { mutableStateOf(todo.location) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit ToDo") },
        confirmButton = {
            Button(
                onClick = {
                    onEditToDo(name, description, location)
                    onDismiss()
                }
            ) {
                Text("Update")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") }
                )
                TextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") }
                )
            }
        }
    )
}

@Composable
fun ToDoListItem(
    todo: ToDo,
    onEditClicked: () -> Unit,
    onDeleteClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = todo.name,
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = todo.description,
                    style = MaterialTheme.typography.body2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = todo.location,
                    style = MaterialTheme.typography.caption
                )
            }
            Row {
                IconButton(onClick = onEditClicked) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = onDeleteClicked) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}


@Composable
fun AddToDoDialog(
    onDismiss: () -> Unit,
    onAddToDo: (name: String, description: String, location: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add ToDo") },
        confirmButton = {
            Button(
                onClick = {
                    onAddToDo(name, description, location)
                    onDismiss()
                }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    )
}

@Composable
fun ToDoListTopBar(
    navigateBack: () -> Unit
) {
    TopAppBar (
        title = {
            Text(
                text = TODO_LIST_SCREEN
            )
        },
        navigationIcon = {
            BackButton(
                navigateBack = navigateBack
            )
        }
    )
}