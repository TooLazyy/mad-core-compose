package ru.wearemad.mad_core_compose.message.data

import androidx.compose.runtime.Stable

@Stable
sealed interface AppSnackState {

    object Hidden : AppSnackState

    data class Visible(
        val payload: Any?
    ) : AppSnackState
}