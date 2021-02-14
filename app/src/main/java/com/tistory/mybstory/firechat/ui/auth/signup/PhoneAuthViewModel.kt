package com.tistory.mybstory.firechat.ui.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.tistory.mybstory.firechat.ui.auth.country.Country
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneAuthViewModel @Inject constructor() : ViewModel() {

    private val _selectedCountryFlow = MutableStateFlow<Country>(Country.US())
    val selectedCountry get() = _selectedCountryFlow.asLiveData()

    fun selectCountry(country: Country) = viewModelScope.launch {
        _selectedCountryFlow.emit(country)
    }
}

