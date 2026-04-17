package com.example.pertemuan7.model

data class Task(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val priority: String = "Low", // "Low", "Medium", "High"
    val category: String = "Work", // "Work", "Personal", "Study"
    val isUrgent: Boolean = false,
    val progress: Float = 0f, // Nilai 0.0f sampai 1.0f
    val selectedLabels: List<String> = emptyList() // "Home", "Urgent", "Work"
)
