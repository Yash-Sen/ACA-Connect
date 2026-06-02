package com.example.acaconnect.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.acaconnect.SubTask
import com.example.acaconnect.Task
import com.example.acaconnect.sampleTasks
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen() {
    var tasks by remember { mutableStateOf(sampleTasks) }
    var showDialog by remember { mutableStateOf(false) }
    var newTaskTitle by remember { mutableStateOf("") }
    
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )
    var showDatePicker by remember { mutableStateOf(false) }
    
    // Calendar state
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text(
                text = "Academic Planner",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Enhanced Calendar View
            CalendarView(
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it },
                tasks = tasks
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (tasks.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text("No tasks added yet", color = MaterialTheme.colorScheme.outline)
                }
            } else {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    item {
                        Text(
                            text = "All Tasks",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    items(tasks) { task ->
                        val taskDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(task.dueDate))
                        TaskCard(
                            task = task,
                            onCheckedChange = { isChecked ->
                                tasks = tasks.map { if (it.id == task.id) it.copy(isCompleted = isChecked) else it }
                            },
                            onDelete = {
                                tasks = tasks.filter { it.id != task.id }
                            },
                            onSubTaskToggle = { subIndex, isChecked ->
                                tasks = tasks.map { t ->
                                    if (t.id == task.id) {
                                        val newSubTasks = t.subTasks.toMutableList()
                                        newSubTasks[subIndex] = newSubTasks[subIndex].copy(isCompleted = isChecked)
                                        t.copy(subTasks = newSubTasks)
                                    } else t
                                }
                            },
                            subtitle = "Due: $taskDate"
                        )
                    }
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Add New Task") },
                text = {
                    Column {
                        TextField(
                            value = newTaskTitle,
                            onValueChange = { newTaskTitle = it },
                            placeholder = { Text("Task Title (e.g. Lab Report)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        OutlinedCard(
                            onClick = { showDatePicker = true },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CalendarMonth, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = datePickerState.selectedDateMillis?.let {
                                        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(it))
                                    } ?: "Select Due Date"
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        if (newTaskTitle.isNotBlank()) {
                            val dueDate = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                            tasks = tasks + Task(
                                id = UUID.randomUUID().toString(),
                                title = newTaskTitle,
                                isCompleted = false,
                                dueDate = dueDate,
                                subTasks = listOf(SubTask("Drafting"), SubTask("Final Review"))
                            )
                            selectedDate = Calendar.getInstance().apply { timeInMillis = dueDate }
                            newTaskTitle = ""
                            showDialog = false
                        }
                    }) {
                        Text("Add Task")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

@Composable
fun CalendarView(
    selectedDate: Calendar,
    onDateSelected: (Calendar) -> Unit,
    tasks: List<Task>
) {
    var baseDate by remember { mutableStateOf(Calendar.getInstance()) }
    val monthName = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(baseDate.time)
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    val newBase = baseDate.clone() as Calendar
                    newBase.add(Calendar.WEEK_OF_YEAR, -1)
                    baseDate = newBase
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBackIos, contentDescription = "Previous Week", modifier = Modifier.size(16.dp))
                }
                
                Text(
                    text = monthName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = {
                    val newBase = baseDate.clone() as Calendar
                    newBase.add(Calendar.WEEK_OF_YEAR, 1)
                    baseDate = newBase
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = "Next Week", modifier = Modifier.size(16.dp))
                }
            }
            
            // Scrollable Row of Days
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                state = rememberLazyListState()
            ) {
                val weekStart = (baseDate.clone() as Calendar).apply {
                    set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)
                }
                
                items(7) { i ->
                    val dayDate = (weekStart.clone() as Calendar).apply {
                        add(Calendar.DAY_OF_MONTH, i)
                    }
                    val isSelected = dayDate.get(Calendar.DAY_OF_YEAR) == selectedDate.get(Calendar.DAY_OF_YEAR) &&
                                     dayDate.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR)
                    val hasTask = tasks.any {
                        val taskCal = Calendar.getInstance().apply { timeInMillis = it.dueDate }
                        taskCal.get(Calendar.YEAR) == dayDate.get(Calendar.YEAR) &&
                        taskCal.get(Calendar.DAY_OF_YEAR) == dayDate.get(Calendar.DAY_OF_YEAR)
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .width(48.dp)
                            .padding(horizontal = 2.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
                            .clickable { onDateSelected(dayDate) }
                            .padding(vertical = 8.dp)
                    ) {
                        Text(
                            text = SimpleDateFormat("E", Locale.getDefault()).format(dayDate.time).first().toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.outline
                        )
                        Text(
                            text = dayDate.get(Calendar.DAY_OF_MONTH).toString(),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        )
                        if (hasTask) {
                            Box(
                                modifier = Modifier
                                    .size(4.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary)
                            )
                        } else {
                            Spacer(modifier = Modifier.size(4.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TaskCard(
    task: Task,
    onCheckedChange: (Boolean) -> Unit,
    onDelete: () -> Unit,
    onSubTaskToggle: (Int, Boolean) -> Unit,
    subtitle: String? = null
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Checkbox(checked = task.isCompleted, onCheckedChange = onCheckedChange)
                    Column {
                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.bodyLarge,
                            textDecoration = if (task.isCompleted) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                        )
                        if (subtitle != null) {
                            Text(
                                text = subtitle,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }
                Row {
                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = "Expand"
                        )
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }

            AnimatedVisibility(visible = expanded) {
                Column(modifier = Modifier.padding(start = 32.dp, top = 8.dp)) {
                    Text("Sub-tasks:", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
                    task.subTasks.forEachIndexed { index, subTask ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = subTask.isCompleted,
                                onCheckedChange = { onSubTaskToggle(index, it) },
                                modifier = Modifier.scale(0.8f)
                            )
                            Text(
                                text = subTask.title,
                                style = MaterialTheme.typography.bodyMedium,
                                textDecoration = if (subTask.isCompleted) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                            )
                        }
                    }
                }
            }
        }
    }
}
