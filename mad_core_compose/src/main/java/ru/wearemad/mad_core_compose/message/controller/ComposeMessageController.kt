package ru.wearemad.mad_core_compose.message.controller

import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult

interface ComposeMessageController {

    fun cancelAll()

    fun showSnack(
        text: String,
        actionText: String?,
        duration: SnackbarDuration,
        listener: (result: SnackbarResult) -> Unit
    )

    fun showToast(text: String, duration: Int)
}