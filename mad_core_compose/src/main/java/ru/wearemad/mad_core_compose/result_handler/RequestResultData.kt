package ru.wearemad.mad_core_compose.result_handler

class RequestResultData(
    val key: RequestResultKey,
    val data: Any
) {

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> castData(): T = data as T
}