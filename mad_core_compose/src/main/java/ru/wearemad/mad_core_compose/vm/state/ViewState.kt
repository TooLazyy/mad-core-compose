package ru.wearemad.mad_core_compose.vm.state

import androidx.compose.runtime.Stable

@Stable
interface ViewState {

    val loadingState: LoadingState
}