package ru.wearemad.mad_core_compose.vm.core

import kotlinx.coroutines.flow.StateFlow
import ru.wearemad.mad_core_compose.vm.event.EventsSource
import ru.wearemad.mad_core_compose.vm.event.VmEvent
import ru.wearemad.mad_core_compose.vm.lifecycle.ScreenLifecycleObserver
import ru.wearemad.mad_core_compose.vm.state.ViewState

interface CoreVm<State : ViewState, Event : VmEvent> :
    EventsSource<Event>,
    ScreenLifecycleObserver {

    val stateFlow: StateFlow<State>
}