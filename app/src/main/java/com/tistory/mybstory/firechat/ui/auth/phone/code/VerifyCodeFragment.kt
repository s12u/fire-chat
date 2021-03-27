package com.tistory.mybstory.firechat.ui.auth.phone.code

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.tistory.mybstory.firechat.R
import com.tistory.mybstory.firechat.base.ui.BaseFragment
import com.tistory.mybstory.firechat.base.ui.hideKeyboard
import com.tistory.mybstory.firechat.databinding.FragmentVerifyCodeBinding
import com.tistory.mybstory.firechat.ui.auth.phone.PhoneAuthUiState
import com.tistory.mybstory.firechat.util.hideProgress
import com.tistory.mybstory.firechat.util.showProgress
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class VerifyCodeFragment : BaseFragment<FragmentVerifyCodeBinding>(R.layout.fragment_verify_code) {

    private val viewModel: VerifyCodeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
        observeUiStateChanges()
    }

    private fun initUI() = with(binding) {
        vm = viewModel
        btnVerify.setOnClickListener {
            viewModel.verifyPhoneNumberWithCode()
            hideKeyboard()
        }

        btnResend.setOnClickListener {
            viewModel.resendVerificationCode(requireActivity())
        }

        // TODO: show counter text
        // TODO: clear text & show dialog on expired
    }

    private fun observeUiStateChanges() {
        viewModel.phoneAuthUiStateFlow
            .onEach { handleResendCodeUiState(it) }
            .launchIn(this)

        viewModel.verifyUiStateFlow
            .onEach { handleVerifyCodeUiState(it) }
            .launchIn(this)

        viewModel.isNewUserLiveData
            .observe(viewLifecycleOwner) { handleNewUserState(it) }
    }

    private fun handleVerifyCodeUiState(state: VerifyUiState) = launch {
        Timber.e("state : $state")
        when (state) {
            is VerifyUiState.Success -> {
                // TODO: go to next page
                hideProgress()
            }
            is VerifyUiState.Error -> {
                // TODO: show error message
                hideProgress()
            }
            is VerifyUiState.Loading -> {
                showProgress()
            }
            else -> {

            }
        }
    }

    private fun handleResendCodeUiState(state: PhoneAuthUiState) = launch {
        Timber.e("state : $state")
        when (state) {
            is PhoneAuthUiState.Success -> {
                hideProgress()
            }
            is PhoneAuthUiState.Error -> {
                // TODO: show error message
                hideProgress()
            }
            is PhoneAuthUiState.Loading -> {
                showProgress()
            }
            else -> {
            }
        }
    }

    private fun handleNewUserState(isNewUser: Boolean) {
        if (isNewUser) {
            findNavController().navigate(R.id.action_verifyCode_to_newProfileFragment)
        } else {
            findNavController().navigate(R.id.action_verifyCode_to_mainFragment)
        }
    }
}
