package ru.wearemad.mad_core_compose.vm.result_listener

import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.ConcurrentHashMap
import ru.wearemad.mad_core_compose.result_handler.RequestResultData
import ru.wearemad.mad_core_compose.result_handler.RequestResultKey
import ru.wearemad.mad_core_compose.result_handler.RequestResultStore
import ru.wearemad.mad_core_compose.vm.lifecycle.ScreenLifecycleObserver
import ru.wearemad.mad_core_compose.vm.lifecycle.data.ActivityLifecycleState
import ru.wearemad.mad_core_compose.vm.lifecycle.data.LifecycleStateData
import ru.wearemad.mad_core_compose.vm.lifecycle.data.ScreenLifecycleState
import ru.wearemad.mad_utils.dispatcher.DispatchersProvider
import kotlin.coroutines.CoroutineContext

class DefaultVmRequestResultHandler(
    dispatchersProvider: DispatchersProvider,
    private val requestResultStore: RequestResultStore,
    private val screenLifecycleObserver: ScreenLifecycleObserver
) : VmRequestResultHandler {

    private companion object {

        const val RESULTS_BUFFER_CAPACITY = 32
    }

    private val job = SupervisorJob()

    private val keysSet = ConcurrentHashMap.newKeySet<RequestResultKey>()
    private val resultsMutex = Mutex()
    private val resultsSharedFlow = MutableSharedFlow<RequestResultData>(
        extraBufferCapacity = RESULTS_BUFFER_CAPACITY,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override val coroutineContext: CoroutineContext = job + dispatchersProvider.io()

    override val resultsFlow: Flow<RequestResultData> = resultsSharedFlow
        .onSubscription { checkCanAskForResults() }

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
        job.cancel()
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
        resultsMutex.withLock {
            if (resultsSharedFlow.subscriptionCount.value == 0) {
                return@withLock
            }
            keysSet.forEach {
                val result = requestResultStore.getResultForKey(it)
                if (result != null && checkLifecycle()) {
                    requestResultStore.consumeResultForKey(it)
                    resultsSharedFlow.tryEmit(RequestResultData(it, result))
                }
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