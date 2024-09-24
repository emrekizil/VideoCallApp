package com.emrekizil.videocallapp.data.source.remote

import com.emrekizil.videocallapp.data.api.VideoCallApi
import com.emrekizil.videocallapp.data.dto.RegisterUserRequest
import com.emrekizil.videocallapp.data.dto.RegisterUserResponse
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val api: VideoCallApi
) : RemoteDataSource {
    override suspend fun registerUser(
        userRequest: RegisterUserRequest
    ): Response<RegisterUserResponse> = api.registerUser(userRequest)
}