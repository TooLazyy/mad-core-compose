package ru.wearemad.mad_core_compose.vm.result_listener

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import ru.wearemad.mad_core_compose.result_handler.RequestResultData

interface VmRequestResultHandler : CoroutineScope {

    val resultsFlow: Flow<RequestResultData>

    fun registerResultKeys(vararg keys: String)

    fun setResult(key: String, result: Any)

    fun cancelResults()
}