package ru.wearemad.mad_core_compose.message.data

import androidx.compose.runtime.Stable

@Stable
sealed interface AppSnackDuration {

    object Forever : AppSnackDuration

    data class WithTime(
        val durationMs: Long
    ) : AppSnackDuration
}
