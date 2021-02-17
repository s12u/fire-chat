package com.tistory.mybstory.firechat.util

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class EventBus {
    private val _events = MutableSharedFlow<Event>() // private mutable shared flow
    val events = _events.asSharedFlow() // publicly exposed as read-only shared flow

    suspend fun postEvent(event: Event) {
        _events.emit(event) // suspends until all subscribers receive it
    }
}

sealed class Event {
    object ShowProgress: Event()
    object HideProgress: Event()
}
