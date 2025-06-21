package com.example.dailymetricstracker.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "entries",
    foreignKeys = [
        ForeignKey(
            entity = Metric::class,
            parentColumns = ["id"],
            childColumns = ["metricId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["date", "metricId"], unique = true)]
)
data class Entry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: Date,
    val metricId: Long,
    val value: Int
) 