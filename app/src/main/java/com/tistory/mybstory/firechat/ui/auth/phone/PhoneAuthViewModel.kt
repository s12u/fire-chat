package com.tistory.mybstory.firechat.ui.auth.phone

import androidx.lifecycle.*
import com.tistory.mybstory.firechat.ui.auth.phone.country.Country
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PhoneAuthViewModel @Inject constructor() : ViewModel() {

    init {
        Timber.e("created!")
    }

    private val _selectedCountryFlow = MutableStateFlow<Country>(Country.US())
    val selectedCountry get() = _selectedCountryFlow.asLiveData(viewModelScope.coroutineContext)

    private val _phoneNumberFlow = MutableStateFlow("")
    val phoneNumberFlow: Flow<String>
        get() = _phoneNumberFlow.map { value ->
            "+${_selectedCountryFlow.value.code} $value"
        }

    val displayPhoneNumber: LiveData<String>
        get() = _phoneNumberFlow.asLiveData(viewModelScope.coroutineContext)
            .map { value ->
                "+${_selectedCountryFlow.value.code} $value"
            }

    fun selectCountry(country: Country) = viewModelScope.launch {
        _selectedCountryFlow.emit(country)
    }

    val handleTextInput = fun(text: String) {
        _phoneNumberFlow.value = text
    }

}
