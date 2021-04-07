package com.tistory.mybstory.firechat.ui.auth.phone.code

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.tistory.mybstory.firechat.R
import com.tistory.mybstory.firechat.base.ui.BaseFragment
import com.tistory.mybstory.firechat.base.ui.hideKeyboard
import com.tistory.mybstory.firechat.base.ui.showKeyboard
import com.tistory.mybstory.firechat.databinding.FragmentVerifyCodeBinding
import com.tistory.mybstory.firechat.util.hideProgress
import com.tistory.mybstory.firechat.util.showErrorSnackBar
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

        viewModel.resendVerificationCode(requireActivity())
        showKeyboard(etVerificationCode)
        // TODO: clear text & show dialog on expired
    }

    private fun observeUiStateChanges() {
        viewModel.phoneAuthUiStateFlow
            .onEach { handleResendCodeUiState(it) }
            .launchIn(this)

        viewModel.verifyUiStateFlow
            .onEach { handleVerifyCodeUiState(it) }
            .launchIn(this)



        viewModel.signInStateFlow
            .onEach { handleSignInState(it) }
            .launchIn(this)
    }

    private fun handleVerifyCodeUiState(state: VerifyUiState) = launch {
        Timber.e("state : $state")
        when (state) {
            is VerifyUiState.Success -> {
                hideProgress()
                binding.tilVerificationCode.error = ""
            }
            is VerifyUiState.Error -> {
                handleVerificationError(state.exception)
                hideProgress()
            }
            is VerifyUiState.Loading -> {
                showProgress()
                binding.tilVerificationCode.error = ""
            }
            else -> { }
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
                handleResentError(state.exception)
                hideProgress()
            }
            is PhoneAuthUiState.Loading -> {
                showProgress()
            }
            else -> { }
        }
    }

    private fun handleSignInState(state: SignInState) {
        when (state) {
            is SignInState.Success -> {
                hideProgress()
                if (state.isNewUser) {
                    findNavController().navigate(R.id.action_verifyCode_to_newProfileFragment)
                } else {
                    findNavController().navigate(R.id.action_verifyCode_to_mainFragment)
                }
            }
            is SignInState.Error -> {
                hideProgress()
                handleSignInError(state.exception)
            }
            is SignInState.Loading -> {
                showProgress()
            }
            else -> {}
        }
    }

    private fun handleVerificationError(e: Throwable) {
        when (e) {
            is FirebaseAuthInvalidCredentialsException -> {
                binding.tilVerificationCode.error = getString(R.string.error_invalid_verification_code)
            }
        }
    }

    private fun handleResentError(e: Throwable) {
        val errorMessage = when (e) {
            is FirebaseAuthInvalidCredentialsException -> { getString(R.string.error_invalid_number) }
            else -> { getString(R.string.error_unknown) }
        }
        showErrorSnackBar(errorMessage)
        findNavController().navigateUp()
    }

    private fun handleSignInError(e: Throwable) {
        when (e) {
            is FirebaseAuthInvalidCredentialsException -> {
                // TODO: dialog?
            }
        }
    }

    override var backPressedCallback: (OnBackPressedCallback.() -> Unit)? = null
}
