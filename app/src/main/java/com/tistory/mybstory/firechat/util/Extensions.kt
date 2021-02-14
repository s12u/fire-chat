package com.tistory.mybstory.firechat.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

fun String?.getPosterThumbUrl(): String {
    return if (!isNullOrEmpty()) {
        "https://image.tmdb.org/t/p/w500$this"
    } else ""
}

fun String?.getOriginalImageUrl(): String {
    return if (!isNullOrEmpty()) {
        "https://image.tmdb.org/t/p/original/$this"
    } else ""
}

fun String?.formatDateStringToLocalized(): String {
    return if (!isNullOrEmpty()) {
        val localDate = DateUtils.formatDateToLocalDate(this!!)
        DateUtils.formatLocalDateToString(localDate)
    } else ""
}

fun ViewModel.viewModelScope(dispatcher: CoroutineDispatcher?) =
    if (dispatcher != null) CoroutineScope(dispatcher) else this.viewModelScope
