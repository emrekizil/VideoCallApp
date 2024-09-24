package com.emrekizil.videocallapp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emrekizil.videocallapp.data.database.CallHistoryEntity
import com.emrekizil.videocallapp.data.repository.VideoCallRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserCallViewModel @Inject constructor(
    private val repository: VideoCallRepository
) : ViewModel() {
    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _callHistory = MutableStateFlow<List<CallHistoryEntity>>(emptyList())
    val callHistory = _callHistory.asStateFlow()

    init {
        getUsername()
    }

    private fun getUsername() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUsername().collectLatest { value ->
                _userName.update {
                    value
                }
            }
        }
    }

    fun getCallHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCallHistory().collectLatest { history ->
                _callHistory.update {
                    history
                }
            }
        }
    }
}