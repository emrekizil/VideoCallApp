package com.emrekizil.videocallapp.ui.startcall

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.emrekizil.videocallapp.databinding.FragmentStartCallBinding
import com.emrekizil.videocallapp.ui.base.BaseFragment
import com.emrekizil.videocallapp.utils.EmptyTextRule
import com.emrekizil.videocallapp.utils.LetterTextRule
import com.emrekizil.videocallapp.utils.NumberTextRule
import com.emrekizil.videocallapp.utils.UsernameTextRule
import com.emrekizil.videocallapp.utils.validateRule
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartCallFragment :
    BaseFragment<FragmentStartCallBinding>(FragmentStartCallBinding::inflate) {

    private val args by navArgs<StartCallFragmentArgs>()

    private val viewModel by viewModels<StartCallViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.startCallButton.setOnClickListener {
            if (validator()) {
                viewModel.insertCall(binding.callUsernameEditText.text.toString())
                navigate(
                    StartCallFragmentDirections.actionStartCallFragment2ToHomeFragment2(
                        args.username,
                        binding.callUsernameEditText.text.toString()
                    )
                )
            }
        }
    }

    private fun validator(): Boolean {
        return binding.callUsernameEditText.validateRule(
            listOf(EmptyTextRule(), UsernameTextRule(), LetterTextRule(), NumberTextRule())
        )
    }
}