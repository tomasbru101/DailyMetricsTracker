package com.example.dailymetricstracker.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymetricstracker.data.AppDatabase
import com.example.dailymetricstracker.data.Entry
import com.example.dailymetricstracker.data.Metric
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class MetricWithEntry(
    val metric: Metric,
    val entry: Entry?
)

data class EntryWithMetricName(
    val entry: Entry,
    val metricName: String
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val metricDao = AppDatabase.getDatabase(application).metricDao()
    private val entryDao = AppDatabase.getDatabase(application).entryDao()

    val metricsWithEntries: StateFlow<List<MetricWithEntry>> =
        metricDao.getAllMetrics()
            .combine(entryDao.getEntriesForDate(getToday())) { metrics, entries ->
                metrics.map { metric ->
                    MetricWithEntry(
                        metric = metric,
                        entry = entries.find { it.metricId == metric.id }
                    )
                }
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allEntries: StateFlow<List<EntryWithMetricName>> =
        entryDao.getAllEntries()
            .combine(metricDao.getAllMetrics()) { entries, metrics ->
                entries.map { entry ->
                    EntryWithMetricName(
                        entry = entry,
                        metricName = metrics.find { it.id == entry.metricId }?.name ?: "Unknown"
                    )
                }
            }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateEntry(metricId: Long, value: Int) {
        viewModelScope.launch {
            entryDao.insertOrUpdateEntry(
                Entry(
                    date = getToday(),
                    metricId = metricId,
                    value = value
                )
            )
        }
    }

    private fun getToday(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    fun formatDate(date: Date): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return formatter.format(date)
    }
} 