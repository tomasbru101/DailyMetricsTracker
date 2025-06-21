package com.example.dailymetricstracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MetricDao {
    @Query("SELECT * FROM metrics")
    fun getAllMetrics(): Flow<List<Metric>>

    @Insert
    suspend fun insertMetric(metric: Metric)

    @Update
    suspend fun updateMetric(metric: Metric)

    @Delete
    suspend fun deleteMetric(metric: Metric)
} 