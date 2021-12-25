package ru.wearemad.mad_core_compose.vm.lifecycle.data

sealed class ActivityLifecycleState(
    val stateId: Int
) {

    object None : ActivityLifecycleState(-1)

    object Created : ActivityLifecycleState(1)

    object Started : ActivityLifecycleState(2)

    object Resumed : ActivityLifecycleState(3)

    object Paused : ActivityLifecycleState(4)

    object Stopped : ActivityLifecycleState(5)

    object Destroyed : ActivityLifecycleState(6)
}