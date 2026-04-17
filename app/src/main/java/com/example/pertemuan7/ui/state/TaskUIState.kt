package com.example.pertemuan7.ui.state

import com.example.pertemuan7.model.Task

sealed class TaskListUIState {
    object Loading : TaskListUIState()
    data class Success(val tasks: List<Task>) : TaskListUIState()
    data class Error(val message: String) : TaskListUIState()
}

data class TaskFormState(
    val title: String = "",
    val description: String = "",
    val priority: String = "Low",
    val category: String = "Work",
    val isUrgent: Boolean = false,
    val progress: Float = 0f,
    val selectedLabels: List<String> = emptyList(),
    val isEditing: Boolean = false,
    val error: String? = null
)
