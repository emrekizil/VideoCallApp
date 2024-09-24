package com.emrekizil.videocallapp.ui.startcall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emrekizil.videocallapp.data.repository.VideoCallRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartCallViewModel @Inject constructor(
    private val repository: VideoCallRepository
) : ViewModel() {

    fun insertCall(recipientUserId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertCall(recipientUserId)
        }
    }
}