package ru.wearemad.mad_core_compose.result_handler

import android.os.Bundle
import kotlinx.coroutines.flow.Flow

interface RequestResultStore {

    val resultsChangedFlow: Flow<Unit>

    fun saveState(): Bundle

    fun restoreState(inState: Bundle)

    fun setResultForKey(key: RequestResultKey, data: Any)

    fun getResultForKey(key: RequestResultKey): Any?

    fun consumeResultForKey(key: RequestResultKey)
}