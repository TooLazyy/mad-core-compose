package ru.wearemad.mad_core_compose.vm.core

import androidx.annotation.MainThread
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.wearemad.mad_base.coroutines.RequestResult
import ru.wearemad.mad_core_compose.vm.dependencies.VmDependencies
import ru.wearemad.mad_core_compose.vm.event.DefaultEventsSource
import ru.wearemad.mad_core_compose.vm.event.EventsSource
import ru.wearemad.mad_core_compose.vm.event.VmEvent
import ru.wearemad.mad_core_compose.vm.lifecycle.ScreenLifecycleObserver
import ru.wearemad.mad_core_compose.vm.result_listener.VmRequestResultHandler
import ru.wearemad.mad_core_compose.vm.state.ViewState
import kotlin.coroutines.CoroutineContext

abstract class BaseVm<State : ViewState, Event : VmEvent>(
    state: State,
    protected val dependencies: VmDependencies,
    private val eventSource: EventsSource<Event> = DefaultEventsSource(
        dependencies.dispatchers.io()
    )
) : ViewModel(),
    CoreVm<State, Event>,
    CoroutineScope,
    EventsSource<Event> by eventSource,
    ScreenLifecycleObserver by dependencies.screenLifecycleObserver,
    VmRequestResultHandler by dependencies.requestResultHandler {

    private val parentJob = SupervisorJob()

    protected val stateMutableFlow: MutableStateFlow<State> = MutableStateFlow(state)
    protected val stateMutationsChannel =
        Channel<(currentState: State) -> State>(Channel.BUFFERED)

    protected val currentState: State
        get() = stateMutableFlow.value

    override val coroutineContext: CoroutineContext = dependencies.dispatchers.io() + parentJob

    override val stateFlow: StateFlow<State>
        get() = stateMutableFlow

    init {
        subscribeToStateMutations()
    }

    override fun onCleared() {
        eventSource.cancelEvents()
        dependencies.requestResultHandler.cancelResults()
        parentJob.cancelChildren()
    }

    @MainThread
    protected open fun onError(error: Throwable) {
        showErrorMessage(error)
    }

    @MainThread
    protected open fun showErrorMessage(error: Throwable) {
    }

    protected fun mutateStateAsync(mutator: (currentState: State) -> State) {
        launch {
            mutateState(mutator)
        }
    }

    protected suspend fun mutateState(mutator: (currentState: State) -> State) {
        stateMutationsChannel.send(mutator)
    }

    protected suspend fun runOnUi(block: suspend () -> Unit) =
        withContext(dependencies.dispatchers.main()) {
            block()
        }

    protected suspend fun <T> RequestResult<T>.handleResult(
        block: suspend (T) -> Unit
    ) = runOnUi {
        when (val result = this@handleResult) {
            is RequestResult.Success -> block(result.data)
            is RequestResult.Error -> handleError(result.error)
        }
    }

    protected suspend fun <T> RequestResult<T>.handleResultWithError(
        resultBlock: suspend (T) -> Unit,
        errorBlock: suspend (Throwable) -> Unit
    ) = runOnUi {
        when (val result = this@handleResultWithError) {
            is RequestResult.Success -> resultBlock(result.data)
            is RequestResult.Error -> handleError(result.error, errorBlock)
        }
    }

    private suspend fun handleError(t: Throwable, errorBlock: suspend (Throwable) -> Unit = {}) {
        onError(t)
        errorBlock(t)
    }

    private fun subscribeToStateMutations() {
        launch(dependencies.dispatchers.mainImmediate()) {
            stateMutationsChannel
                .receiveAsFlow()
                .collect {
                    stateMutableFlow.value = it.invoke(stateMutableFlow.value)
                }
        }
    }
}