package com.tistory.mybstory.firechat.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.tistory.mybstory.firechat.ui.MainActivity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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

/**
 *  fragment extensions *
 *  */

fun Fragment.showProgress() = lifecycleScope.launch {
    val activity = requireActivity() as MainActivity
    activity.eventBus.postEvent(Event.ShowProgress)
}

fun Fragment.hideProgress() = lifecycleScope.launch {
    val activity = requireActivity() as MainActivity
    activity.eventBus.postEvent(Event.HideProgress)
    activity.eventBus.postEvent(Event.None)
}
