package com.emrekizil.videocallapp.ui

import androidx.lifecycle.ViewModel
import com.emrekizil.videocallapp.data.repository.VideoCallRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val videoCallRepository: VideoCallRepository
) : ViewModel() {

    suspend fun isUserRegistered(): Boolean {
        return videoCallRepository.getUsername().first().isNotEmpty()
    }
}

