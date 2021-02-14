package com.tistory.mybstory.firechat.ui.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class FirstRunViewModel @Inject constructor(
    val firebaseAuth: FirebaseAuth
) : ViewModel() {


}
