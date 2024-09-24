package com.emrekizil.videocallapp.ui.call

import android.view.SurfaceView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emrekizil.videocallapp.data.repository.AgoraRepository
import com.emrekizil.videocallapp.data.repository.VideoCallRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.agora.rtc2.IRtcEngineEventHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val agoraRepository: AgoraRepository
) :
    ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)

    private val _messageItems = MutableStateFlow<List<UserMessage>>(emptyList())
    val messageItems = _messageItems.asStateFlow()

    fun initializeCall(mRtcEventHandler: IRtcEngineEventHandler, surfaceView: SurfaceView) {
        agoraRepository.initializeAndJoinChannel(mRtcEventHandler, surfaceView)
    }

    fun setupRemoteVideo(uid: Int, surfaceView: SurfaceView) {
        agoraRepository.setupRemoteVideo(uid, surfaceView)
    }

    fun stopChannel() {
        agoraRepository.stopChannel()
    }

    fun setupChatClient() {
        agoraRepository.setupChatClient()
    }

    fun setupListeners(onMessageReceived: (String, Boolean) -> Unit) {
        agoraRepository.setupListeners(onMessageReceived) {
            _isLoggedIn.update {
                it
            }
        }
    }

    fun onRecipientMessage(username:String,message: String) {
        viewModelScope.launch {
            _messageItems.update { oldItems ->
                val newList = oldItems.toMutableList()
                newList.add(UserMessage(username,extractTextInsideQuotes(message), MessageType.RECIPIENT))
                newList
            }
        }
    }

    fun onUserMessage(username:String,message: String) {
        viewModelScope.launch {
            _messageItems.update { oldItems ->
                val newList = oldItems.toMutableList()
                newList.add(UserMessage(username,message, MessageType.USER))
                newList
            }
        }
    }

    fun sendMessage(toSendName: String, content: String, onSendMessage: () -> Unit) {
        agoraRepository.sendMessage(toSendName, content, onSendMessage)
    }

    fun joinLeave(userId: String) {
        agoraRepository.joinLeave(_isLoggedIn.value, userId) {
            _isLoggedIn.update { it }
        }
    }

    private fun extractTextInsideQuotes(input: String): String {
        val regex = "\"(.*?)\"".toRegex()
        val matchResult = regex.find(input)
        return matchResult?.groups?.get(1)?.value ?: ""
    }
}