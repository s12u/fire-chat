package com.tistory.mybstory.firechat.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.tistory.mybstory.firechat.ui.MainActivity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun String?.formatDateStringToLocalized(): String {
    return if (!isNullOrEmpty()) {
        val localDate = DateUtils.formatDateToLocalDate(this)
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

fun Fragment.showErrorSnackBar(errorMessage: String) {
    Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_SHORT).show()
}

