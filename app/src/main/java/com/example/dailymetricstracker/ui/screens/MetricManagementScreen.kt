package com.example.dailymetricstracker.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dailymetricstracker.data.Metric
import com.example.dailymetricstracker.ui.MetricManagementViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetricManagementScreen(
    viewModel: MetricManagementViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val metrics by viewModel.metrics.collectAsState()
    var showAddMetricDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Metrics") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            IconButton(onClick = { showAddMetricDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Metric")
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(metrics) { metric ->
                MetricManagementItem(
                    metric = metric,
                    onUpdate = { viewModel.updateMetric(it) },
                    onDelete = { viewModel.deleteMetric(it) }
                )
            }
        }

        if (showAddMetricDialog) {
            AddMetricDialog(
                onAddMetric = { name ->
                    viewModel.addMetric(name)
                    showAddMetricDialog = false
                },
                onDismiss = { showAddMetricDialog = false }
            )
        }
    }
}

@Composable
fun MetricManagementItem(
    metric: Metric,
    onUpdate: (Metric) -> Unit,
    onDelete: (Metric) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf(metric.name) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isEditing) {
            TextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.weight(1f)
            )
            Button(onClick = {
                onUpdate(metric.copy(name = name))
                isEditing = false
            }) {
                Text("Save")
            }
        } else {
            Text(
                text = metric.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { isEditing = true }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = { onDelete(metric) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Composable
fun AddMetricDialog(
    onAddMetric: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Metric") },
        text = {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Metric Name") }
            )
        },
        confirmButton = {
            Button(onClick = { onAddMetric(name) }) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
} 