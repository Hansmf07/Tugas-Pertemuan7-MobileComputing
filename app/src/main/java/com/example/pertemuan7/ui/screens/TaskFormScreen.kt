package com.example.pertemuan7.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.pertemuan7.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskFormScreen(viewModel: TaskViewModel, onNavigateBack: () -> Unit) {
    val formState by viewModel.formState.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = if (formState.isEditing) "Edit Task" else "Add New Task", style = MaterialTheme.typography.headlineMedium)

        // A. TextField (Judul & Deskripsi)
        OutlinedTextField(
            value = formState.title,
            onValueChange = { viewModel.updateTitle(it) },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth(),
            isError = formState.error != null
        )
        if (formState.error != null) {
            Text(text = formState.error!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        OutlinedTextField(
            value = formState.description,
            onValueChange = { viewModel.updateDescription(it) },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        // B. RadioButton (Prioritas)
        Text(text = "Priority", style = MaterialTheme.typography.titleMedium)
        val priorityOptions = listOf("Low", "Medium", "High")
        Column(Modifier.selectableGroup()) {
            priorityOptions.forEach { text ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .selectable(
                            selected = (text == formState.priority),
                            onClick = { viewModel.updatePriority(text) },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (text == formState.priority),
                        onClick = null // null recommended for accessibility with screen readers
                    )
                    Text(
                        text = text,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }

        // C. Exposed Dropdown Menu (Kategori)
        Text(text = "Category", style = MaterialTheme.typography.titleMedium)
        val categories = listOf("Work", "Personal", "Study")
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = formState.category,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select Category") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category) },
                        onClick = {
                            viewModel.updateCategory(category)
                            expanded = false
                        }
                    )
                }
            }
        }

        // D. Switch (Urgent)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Mark as Urgent", style = MaterialTheme.typography.bodyLarge)
            Switch(
                checked = formState.isUrgent,
                onCheckedChange = { viewModel.updateUrgent(it) }
            )
        }

        // E. Slider (Progress)
        Text(text = "Progress: ${(formState.progress * 100).toInt()}%", style = MaterialTheme.typography.titleMedium)
        Slider(
            value = formState.progress,
            onValueChange = { viewModel.updateProgress(it) }
        )

        // F. Checkbox (Labels)
        Text(text = "Labels", style = MaterialTheme.typography.titleMedium)
        val labelOptions = listOf("Home", "Work", "Urgent")
        labelOptions.forEach { label ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .selectable(
                        selected = formState.selectedLabels.contains(label),
                        onClick = { viewModel.toggleLabel(label) },
                        role = Role.Checkbox
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = formState.selectedLabels.contains(label),
                    onCheckedChange = null
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }

        Button(
            onClick = {
                viewModel.saveTask()
                onNavigateBack()
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
        ) {
            Text(text = if (formState.isEditing) "Update Task" else "Save Task")
        }
    }
}
