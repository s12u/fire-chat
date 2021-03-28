package com.tistory.mybstory.firechat.ui.auth

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tistory.mybstory.firechat.R
import com.tistory.mybstory.firechat.base.ui.BaseFragment
import com.tistory.mybstory.firechat.databinding.FragmentFirstRunBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FirstRunFragment : BaseFragment<FragmentFirstRunBinding> (R.layout.fragment_first_run) {

    private val firstRunViewModel: FirstRunViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            viewModel = firstRunViewModel
        }
        initUI()
    }

    private fun initUI() = with(binding) {
        btnSignIn.setOnClickListener {  }
        btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_firstLogin_to_phoneAuthFragment)
        }
    }

    override var backPressedCallback: (OnBackPressedCallback.() -> Unit)? = null
}
