package com.tistory.mybstory.firechat.ui.auth.phone.code

import android.app.Activity
import androidx.lifecycle.*
import com.google.firebase.auth.PhoneAuthCredential
import com.tistory.mybstory.firechat.domain.Result
import com.tistory.mybstory.firechat.domain.usecase.auth.*
import com.tistory.mybstory.firechat.ui.auth.phone.PhoneAuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class VerifyCodeViewModel @Inject constructor(
    private val verifyWithCodeUseCase: VerifyWithCodeUseCase,
    private val sendVerificationCodeUseCase: SendVerificationCodeUseCase,
    private val signInWithCredentialUseCase: SignInWithCredentialUseCase,
    val handle: SavedStateHandle
) : ViewModel() {

    val phoneNumberLiveData: LiveData<String> = handle.getLiveData("phone_number")
    private var verificationData: VerificationCodeSentResult? = handle.get("verification_data")

    private var _verificationCodeStateFlow = MutableStateFlow("")

    private val _phoneAuthUiStateFlow: MutableStateFlow<PhoneAuthUiState> = MutableStateFlow(PhoneAuthUiState.None)
    val phoneAuthUiStateFlow: StateFlow<PhoneAuthUiState> get() = _phoneAuthUiStateFlow

    private val _verifyUiStateFlow: MutableStateFlow<VerifyUiState> = MutableStateFlow(VerifyUiState.None)
    val verifyUiStateFlow: StateFlow<VerifyUiState> get() = _verifyUiStateFlow

    private val _isNewUserLiveData = MutableLiveData<Boolean>()
    val isNewUserLiveData get(): LiveData<Boolean> = _isNewUserLiveData

    @ExperimentalCoroutinesApi
    fun verifyPhoneNumberWithCode() {
        verificationData?.let { data ->
            verifyWithCodeUseCase(
                data.verificationId
                        to _verificationCodeStateFlow.value
            ).onEach { result ->
                handleVerificationResult(result)
            }.launchIn(viewModelScope)
        }
    }

    @ExperimentalCoroutinesApi
    fun resendVerificationCode(activity: Activity) = viewModelScope.launch {
        val phoneNumber = phoneNumberLiveData.value
        Timber.e("phone number: $phoneNumber")
        phoneNumber?.let {
            sendVerificationCodeUseCase(activity to phoneNumber)
                .onEach { result ->
                    handleCodeResentResult(result)
                }.launchIn(viewModelScope)
        }
    }

    @ExperimentalCoroutinesApi
    private fun signInWithAuthCredential(credential: PhoneAuthCredential?) {
        credential?.let { phoneAuthCredential->
            signInWithCredentialUseCase(phoneAuthCredential)
                .onEach { result->
                    handleSignInWithAuthResult(result)
                }
                .launchIn(viewModelScope)
        }
    }

    private fun handleVerificationResult(result: Result<PhoneNumberVerifyResult>) {
        when (result) {
            is Result.Success -> {
                Timber.e("code match!")
                _verifyUiStateFlow.value = VerifyUiState.Success
                signInWithAuthCredential(result.data.credential)
            }
            is Result.Error -> {
                handleVerificationError(result.exception)
                _verifyUiStateFlow.value = VerifyUiState.Error(result.exception)
            }
            is Result.Loading -> {
                _verifyUiStateFlow.value = VerifyUiState.Loading
            }
        }
    }

    private fun handleCodeResentResult(result: Result<VerificationCodeSentResult>) {
        when (result) {
            is Result.Success -> {
                Timber.e("Success! verification id: ${result.data}")
                verificationData = result.data
                _phoneAuthUiStateFlow.value = PhoneAuthUiState.Success
            }
            is Result.Error -> {
                // TODO: Handle error

//              handleError()
                Timber.e("Error : ${result.exception.message}")
                _phoneAuthUiStateFlow.value = PhoneAuthUiState.Error(result.exception)
            }
            is Result.Loading -> {
                _phoneAuthUiStateFlow.value = PhoneAuthUiState.Loading
            }
        }
    }

    private fun handleSignInWithAuthResult(result: Result<SignInWithCredentialResult>) {
        when (result) {
            is Result.Success-> {
                Timber.e("sign in success! new user : ${result.data.isNewUser}")
                _isNewUserLiveData.value = result.data.isNewUser
            }
            is Result.Error-> {
                Timber.e("sign in exception! : ${result.exception.localizedMessage}")
            }
            is Result.Loading->{

            }
        }
    }

    private fun handleVerificationError(e: Throwable) {
        Timber.e("verification error! : ${e.localizedMessage}")
        when (e) {
            is CancellationException -> {
                // TODO: show error (invalid verification code)
            }
            else ->{}
        }
    }

    val handleTextInput = fun(text: String) {
        _verificationCodeStateFlow.value = text
    }
}

sealed class VerifyUiState {
    object None: VerifyUiState()
    object Success: VerifyUiState()
    object Loading: VerifyUiState()
    data class Error(val exception: Throwable): VerifyUiState()
}

