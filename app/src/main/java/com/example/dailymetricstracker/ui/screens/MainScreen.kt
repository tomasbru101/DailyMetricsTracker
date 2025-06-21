package com.example.dailymetricstracker.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dailymetricstracker.ui.EntryWithMetricName
import com.example.dailymetricstracker.ui.MainViewModel
import com.example.dailymetricstracker.ui.MetricWithEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(),
    onNavigateToMetricManagement: () -> Unit
) {
    val metricsWithEntries by viewModel.metricsWithEntries.collectAsState()
    val allEntries by viewModel.allEntries.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daily Metrics Tracker") },
                actions = {
                    IconButton(onClick = onNavigateToMetricManagement) {
                        Icon(Icons.Default.Menu, contentDescription = "Manage Metrics")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(metricsWithEntries) { metricWithEntry ->
                MetricItem(metricWithEntry, onValueChange = { value ->
                    viewModel.updateEntry(metricWithEntry.metric.id, value)
                })
            }
            item {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Text(
                    "All Entries",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(16.dp)
                )
                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    Text("Date", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
                    Text("Metric", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
                    Text("Value", modifier = Modifier.weight(0.5f), style = MaterialTheme.typography.titleMedium)
                }
                Divider()
            }
            items(allEntries) { entryWithMetricName ->
                AllEntriesItem(entryWithMetricName, viewModel::formatDate)
                Divider()
            }
        }
    }
}

@Composable
fun AllEntriesItem(
    entryWithMetricName: EntryWithMetricName,
    formatDate: (java.util.Date) -> String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = formatDate(entryWithMetricName.entry.date), modifier = Modifier.weight(1f))
        Text(text = entryWithMetricName.metricName, modifier = Modifier.weight(1f))
        Text(text = entryWithMetricName.entry.value.toString(), modifier = Modifier.weight(0.5f))
    }
}

@Composable
fun MetricItem(
    metricWithEntry: MetricWithEntry,
    onValueChange: (Int) -> Unit
) {
    var sliderPosition by remember { mutableStateOf((metricWithEntry.entry?.value ?: 0).toFloat()) }

    LaunchedEffect(metricWithEntry.entry?.value) {
        sliderPosition = (metricWithEntry.entry?.value ?: 0).toFloat()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = metricWithEntry.metric.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = sliderPosition.toInt().toString(),
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Slider(
            value = sliderPosition,
            onValueChange = { sliderPosition = it },
            onValueChangeFinished = {
                onValueChange(sliderPosition.toInt())
            },
            valueRange = 0f..10f,
            steps = 9
        )
    }
} 