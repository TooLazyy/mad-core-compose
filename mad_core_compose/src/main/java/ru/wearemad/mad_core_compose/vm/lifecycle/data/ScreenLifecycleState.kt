package ru.wearemad.mad_core_compose.vm.lifecycle.data

sealed class ScreenLifecycleState {

    /**
     * Initial state
     */
    object None : ScreenLifecycleState()

    /**
     * Composable screen becomes visible(active)
     */
    object Active : ScreenLifecycleState()

    /**
     * Composable screen becomes invisible. Probably hidden by another composable screen
     */
    object Inactive : ScreenLifecycleState()
}