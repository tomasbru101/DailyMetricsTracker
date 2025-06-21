package com.example.dailymetricstracker.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailymetricstracker.data.AppDatabase
import com.example.dailymetricstracker.data.Metric
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MetricManagementViewModel(application: Application) : AndroidViewModel(application) {
    private val metricDao = AppDatabase.getDatabase(application).metricDao()

    val metrics: StateFlow<List<Metric>> = metricDao.getAllMetrics()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addMetric(name: String) {
        viewModelScope.launch {
            metricDao.insertMetric(Metric(name = name))
        }
    }

    fun updateMetric(metric: Metric) {
        viewModelScope.launch {
            metricDao.updateMetric(metric)
        }
    }

    fun deleteMetric(metric: Metric) {
        viewModelScope.launch {
            metricDao.deleteMetric(metric)
        }
    }
} 