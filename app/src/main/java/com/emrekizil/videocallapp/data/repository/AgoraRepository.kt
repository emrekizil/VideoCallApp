package com.emrekizil.videocallapp.data.repository

import android.content.Context
import android.view.SurfaceView
import com.emrekizil.videocallapp.BuildConfig
import com.emrekizil.videocallapp.media.ChatTokenBuilder2
import com.emrekizil.videocallapp.media.RtcTokenBuilder2
import io.agora.CallBack
import io.agora.ConnectionListener
import io.agora.chat.ChatClient
import io.agora.chat.ChatMessage
import io.agora.chat.ChatOptions
import io.agora.rtc2.ChannelMediaOptions
import io.agora.rtc2.Constants
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import io.agora.rtc2.video.VideoCanvas


class AgoraRepository(private val context: Context) {

    private lateinit var mRtcEngine: RtcEngine
    private lateinit var agoraChatClient: ChatClient
    private val appId = BuildConfig.appId
    private val certificate = BuildConfig.certificate
    private val channelName = BuildConfig.channelName
    private val appKey = BuildConfig.appKey

    fun initializeAndJoinChannel(
        mRtcEventHandler: IRtcEngineEventHandler,
        surfaceView: SurfaceView
    ) {
        try {
            val config = RtcEngineConfig()
            config.mContext = context
            config.mAppId = appId
            config.mEventHandler = mRtcEventHandler
            mRtcEngine = RtcEngine.create(config)
        } catch (_: Exception) {

        }
        val tokenBuilder = RtcTokenBuilder2()
        val timestamp = (System.currentTimeMillis() / 1000 + 60).toInt()
        val token = tokenBuilder.buildTokenWithUid(
            appId,
            certificate,
            channelName,
            0,
            RtcTokenBuilder2.Role.ROLE_PUBLISHER,
            timestamp,
            timestamp
        )
        mRtcEngine.enableVideo()
        mRtcEngine.startPreview()

        mRtcEngine.setupLocalVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, 0))
        val options = ChannelMediaOptions()

        options.clientRoleType = Constants.CLIENT_ROLE_BROADCASTER
        options.channelProfile = Constants.CHANNEL_PROFILE_COMMUNICATION_1v1

        mRtcEngine.joinChannel(token, channelName, 0, options)
    }

    fun setupRemoteVideo(uid: Int, surfaceView: SurfaceView) {
        mRtcEngine.setupRemoteVideo(
            VideoCanvas(
                surfaceView, VideoCanvas.RENDER_MODE_FIT, uid
            )
        )
    }

    fun stopChannel() {
        mRtcEngine.stopPreview()
        mRtcEngine.leaveChannel()
        agoraChatClient.logout(true)
    }

    fun setupChatClient() {
        val chatOptions = ChatOptions()
        if (appKey.isEmpty()) {
            return
        }
        chatOptions.appKey = appKey
        agoraChatClient = ChatClient.getInstance()
        agoraChatClient.init(context, chatOptions)
        agoraChatClient.setDebugMode(true)
    }

    fun setupListeners(onMessageReceived: (String, Boolean) -> Unit, onJoined: (Boolean) -> Unit) {
        agoraChatClient.chatManager().addMessageListener { messages ->
            if (messages != null) {
                for (message in messages) {
                    onMessageReceived(message.body.toString(), false)
                }
            }
        }

        agoraChatClient.addConnectionListener(object : ConnectionListener {
            override fun onConnected() {}
            override fun onDisconnected(errorCode: Int) {
                onJoined(false)
            }

            override fun onLogout(errorCode: Int) {}
        })
    }

    fun sendMessage(toSendName: String, content: String, onSendMessage: () -> Unit) {
        val message = ChatMessage.createTextSendMessage(content, toSendName)

        message.setMessageStatusCallback(object : CallBack {
            override fun onSuccess() {
                onSendMessage()
            }
            override fun onError(code: Int, error: String?) {}
        })

        agoraChatClient.chatManager().sendMessage(message)
    }

    fun joinLeave(isLoggedIn:Boolean,userId:String, onJoined: (Boolean) -> Unit,) {
        if (isLoggedIn) {
            agoraChatClient.logout(true, object : CallBack {
                override fun onSuccess() {
                    onJoined(false)
                }
                override fun onError(code: Int, error: String?) {}
            })
        } else {
            val token = ChatTokenBuilder2().buildUserToken(appId,certificate,userId, EXPIRE_DURATION)
            agoraChatClient.loginWithAgoraToken(userId, token, object : CallBack {
                override fun onSuccess() {
                    onJoined(true)
                }
                override fun onError(code: Int, error: String?) {}
            })
        }
    }

    companion object {
        private const val EXPIRE_DURATION = 600
    }
}