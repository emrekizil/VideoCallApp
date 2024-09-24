package com.emrekizil.videocallapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CallHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val timestamp: Long
)
