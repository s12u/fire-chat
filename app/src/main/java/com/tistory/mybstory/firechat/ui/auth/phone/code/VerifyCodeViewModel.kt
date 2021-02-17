package com.tistory.mybstory.firechat.ui.auth.phone.code

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tistory.mybstory.firechat.domain.Result
import com.tistory.mybstory.firechat.domain.usecase.auth.PhoneNumberVerifyResult
import com.tistory.mybstory.firechat.domain.usecase.auth.VerificationCodeSentResult
import com.tistory.mybstory.firechat.domain.usecase.auth.VerifyWithCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class VerifyCodeViewModel @Inject constructor(
    private val verifyWithCodeUseCase: VerifyWithCodeUseCase,
    val handle: SavedStateHandle
) : ViewModel() {

    val phoneNumberLiveData: LiveData<String> = handle.getLiveData("phone_number")
    private val verificationData: VerificationCodeSentResult? = handle.get("verification_data")
    private val _verificationCodeStateFlow = MutableStateFlow("")

    @ExperimentalCoroutinesApi
    fun verifyPhoneNumberWithCode() {
        // TODO: verify phone number with user input (code)
        verificationData?.let {
            verifyWithCodeUseCase(
                verificationData.verificationId
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
            }
            is Result.Error -> {
                Timber.e("code mismatch!")
            }
            else -> {

            }
        }
    }

    val handleTextInput = fun(text: String) {
        _verificationCodeStateFlow.value = text
    }
}
