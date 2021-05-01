package com.tistory.mybstory.firechat.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.tistory.mybstory.firechat.data.repository.ChatRepository
import com.tistory.mybstory.firechat.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
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
