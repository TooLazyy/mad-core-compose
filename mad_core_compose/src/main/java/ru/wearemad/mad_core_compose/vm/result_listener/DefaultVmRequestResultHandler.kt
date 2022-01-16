package ru.wearemad.mad_core_compose.vm.result_listener

import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.wearemad.mad_base.coroutines.DispatchersProvider
import ru.wearemad.mad_core_compose.result_handler.RequestResultData
import ru.wearemad.mad_core_compose.result_handler.RequestResultKey
import ru.wearemad.mad_core_compose.result_handler.RequestResultStore
import ru.wearemad.mad_core_compose.vm.lifecycle.ScreenLifecycleObserver
import ru.wearemad.mad_core_compose.vm.lifecycle.data.ActivityLifecycleState
import ru.wearemad.mad_core_compose.vm.lifecycle.data.LifecycleStateData
import ru.wearemad.mad_core_compose.vm.lifecycle.data.ScreenLifecycleState
import kotlin.coroutines.CoroutineContext

class DefaultVmRequestResultHandler(
    dispatchersProvider: DispatchersProvider,
    private val requestResultStore: RequestResultStore,
    private val screenLifecycleObserver: ScreenLifecycleObserver
) : VmRequestResultHandler {

    private val job = SupervisorJob()

    private val keysSet = mutableSetOf<RequestResultKey>()
    private val resultsChannel = Channel<RequestResultData>(
        capacity = Channel.BUFFERED
    )

    override val coroutineContext: CoroutineContext = job + dispatchersProvider.io()

    override val resultsFlow: Flow<RequestResultData>
        get() = resultsChannel.receiveAsFlow()

    init {
        launch {
            screenLifecycleObserver
                .lifecycleStateFlow
                .filter { it.checkFitForResult() }
                .collect {
                    checkCanAskForResults()
                }
        }
        launch {
            requestResultStore
                .resultsChangedFlow
                .collect {
                    checkCanAskForResults()
                }
        }
    }

    override fun cancelResults() {
        job.cancelChildren()
    }

    override fun setResult(key: String, result: Any) {
        requestResultStore.setResultForKey(
            RequestResultKey(key),
            result 
        )
    }

    override fun registerResultKeys(vararg keys: String) {
        keys.forEach {
            keysSet.add(RequestResultKey(it))
        }
        launch {
            checkCanAskForResults()
        }
    }

    private suspend fun checkCanAskForResults() {
        keysSet.forEach {
            val result = requestResultStore.getResultForKey(it)
            if (result != null && checkLifecycle()) {
                requestResultStore.consumeResultForKey(it)
                resultsChannel.send(RequestResultData(it, result))
            }
        }
    }

    private fun checkLifecycle(): Boolean {
        val lifecycleState = screenLifecycleObserver.lifecycleState
        return lifecycleState.checkFitForResult()
    }

    private fun LifecycleStateData.checkFitForResult(): Boolean =
        screenLifecycleState is ScreenLifecycleState.Active &&
                activityLifecycleState is ActivityLifecycleState.Resumed
}