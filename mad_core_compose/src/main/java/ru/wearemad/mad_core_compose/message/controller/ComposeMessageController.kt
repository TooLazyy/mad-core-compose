package ru.wearemad.mad_core_compose.message.controller

import ru.wearemad.mad_core_compose.message.data.AppShackData
import ru.wearemad.mad_core_compose.message.data.AppSnackActionResult

interface ComposeMessageController {

    fun cancelAll()

    fun showSnack(
        data: AppShackData,
        listener: (result: AppSnackActionResult) -> Unit
    )

    fun showToast(text: String, duration: Int)

    sealed interface AppSnackBarEvent {

        object Hide : AppSnackBarEvent

        class Show(
            val data: AppShackData,
            val listener: (result: AppSnackActionResult) -> Unit
        ) : AppSnackBarEvent
    }
}