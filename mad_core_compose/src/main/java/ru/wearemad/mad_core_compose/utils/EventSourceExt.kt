package ru.wearemad.mad_core_compose.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.wearemad.mad_core_compose.vm.event.EventsSource
import ru.wearemad.mad_core_compose.vm.event.VmEvent

@Composable
fun <Event : VmEvent> EventsSource<Event>.SubscribeToEvents(
    key: Any? = Unit,
    onNewEvent: suspend (Event) -> Unit
) {
    LaunchedEffect(key) {

        launch {
            eventsFlow
                .collect {
                    onNewEvent(it)
                }
        }
    }
}