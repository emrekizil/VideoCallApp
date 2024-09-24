package com.emrekizil.videocallapp.ui.profile

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.emrekizil.videocallapp.data.database.CallHistoryEntity
import com.emrekizil.videocallapp.databinding.FragmentUserCallBinding
import com.emrekizil.videocallapp.ui.base.BaseFragment
import com.emrekizil.videocallapp.ui.profile.adapter.ProfileAdapter
import com.emrekizil.videocallapp.utils.collectLatestLifecycleFlow
import com.emrekizil.videocallapp.utils.hide
import com.emrekizil.videocallapp.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserCallFragment : BaseFragment<FragmentUserCallBinding>(FragmentUserCallBinding::inflate) {

    private val viewModel: UserCallViewModel by viewModels()

    private val adapter by lazy {
        ProfileAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCallHistory()
        adapter.setOnCallItemClickListener {
            onCallItem(it)
        }
        binding.callHistoryAdapter.adapter = adapter
        binding.startCallButton.setOnClickListener {
            navigate(UserCallFragmentDirections.actionUserCallFragmentToStartCallFragment2(viewModel.userName.value))
        }

        collectLatestLifecycleFlow(viewModel.userName) { username ->
            binding.usernameTextView.text = username
        }
        collectLatestLifecycleFlow(viewModel.callHistory) { callHistory ->
            adapter.updateItems(callHistory)
            updateViewState(callHistory)
        }

    }

    private fun onCallItem(username: String) {
        navigate(
            UserCallFragmentDirections.actionUserCallFragmentToHomeFragment(
                viewModel.userName.value, username
            )
        )
    }

    private fun updateViewState(callHistory: List<CallHistoryEntity>) {
        if (callHistory.isEmpty()) {
            binding.noCallsYetTextView.show()
            binding.yourCallHistoryTextView.show()
        } else {
            binding.noCallsYetTextView.hide()
            binding.yourCallHistoryTextView.hide()
        }
    }
}