package com.emrekizil.videocallapp.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.emrekizil.videocallapp.utils.viewBinding

abstract class BaseFragment<T : ViewBinding>(factory: (LayoutInflater) -> T) : Fragment() {
    val binding: T by viewBinding(factory)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    open fun navigate(action: NavDirections) {
        findNavController().navigate(action)
    }
}