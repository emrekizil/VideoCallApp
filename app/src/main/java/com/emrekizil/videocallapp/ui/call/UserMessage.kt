package com.emrekizil.videocallapp.ui.call

data class UserMessage(
    val username:String,
    val message:String,
    val messageType:MessageType
)
enum class MessageType {
    USER,RECIPIENT
}