package ru.wearemad.mad_core_compose.data

sealed class RequestResult<out T : Any?> {

    data class Success<T : Any?>(val data: T) : RequestResult<T>()

    data class Error(val error: Throwable) : RequestResult<Nothing>()
}