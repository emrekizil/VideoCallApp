package com.emrekizil.videocallapp.data.repository

import com.emrekizil.videocallapp.data.ResponseState
import com.emrekizil.videocallapp.data.database.CallHistoryEntity
import com.emrekizil.videocallapp.data.database.VideoCallDao
import com.emrekizil.videocallapp.data.dto.RegisterUserRequest
import com.emrekizil.videocallapp.data.dto.RegisterUserResponse
import com.emrekizil.videocallapp.data.source.local.UserPreferenceSource
import com.emrekizil.videocallapp.data.source.remote.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class VideoCallRepositoryImpl @Inject constructor(
    private val userPreferenceSource: UserPreferenceSource,
    private val remoteDataSource: RemoteDataSource,
    private val videoCallDao: VideoCallDao
) : VideoCallRepository {

    override suspend fun saveUsername(username: String) {
        userPreferenceSource.saveUsername(username)
    }

    override fun getUsername(): Flow<String> =
        userPreferenceSource.getUsername().flowOn(Dispatchers.IO)

    override fun registerUser(
        userRequest: RegisterUserRequest
    ): Flow<ResponseState<RegisterUserResponse>> {
        return flow {
            emit(ResponseState.Loading)
            val response = remoteDataSource.registerUser(userRequest).body()!!
            emit(ResponseState.Success(response))
        }.catch {
            emit(ResponseState.Error(it.message.orEmpty()))
        }
    }

    override suspend fun insertCall(recipientUserId: String) {
        val currentTime = System.currentTimeMillis()
        val entity = CallHistoryEntity(username = recipientUserId, timestamp = currentTime)
        videoCallDao.insertCall(entity)
    }

    override fun getCallHistory(): Flow<List<CallHistoryEntity>> = videoCallDao.getCallHistory()
}