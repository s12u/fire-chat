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
            viewModel.sendVerificationCode(requireActivity())
        }

        viewModel.sendVerificationCode(requireActivity())
        // TODO: clear text & show dialog on expired
    }

    private fun observeUiStateChanges() {
        viewModel.phoneAuthUiStateFlow
            .onEach { handleCodeSentUiState(it) }
            .launchIn(this)

        viewModel.verifyUiStateFlow
            .onEach { handleVerifyCodeUiState(it) }
            .launchIn(this)

        viewModel.signInStateFlow
            .onEach { handleSignInState(it) }
            .launchIn(this)
    }

    // handle sms otp verification result
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

    // handle sms code sent result
    private fun handleCodeSentUiState(state: PhoneAuthUiState) = launch {
        Timber.e("state : $state")
        when (state) {
            is PhoneAuthUiState.Success -> {
                hideProgress()
                showKeyboard(binding.etVerificationCode)
            }
            is PhoneAuthUiState.Error -> {
                // TODO: show error message
                handleCodeSentError(state.exception)
                hideProgress()
            }
            is PhoneAuthUiState.Loading -> {
                showProgress()
            }
            else -> { }
        }
    }

    // handle sign-in result
    private fun handleSignInState(state: SignInState) {
        when (state) {
            is SignInState.Success -> {
                hideProgress()
                Timber.e("new user : ${state.isNewUser}")
                if (state.isNewUser) {
                    findNavController().navigate(R.id.action_verifyCode_to_newProfileFragment)
                } else {
                    findNavController().navigate(R.id.action_verifyCode_to_homeFragment)
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

    // handle verification error
    private fun handleVerificationError(e: Throwable) {
        when (e) {
            is FirebaseAuthInvalidCredentialsException -> {
                binding.tilVerificationCode.error = getString(R.string.error_invalid_verification_code)
            }
        }
    }

    // handle sms code sent error
    private fun handleCodeSentError(e: Throwable) {
        val errorMessage = when (e) {
            is FirebaseAuthInvalidCredentialsException -> { getString(R.string.error_invalid_number) }
            else -> { getString(R.string.error_unknown) }
        }
        showErrorSnackBar(errorMessage)
        findNavController().navigateUp()
    }

    // handle sign-in error
    private fun handleSignInError(e: Throwable) {
        when (e) {
            is FirebaseAuthInvalidCredentialsException -> {
                // TODO: dialog?
            }
        }
    }

    override var backPressedCallback: (OnBackPressedCallback.() -> Unit)? = null
}
