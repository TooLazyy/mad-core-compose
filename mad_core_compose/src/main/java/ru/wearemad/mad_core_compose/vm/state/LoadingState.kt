package ru.wearemad.mad_core_compose.vm.state

import androidx.compose.runtime.Stable

@Stable
sealed class LoadingState {

    @Stable
    object None : LoadingState()

    @Stable
    object Loading : LoadingState()
}