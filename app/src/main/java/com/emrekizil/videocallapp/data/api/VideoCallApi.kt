package com.emrekizil.videocallapp.data.api

import com.emrekizil.videocallapp.BuildConfig
import com.emrekizil.videocallapp.data.dto.RegisterUserRequest
import com.emrekizil.videocallapp.data.dto.RegisterUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path


interface VideoCallApi {
    @POST("{orgName}/{appName}/users")
    suspend fun registerUser(
        @Body request: RegisterUserRequest,
        @Path("orgName") orgName: String = BuildConfig.orgName,
        @Path("appName") appName: String = BuildConfig.appName
    ): Response<RegisterUserResponse>
}