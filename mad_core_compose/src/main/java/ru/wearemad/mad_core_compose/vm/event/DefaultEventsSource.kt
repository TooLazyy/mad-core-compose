package ru.wearemad.mad_core_compose.vm.event

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DefaultEventsSource<Event : VmEvent>(
    dispatcher: CoroutineDispatcher
) : EventsSource<Event>,
    CoroutineScope {

    private val parentJob = SupervisorJob()

    private val eventsChannel = Channel<Event>(capacity = Channel.BUFFERED)

    override val coroutineContext: CoroutineContext = dispatcher + parentJob

    override val eventsFlow: Flow<Event>
        get() = eventsChannel.receiveAsFlow()

    override fun cancelEvents() {
        parentJob.cancelChildren()
    }

    override fun sendEventAsync(event: Event) {
        launch {
            sendEvent(event)
        }
    }

    override suspend fun sendEvent(event: Event) {
        eventsChannel.send(event)
    }
}