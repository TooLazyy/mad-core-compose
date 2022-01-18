package ru.wearemad.mad_core_compose.vm.lifecycle.data

sealed class ActivityLifecycleState(
    val stateId: Int
) {

    abstract fun checkOppositeState(state: ActivityLifecycleState): Boolean

    object None : ActivityLifecycleState(-1) {

        override fun checkOppositeState(
            state: ActivityLifecycleState
        ): Boolean = true
    }

    object Created : ActivityLifecycleState(1) {

        override fun checkOppositeState(
            state: ActivityLifecycleState
        ): Boolean = state.stateId == Destroyed.stateId
    }

    object Started : ActivityLifecycleState(2) {

        override fun checkOppositeState(
            state: ActivityLifecycleState
        ): Boolean = state.stateId == Stopped.stateId
    }

    object Resumed : ActivityLifecycleState(3) {

        override fun checkOppositeState(
            state: ActivityLifecycleState
        ): Boolean = state.stateId == Paused.stateId
    }

    object Paused : ActivityLifecycleState(4) {

        override fun checkOppositeState(
            state: ActivityLifecycleState
        ): Boolean = state.stateId == Resumed.stateId
    }

    object Stopped : ActivityLifecycleState(5) {

        override fun checkOppositeState(
            state: ActivityLifecycleState
        ): Boolean = state.stateId == Started.stateId
    }

    object Destroyed : ActivityLifecycleState(6) {

        override fun checkOppositeState(
            state: ActivityLifecycleState
        ): Boolean = state.stateId == Created.stateId
    }
}