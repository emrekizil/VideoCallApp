package com.emrekizil.videocallapp.data.dto


import com.google.gson.annotations.SerializedName

data class RegisterUserResponse(
    @SerializedName("action")
    val action: String?,
    @SerializedName("application")
    val application: String?,
    @SerializedName("applicationName")
    val applicationName: String?,
    @SerializedName("data")
    val `data`: List<Any?>?,
    @SerializedName("duration")
    val duration: Int?,
    @SerializedName("entities")
    val entities: List<Entity?>?,
    @SerializedName("organization")
    val organization: String?,
    @SerializedName("path")
    val path: String?,
    @SerializedName("timestamp")
    val timestamp: Long?,
    @SerializedName("uri")
    val uri: String?
)