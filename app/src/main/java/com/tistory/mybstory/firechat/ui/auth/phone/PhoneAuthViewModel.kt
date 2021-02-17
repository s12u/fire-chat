package com.tistory.mybstory.firechat.ui.auth.phone

import android.app.Activity
import androidx.lifecycle.*
import com.tistory.mybstory.firechat.domain.Result
import com.tistory.mybstory.firechat.domain.usecase.auth.SendVerificationCodeUseCase
import com.tistory.mybstory.firechat.domain.usecase.auth.VerificationCodeSentResult
import com.tistory.mybstory.firechat.ui.auth.phone.country.Country
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PhoneAuthViewModel @Inject constructor(
    private val sendVerificationCodeUseCase: SendVerificationCodeUseCase,
) : ViewModel() {

    init {
        Timber.e("created!")
    }

    private val _selectedCountryFlow = MutableStateFlow<Country>(Country.US())
    val selectedCountry get() = _selectedCountryFlow.asLiveData(viewModelScope.coroutineContext)

    private val _phoneNumberFlow = MutableStateFlow("")
    val phoneNumberFlow: Flow<String>
        get() = _phoneNumberFlow.map { value ->
            "+${_selectedCountryFlow.value.code}$value"
        }

    val displayPhoneNumber: LiveData<String>
        get() = _phoneNumberFlow.asLiveData(viewModelScope.coroutineContext)
            .map { value ->
                "+${_selectedCountryFlow.value.code} $value"
            }

    private val _verificationDataFlow = MutableStateFlow(
        VerificationCodeSentResult(
            "",
            null
        )
    )
    val verificationDataFlow: StateFlow<VerificationCodeSentResult> get() = _verificationDataFlow

    private val _phoneAuthUiStateFlow: MutableStateFlow<PhoneAuthUiState> = MutableStateFlow(PhoneAuthUiState.Entered)
    val phoneAuthUiStateFlow: StateFlow<PhoneAuthUiState> get() = _phoneAuthUiStateFlow

    fun selectCountry(country: Country) = viewModelScope.launch {
        _selectedCountryFlow.emit(country)
    }

    @ExperimentalCoroutinesApi
    fun sendVerificationCode(activity: Activity) = viewModelScope.launch {
        val phoneNumber = phoneNumberFlow.stateIn(this).value
        Timber.e("phone number: $phoneNumber")
        sendVerificationCodeUseCase(activity to phoneNumber)
            .onEach { result ->
                when (result) {
                    is Result.Success -> {
                        Timber.e("Success! verification id: ${result.data}")
                        _verificationDataFlow.value = result.data
                        _phoneAuthUiStateFlow.value = PhoneAuthUiState.Success
                    }
                    is Result.Error -> {
                        // TODO: Handle error
                        Timber.e("Error : ${result.exception.message}")
                        _phoneAuthUiStateFlow.value = PhoneAuthUiState.Error(result.exception)
                    }
                    is Result.Loading -> {
                        _phoneAuthUiStateFlow.value = PhoneAuthUiState.Loading
                    }
                }
            }.launchIn(viewModelScope)
    }

    val handleTextInput = fun(text: String) {
        _phoneNumberFlow.value = text
    }
}

sealed class PhoneAuthUiState {
    object Entered: PhoneAuthUiState()
    object Success: PhoneAuthUiState()
    object Loading: PhoneAuthUiState()
    data class Error(val exception: Throwable): PhoneAuthUiState()
}


