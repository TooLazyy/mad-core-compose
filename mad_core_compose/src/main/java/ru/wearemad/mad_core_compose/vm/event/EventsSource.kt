package ru.wearemad.mad_core_compose.vm.event

import kotlinx.coroutines.flow.Flow

interface EventsSource<Event : VmEvent> {

    val eventsFlow: Flow<Event>

    fun cancelEvents()

    fun sendEventAsync(event: Event)

    suspend fun sendEvent(event: Event)
}