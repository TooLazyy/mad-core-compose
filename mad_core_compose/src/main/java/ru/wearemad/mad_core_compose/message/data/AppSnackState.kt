package ru.wearemad.mad_core_compose.message.data

import androidx.compose.runtime.Stable

@Stable
sealed interface AppSnackState {

    val payload: Any?

    data class Hidden(
        override val payload: Any?
    ) : AppSnackState

    data class Visible(
        override val payload: Any?
    ) : AppSnackState
}