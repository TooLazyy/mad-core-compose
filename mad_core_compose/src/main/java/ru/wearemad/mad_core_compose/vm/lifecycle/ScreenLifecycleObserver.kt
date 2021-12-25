package ru.wearemad.mad_core_compose.vm.lifecycle

import kotlinx.coroutines.flow.Flow
import ru.wearemad.mad_core_compose.vm.lifecycle.data.ActivityLifecycleState
import ru.wearemad.mad_core_compose.vm.lifecycle.data.LifecycleStateData
import ru.wearemad.mad_core_compose.vm.lifecycle.data.ScreenLifecycleState

interface ScreenLifecycleObserver {

    val lifecycleStateFlow: Flow<LifecycleStateData>

    fun onScreenStateChanged(screenLifecycleState: ScreenLifecycleState)

    fun onActivityStateChanged(activityLifecycleState: ActivityLifecycleState)
}