package com.example.pertemuan7.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.pertemuan7.model.Task
import com.example.pertemuan7.ui.state.TaskListUIState
import com.example.pertemuan7.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    viewModel: TaskViewModel,
    onAddTask: () -> Unit,
    onEditTask: (Task) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Task List") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                viewModel.resetForm()
                onAddTask()
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            when (val state = uiState) {
                is TaskListUIState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                is TaskListUIState.Error -> Text(text = state.message, color = Color.Red, modifier = Modifier.align(Alignment.Center))
                is TaskListUIState.Success -> {
                    if (state.tasks.isEmpty()) {
                        Text(text = "No tasks yet.", modifier = Modifier.align(Alignment.Center))
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(state.tasks) { task ->
                                TaskItem(
                                    task = task,
                                    onClick = {
                                        viewModel.loadTaskForEditing(task)
                                        onEditTask(task)
                                    },
                                    onDelete = { viewModel.deleteTask(task) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onClick: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = task.title, style = MaterialTheme.typography.titleLarge)
                Text(text = task.category, style = MaterialTheme.typography.bodyMedium)
                if (task.isUrgent) {
                    Text(text = "URGENT", color = Color.Red, style = MaterialTheme.typography.labelSmall)
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Task")
            }
        }
    }
}
