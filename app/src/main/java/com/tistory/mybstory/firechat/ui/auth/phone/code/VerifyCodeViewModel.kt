package com.tistory.mybstory.firechat.ui.auth.phone.code

import android.app.Activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tistory.mybstory.firechat.domain.Result
import com.tistory.mybstory.firechat.domain.usecase.auth.PhoneNumberVerifyResult
import com.tistory.mybstory.firechat.domain.usecase.auth.SendVerificationCodeUseCase
import com.tistory.mybstory.firechat.domain.usecase.auth.VerificationCodeSentResult
import com.tistory.mybstory.firechat.domain.usecase.auth.VerifyWithCodeUseCase
import com.tistory.mybstory.firechat.ui.auth.phone.PhoneAuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class VerifyCodeViewModel @Inject constructor(
    private val verifyWithCodeUseCase: VerifyWithCodeUseCase,
    private val sendVerificationCodeUseCase: SendVerificationCodeUseCase,
    val handle: SavedStateHandle
) : ViewModel() {

    val phoneNumberLiveData: LiveData<String> = handle.getLiveData("phone_number")
    private var verificationData: VerificationCodeSentResult? = handle.get("verification_data")
    private var _verificationCodeStateFlow = MutableStateFlow("")

    private val _phoneAuthUiStateFlow: MutableStateFlow<PhoneAuthUiState> = MutableStateFlow(PhoneAuthUiState.None)
    val phoneAuthUiStateFlow: StateFlow<PhoneAuthUiState> get() = _phoneAuthUiStateFlow

    private val _verifyUiStateFlow: MutableStateFlow<VerifyUiState> = MutableStateFlow(VerifyUiState.None)
    val verifyUiStateFlow: StateFlow<VerifyUiState> get() = _verifyUiStateFlow

    @ExperimentalCoroutinesApi
    fun verifyPhoneNumberWithCode() {
        // TODO: verify phone number with user input (code)
        verificationData?.let { data ->
            verifyWithCodeUseCase(
                data.verificationId
                        to _verificationCodeStateFlow.value
            ).onEach { result ->
                handleVerificationResult(result)
            }.launchIn(viewModelScope)
        }
    }

    private fun handleVerificationResult(result: Result<PhoneNumberVerifyResult>) {
        when (result) {
            is Result.Success -> {
                Timber.e("code match!")
                _verifyUiStateFlow.value = VerifyUiState.Success
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

    private fun handleVerificationError(e: Throwable) {
       when (e) {
           is CancellationException -> {
               // TODO: show error (invalid verification code)
           }
           else ->{}
       }
    }

    @ExperimentalCoroutinesApi
    fun resendVerificationCode(activity: Activity) = viewModelScope.launch {
        val phoneNumber = phoneNumberLiveData.value
        Timber.e("phone number: $phoneNumber")
        phoneNumber?.let {
            sendVerificationCodeUseCase(activity to phoneNumber)
                .onEach { result ->
                    when (result) {
                        is Result.Success -> {
                            Timber.e("Success! verification id: ${result.data}")
                            verificationData = result.data
                            _phoneAuthUiStateFlow.value = PhoneAuthUiState.Success
                        }
                        is Result.Error -> {
                            // TODO: Handle error
//                            handleError()
                            Timber.e("Error : ${result.exception.message}")
                            _phoneAuthUiStateFlow.value = PhoneAuthUiState.Error(result.exception)
                        }
                        is Result.Loading -> {
                            _phoneAuthUiStateFlow.value = PhoneAuthUiState.Loading
                        }
                    }
                }.launchIn(viewModelScope)
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
