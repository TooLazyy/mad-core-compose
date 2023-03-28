package ru.wearemad.mad_core_compose.message.controller

import ru.wearemad.mad_core_compose.message.data.AppShackData

interface ComposeMessageController {

    fun cancelAll()

    fun showSnack(
        data: AppShackData,
    )

    fun showToast(text: String, duration: Int)

    sealed interface AppSnackBarEvent {

        object Hide : AppSnackBarEvent

        class Show(
            val data: AppShackData,
        ) : AppSnackBarEvent
    }
}