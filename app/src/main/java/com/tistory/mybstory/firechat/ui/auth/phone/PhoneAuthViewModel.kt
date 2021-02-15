package com.tistory.mybstory.firechat.ui.auth.phone

import androidx.lifecycle.*
import com.tistory.mybstory.firechat.ui.auth.country.Country
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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

    private val _phoneNumberLiveData = MutableLiveData("")
    val phoneNumberLiveData: LiveData<String> get() = _phoneNumberLiveData.map { value ->
        "+${_selectedCountryFlow.value.code}$value"
    }

    val displayPhoneNumber: LiveData<String> get() = _phoneNumberLiveData.map { value ->
        "+${_selectedCountryFlow.value.code} $value"
    }

    fun selectCountry(country: Country) = viewModelScope.launch {
        _selectedCountryFlow.emit(country)
    }

    val handleTextInput = fun(text: String) {
        _phoneNumberLiveData.value = text
    }
}

