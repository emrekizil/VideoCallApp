package com.emrekizil.videocallapp.data.source.remote

import com.emrekizil.videocallapp.data.dto.RegisterUserRequest
import com.emrekizil.videocallapp.data.dto.RegisterUserResponse
import retrofit2.Response

interface RemoteDataSource {
    suspend fun registerUser(
        userRequest: RegisterUserRequest
    ): Response<RegisterUserResponse>
}