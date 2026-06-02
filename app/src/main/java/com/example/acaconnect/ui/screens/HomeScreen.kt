package com.example.acaconnect.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.acaconnect.Batch
import com.example.acaconnect.Department
import com.example.acaconnect.R
import com.example.acaconnect.departments
import com.example.acaconnect.batches
import com.example.acaconnect.semesters
import com.example.acaconnect.sampleResources

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var selectedDept by remember { mutableStateOf<Department?>(null) }
    var selectedBatch by remember { mutableStateOf<Batch?>(null) }
    var selectedSemester by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    val uriHandler = LocalUriHandler.current

    // Determine background image based on selection
    val backgroundImage = when (selectedDept?.code) {
        "EE" -> painterResource(id = R.drawable.elec)
        // Use FOE for other departments or the main folder screen
        else -> painterResource(id = R.drawable.foe)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image with transparency for "visually calming" effect
        Image(
            painter = backgroundImage,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.15f // Low alpha for a subtle professional background effect
        )

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (selectedDept != null) {
                    IconButton(onClick = {
                        when {
                            selectedSemester != null -> selectedSemester = null
                            selectedBatch != null -> selectedBatch = null
                            else -> selectedDept = null
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
                Text(
                    text = when {
                        selectedSemester != null -> "Resources"
                        selectedBatch != null -> "Semester"
                        selectedDept != null -> "Batch"
                        else -> "ACA Folders"
                    },
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                selectedDept == null -> {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        placeholder = { Text("Search departments...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        shape = MaterialTheme.shapes.medium
                    )
                    
                    val filteredDepts = departments.filter { it.name.contains(searchQuery, ignoreCase = true) }
                    
                    // 4 Tile Grid Layout for Departments
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(filteredDepts) { dept ->
                            DepartmentTile(dept) { selectedDept = dept }
                        }
                    }
                }
                selectedBatch == null -> {
                    LazyColumn {
                        items(batches) { batch ->
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { selectedBatch = batch },
                                elevation = CardDefaults.cardElevation(2.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(text = "Batch ${batch.number}", style = MaterialTheme.typography.titleMedium)
                                    Text(text = batch.academicYear, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                                }
                            }
                        }
                    }
                }
                selectedSemester == null -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(semesters) { semester ->
                            Button(
                                onClick = { selectedSemester = semester },
                                modifier = Modifier.height(60.dp),
                                shape = MaterialTheme.shapes.medium,
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer)
                            ) {
                                Text(text = semester, textAlign = TextAlign.Center)
                            }
                        }
                    }
                }
                else -> {
                    val resources = sampleResources[selectedDept!!.code]?.get(selectedSemester!!) ?: emptyList()
                    if (resources.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No resources available yet.", style = MaterialTheme.typography.bodyMedium)
                        }
                    } else {
                        LazyColumn {
                            items(resources) { resource ->
                                ResourceItem(resource) { uriHandler.openUri(resource.url) }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DepartmentTile(dept: Department, onClick: () -> Unit) {
    val icon = when(dept.code) {
        "CE" -> Icons.Default.Computer
        "EE" -> Icons.Default.ElectricBolt
        "ME" -> Icons.Default.Settings
        "CV" -> Icons.Default.Apartment
        else -> Icons.Default.Folder
    }

    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = dept.name,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun ResourceItem(resource: com.example.acaconnect.AcademicResource, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.9f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (resource.type == "Recording") Icons.Default.PlayCircle else Icons.Default.Description,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = resource.title, style = MaterialTheme.typography.titleMedium)
                Text(text = resource.type, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
