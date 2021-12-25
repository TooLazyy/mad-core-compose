package ru.wearemad.mad_core_compose.vm.lifecycle.data

data class LifecycleStateData(
    val screenLifecycleState: ScreenLifecycleState = ScreenLifecycleState.None,
    val activityLifecycleState: ActivityLifecycleState = ActivityLifecycleState.None
)