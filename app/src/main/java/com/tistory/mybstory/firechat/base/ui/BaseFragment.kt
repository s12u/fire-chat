package com.tistory.mybstory.firechat.base.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment<B : ViewDataBinding> constructor(
    @LayoutRes private val layoutRes: Int
) : Fragment(layoutRes), CoroutineScope {

    private lateinit var _binding: B
    protected val binding: B get() = _binding

    override val coroutineContext: CoroutineContext
        get() = viewLifecycleOwner.lifecycleScope.coroutineContext

    abstract var backPressedCallback: (OnBackPressedCallback.()->Unit)?

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DataBindingUtil.bind(requireView())!!
        with(binding) {
            lifecycleOwner = viewLifecycleOwner
        }
        captureTouchEvents(view)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        backPressedCallback?.let {
            requireActivity().onBackPressedDispatcher.addCallback(this, true, it)
        }
    }

    private fun captureTouchEvents(view: View) {
        view.apply {
            isClickable = true
            isFocusable = true
            isFocusableInTouchMode = true
            setOnTouchListener { _, _ ->
                performClick()
                hideKeyboard()
                true
            }
        }
    }

}

fun Fragment.hideKeyboard() {
    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    requireView().requestFocus()
}

fun Fragment.showKeyboard(viewToFocus: View) = lifecycleScope.launch {
    viewToFocus.requestFocus()
    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
}
