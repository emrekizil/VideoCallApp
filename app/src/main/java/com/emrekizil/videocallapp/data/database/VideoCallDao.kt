package com.emrekizil.videocallapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoCallDao {
    @Insert
    suspend fun insertCall(callEntity: CallHistoryEntity)

    @Query("SELECT * FROM callhistoryentity ORDER BY id DESC")
    fun getCallHistory(): Flow<List<CallHistoryEntity>>
}