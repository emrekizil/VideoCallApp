package com.emrekizil.videocallapp.ui.call

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.SurfaceView
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.emrekizil.videocallapp.databinding.FragmentHomeBinding
import com.emrekizil.videocallapp.ui.base.BaseFragment
import com.emrekizil.videocallapp.ui.call.adapter.CallAdapter
import com.emrekizil.videocallapp.utils.collectLifecycleFlow
import dagger.hilt.android.AndroidEntryPoint
import io.agora.rtc2.IRtcEngineEventHandler
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by viewModels()

    private val args by navArgs<HomeFragmentArgs>()

    private val adapter by lazy {
        CallAdapter()
    }

    private val mRtcEventHandler = object : IRtcEngineEventHandler() {
        override fun onUserJoined(uid: Int, elapsed: Int) {
            super.onUserJoined(uid, elapsed)
            lifecycleScope.launch {
                setupRemoteVideo(uid)
            }
        }
    }

    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.all { it.value }) {
            initializeAndJoinChannel()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestPermission()
        setupChatClient()
        setupListeners()
        joinLeave()

        binding.btnSendMessage.setOnClickListener {
            sendMessage()
        }
        binding.cancelCallButton.setOnClickListener {
            viewModel.stopChannel()
            navigate(HomeFragmentDirections.actionHomeFragmentToUserCallFragment())
        }

        binding.messageRecyclerView.adapter = adapter
        binding.usernameInCallTextView.text = args.toUser

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.messageItems.collect {
                    adapter.updateItems(it)
                }
            }
        }
    }

    private fun requestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                initializeAndJoinChannel()
            }
            else -> {
                requestCameraPermissionLauncher.launch(getRequiredPermissions())
            }
        }
    }

    private fun setupListeners() {
        viewModel.setupListeners { message, _ ->
            lifecycleScope.launch {
                view?.let {
                    viewModel.onRecipientMessage(args.toUser, message)
                }
            }
        }
    }

    private fun setupChatClient() {
        viewModel.setupChatClient()
    }

    private fun joinLeave() {
        viewModel.joinLeave(
            args.username
        )
    }
    private fun sendMessage() {
        val toSendName = args.toUser
        val content = binding.etMessageText.text.toString()
        viewModel.sendMessage(toSendName, content) {
            lifecycleScope.launch {
                viewModel.onUserMessage(args.username, message = content)
                binding.etMessageText.text?.clear()
            }
        }
    }

    private fun initializeAndJoinChannel() {
        val surfaceView = SurfaceView(activity?.baseContext)
        binding.localVideoViewContainer.addView(surfaceView)
        viewModel.initializeCall(mRtcEventHandler, surfaceView)
    }

    private fun setupRemoteVideo(uid: Int) {
        val remoteSurfaceView = SurfaceView(activity?.baseContext)
        remoteSurfaceView.setZOrderMediaOverlay(true)
        binding.remoteVideoViewContainer.addView(remoteSurfaceView)
        viewModel.setupRemoteVideo(uid, remoteSurfaceView)
    }


    private fun getRequiredPermissions(): Array<String> {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        } else {
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopChannel()
    }


}