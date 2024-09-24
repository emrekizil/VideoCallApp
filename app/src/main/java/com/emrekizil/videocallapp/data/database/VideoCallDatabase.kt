package com.emrekizil.videocallapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CallHistoryEntity::class], version = 1, exportSchema = false)
abstract class VideoCallDatabase : RoomDatabase() {
    abstract fun videoCallDao(): VideoCallDao
}