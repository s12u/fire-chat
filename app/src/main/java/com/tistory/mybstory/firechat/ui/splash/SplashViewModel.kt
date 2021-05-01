package com.tistory.mybstory.firechat.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.tistory.mybstory.firechat.util.isSignedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val auth: FirebaseAuth
): ViewModel() {

    private val _signInState = MutableSharedFlow<Boolean>(0)
    val signInState: SharedFlow<Boolean> get() = _signInState

    fun checkSignInStatus() = viewModelScope.launch {
        _signInState.emit(auth.isSignedIn())
    }

}
