package ru.wearemad.mad_core_compose.vm.lifecycle

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import ru.wearemad.mad_core_compose.vm.lifecycle.data.ActivityLifecycleState
import ru.wearemad.mad_core_compose.vm.lifecycle.data.LifecycleStateData
import ru.wearemad.mad_core_compose.vm.lifecycle.data.ScreenLifecycleState
import kotlin.properties.Delegates

class DefaultScreenLifecycleObserver : ScreenLifecycleObserver {

    private var currentState: LifecycleStateData by Delegates.observable(
        LifecycleStateData()
    ) { _, _, newValue ->
        viewLifecycleFlow.tryEmit(newValue)
    }

    private val viewLifecycleFlow = MutableSharedFlow<LifecycleStateData>(
        replay = 1,
        extraBufferCapacity = 7
    )

    override val lifecycleStateFlow: Flow<LifecycleStateData> = viewLifecycleFlow

    override val lifecycleState: LifecycleStateData
        get() = currentState

    override fun onScreenStateChanged(screenLifecycleState: ScreenLifecycleState) {
        currentState = currentState.copy(
            screenLifecycleState = screenLifecycleState
        )
    }

    override fun onActivityStateChanged(activityLifecycleState: ActivityLifecycleState) {
        val activityCurrentState = currentState.activityLifecycleState
        /**
         * When we attach lifecycle observer to a LifecycleOwner,
         * it's get through the entire lifecycle state up to current
         * Example: we attach observer for the first time, events go Create -> Started -> Resumed.
         * Then re-attach, events are again Create -> Started -> Resumed (cuz resume is the current state).
         * In order to prevent this, check new event id is strictly less, then new one.
         * We can't fall into start or create state after resume if destroy was not called
         */
        if (activityLifecycleState.stateId <= activityCurrentState.stateId &&
            activityCurrentState.stateId != ActivityLifecycleState.Destroyed.stateId
        ) {
            return
        }
        currentState = currentState.copy(
            activityLifecycleState = activityLifecycleState
        )
    }
}