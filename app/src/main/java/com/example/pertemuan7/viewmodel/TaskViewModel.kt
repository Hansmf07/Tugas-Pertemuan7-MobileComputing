package com.example.pertemuan7.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pertemuan7.model.Task
import com.example.pertemuan7.ui.state.TaskFormState
import com.example.pertemuan7.ui.state.TaskListUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TaskViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<TaskListUIState>(TaskListUIState.Success(emptyList()))
    val uiState: StateFlow<TaskListUIState> = _uiState.asStateFlow()

    private val _formState = MutableStateFlow(TaskFormState())
    val formState: StateFlow<TaskFormState> = _formState.asStateFlow()

    private val tasks = mutableListOf<Task>()
    private var currentId = 1

    fun updateTitle(title: String) {
        _formState.update { it.copy(title = title) }
    }

    fun updateDescription(description: String) {
        _formState.update { it.copy(description = description) }
    }

    fun updatePriority(priority: String) {
        _formState.update { it.copy(priority = priority) }
    }

    fun updateCategory(category: String) {
        _formState.update { it.copy(category = category) }
    }

    fun updateUrgent(isUrgent: Boolean) {
        _formState.update { it.copy(isUrgent = isUrgent) }
    }

    fun updateProgress(progress: Float) {
        _formState.update { it.copy(progress = progress) }
    }

    fun toggleLabel(label: String) {
        _formState.update { state ->
            val newLabels = if (state.selectedLabels.contains(label)) {
                state.selectedLabels - label
            } else {
                state.selectedLabels + label
            }
            state.copy(selectedLabels = newLabels)
        }
    }

    fun saveTask() {
        val currentState = _formState.value
        if (currentState.title.isBlank()) {
            _formState.update { it.copy(error = "Title cannot be empty") }
            return
        }

        if (currentState.isEditing) {
            // Logic for updating existing task would go here if we had an ID in formState
            // For simplicity in this tutorial, let's assume we update by title or just add
        } else {
            val newTask = Task(
                id = currentId++,
                title = currentState.title,
                description = currentState.description,
                priority = currentState.priority,
                category = currentState.category,
                isUrgent = currentState.isUrgent,
                progress = currentState.progress,
                selectedLabels = currentState.selectedLabels
            )
            tasks.add(newTask)
        }
        
        _uiState.value = TaskListUIState.Success(tasks.toList())
        resetForm()
    }

    fun deleteTask(task: Task) {
        tasks.remove(task)
        _uiState.value = TaskListUIState.Success(tasks.toList())
    }

    fun loadTaskForEditing(task: Task) {
        _formState.update {
            it.copy(
                title = task.title,
                description = task.description,
                priority = task.priority,
                category = task.category,
                isUrgent = task.isUrgent,
                progress = task.progress,
                selectedLabels = task.selectedLabels,
                isEditing = true
            )
        }
    }

    fun resetForm() {
        _formState.value = TaskFormState()
    }
}
