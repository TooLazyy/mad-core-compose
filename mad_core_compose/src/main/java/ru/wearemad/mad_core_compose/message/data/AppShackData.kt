package ru.wearemad.mad_core_compose.message.data

import androidx.compose.runtime.Stable

@Stable
data class AppShackData(
    val payload: Any?,
    val duration: AppSnackDuration,
)