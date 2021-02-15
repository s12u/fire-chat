package com.tistory.mybstory.firechat.ui.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.tistory.mybstory.firechat.data.repository.ChatRepository
import com.tistory.mybstory.firechat.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @IoDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val chatRepository: ChatRepository,
    private val savedStateHandle: SavedStateHandle? = null
) : ViewModel() {

    init {
//        viewModelScope.launch {
//            FirebaseAuth.getInstance().signInAnonymously().await()
//            val now = Instant.now()
//            chatRepository.getByBefore("DOjgluBkFWvLm4fn9xoX", Timestamp(now.epochSecond, now.nano))
//                .collect {
//                    Timber.e("size : ${it.size}")
//                    it.forEach { msg -> Timber.e(msg.value) }
//                }
//        }
    }
}
