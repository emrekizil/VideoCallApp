package com.emrekizil.videocallapp.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.util.Predicate
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.emrekizil.videocallapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun TextInputEditText.validateRule(
    rules: List<ValidationRule>,
    callback: ((TextInputEditTextUIModel) -> Unit)? = null
): Boolean {
    val textInputLayout = this.parent.parent as TextInputLayout
    val viewEnabled = textInputLayout.isEnabled
    this.doAfterTextChanged {
        textInputLayout.error = null
        textInputLayout.isErrorEnabled = false
    }

    val validateText = this.text.toString().trim()
    for (i in rules.indices) {
        val isNotValid = rules[i].predicate.test(validateText)
        val message = rules[i].errorMessage
        if (isNotValid) {
            if (!viewEnabled) textInputLayout.isEnabled = true
            textInputLayout.error = context.resources.getString(message)
            callback?.invoke(TextInputEditTextUIModel(isEnabled = viewEnabled))
            return false
        } else {
            continue
        }
    }
    return true
}

fun <T : ViewBinding> Fragment.viewBinding(factory: (LayoutInflater) -> T): ReadOnlyProperty<Fragment, T> =
    object : ReadOnlyProperty<Fragment, T>, DefaultLifecycleObserver {

        private var binding: T? = null

        override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
            return binding ?: factory(LayoutInflater.from(requireContext())).also {
                if (viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED)) {
                    viewLifecycleOwner.lifecycle.addObserver(this)
                    binding = it
                }
            }
        }

        override fun onDestroy(owner: LifecycleOwner) {
            super.onDestroy(owner)
            binding = null
        }
    }

inline fun <T> Fragment.collectLatestLifecycleFlow(
    flow: Flow<T>,
    crossinline collect: (T) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest { value ->
                collect(value)
            }
        }
    }
}

inline fun <T> Fragment.collectLifecycleFlow(
    flow: Flow<T>,
    crossinline collect: (T) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest { value ->
                collect(value)
            }
        }
    }
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

inline fun <T : ViewBinding> ViewGroup.inflateAdapterItem(
    crossinline inflater: (LayoutInflater, ViewGroup, Boolean) -> T,
    attachToParent: Boolean = false
): T {
    return inflater.invoke(LayoutInflater.from(context), this, attachToParent)
}

data class TextInputEditTextUIModel(
    val isEnabled: Boolean? = null,
    val errorMessage: String? = null
)

open class ValidationRule(
    @StringRes open val errorMessage: Int = R.string.app_name,
    val predicate: Predicate<String>
) {
    companion object {
        const val USERNAME_LENGTH = 6
    }
}

class EmptyTextRule(
    @StringRes override val errorMessage: Int = R.string.field_cannot_be_left_blank
) : ValidationRule(predicate = { it.isNullOrEmpty() })

class UsernameTextRule(
    @StringRes override val errorMessage: Int = R.string.requires_minimum_six_character
) : ValidationRule(predicate = { it.length < USERNAME_LENGTH })

class NumberTextRule(
    @StringRes override val errorMessage: Int = R.string.requires_minimum_one_number
) : ValidationRule(predicate = { !it.any { char -> char.isDigit() } })

class LetterTextRule(
    @StringRes override val errorMessage: Int = R.string.requires_minimum_one_letter
) : ValidationRule(predicate = { !it.any { char -> char.isLetter() } })