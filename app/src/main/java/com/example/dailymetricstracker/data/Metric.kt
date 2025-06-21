package com.example.dailymetricstracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "metrics")
data class Metric(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String
) 