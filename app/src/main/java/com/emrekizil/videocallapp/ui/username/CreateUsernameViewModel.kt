package com.emrekizil.videocallapp.ui.username

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emrekizil.videocallapp.data.ResponseState
import com.emrekizil.videocallapp.data.repository.VideoCallRepository
import com.emrekizil.videocallapp.data.dto.RegisterUserRequest
import com.emrekizil.videocallapp.data.dto.RegisterUserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateUsernameViewModel @Inject constructor(
    private val videoCallRepository: VideoCallRepository
) : ViewModel() {
    private val _registerUserState = MutableStateFlow<ResponseState<RegisterUserResponse>>(ResponseState.Loading)
    val registerUserState = _registerUserState.asStateFlow()


    fun saveUsername(username:String) {
        viewModelScope.launch (Dispatchers.IO) {
            videoCallRepository.saveUsername(username)
        }

        viewModelScope.launch {
            videoCallRepository.registerUser(RegisterUserRequest(
                username
            )).collectLatest { response ->
                when(response){
                    is ResponseState.Error -> {}
                    ResponseState.Loading -> {}
                    is ResponseState.Success -> {
                        _registerUserState.update {
                            ResponseState.Success(response.data)
                        }
                    }
                }
            }
        }
    }
}