package com.emrekizil.videocallapp.ui.username

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.emrekizil.videocallapp.data.ResponseState
import com.emrekizil.videocallapp.databinding.FragmentCreateUsernameBinding
import com.emrekizil.videocallapp.ui.base.BaseFragment
import com.emrekizil.videocallapp.utils.EmptyTextRule
import com.emrekizil.videocallapp.utils.LetterTextRule
import com.emrekizil.videocallapp.utils.NumberTextRule
import com.emrekizil.videocallapp.utils.UsernameTextRule
import com.emrekizil.videocallapp.utils.collectLifecycleFlow
import com.emrekizil.videocallapp.utils.validateRule

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateUsernameFragment :
    BaseFragment<FragmentCreateUsernameBinding>(FragmentCreateUsernameBinding::inflate) {

    private val viewModel: CreateUsernameViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addUsernameButton.setOnClickListener {
            if (validator()) {
                viewModel.saveUsername(binding.usernameEditText.text.toString())
            }
        }

        collectLifecycleFlow(viewModel.registerUserState){ state->
            when(state){
                is ResponseState.Error -> {}
                ResponseState.Loading -> {}
                is ResponseState.Success -> {
                    navigate(CreateUsernameFragmentDirections.actionCreateUsernameFragmentToHomeNavigation())
                }
            }
        }
    }

    private fun validator(): Boolean {
        return binding.usernameEditText.validateRule(
            listOf(EmptyTextRule(), UsernameTextRule(), LetterTextRule(), NumberTextRule())
        )
    }
}