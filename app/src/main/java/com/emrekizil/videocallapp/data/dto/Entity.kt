package com.emrekizil.videocallapp.data.dto


import com.google.gson.annotations.SerializedName

data class Entity(
    @SerializedName("activated")
    val activated: Boolean?,
    @SerializedName("created")
    val created: Long?,
    @SerializedName("modified")
    val modified: Long?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("username")
    val username: String?,
    @SerializedName("uuid")
    val uuid: String?
)