package com.emrekizil.videocallapp.data.repository

import com.emrekizil.videocallapp.data.ResponseState
import com.emrekizil.videocallapp.data.database.CallHistoryEntity
import com.emrekizil.videocallapp.data.dto.RegisterUserRequest
import com.emrekizil.videocallapp.data.dto.RegisterUserResponse
import kotlinx.coroutines.flow.Flow


interface VideoCallRepository {
    suspend fun saveUsername(username:String)
    fun getUsername():Flow<String>
    fun registerUser(
        userRequest: RegisterUserRequest
    ): Flow<ResponseState<RegisterUserResponse>>

    suspend fun insertCall(recipientUserId:String)
    fun getCallHistory():Flow<List<CallHistoryEntity>>
}