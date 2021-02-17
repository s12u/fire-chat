package com.tistory.mybstory.firechat.domain

sealed class Result<out R> {

    data class Success<out T>(val data: T): Result<T>()
    data class Error(val exception: Throwable): Result<Nothing>()
    object Loading: Result<Nothing>()
}
