package ru.wearemad.mad_core_compose.result_handler

import android.os.Bundle
import android.os.Parcelable
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.io.Serializable

class DefaultRequestResultStore : RequestResultStore {

    companion object {

        private const val KEY_DATA = "request_result_store_data"
    }

    private val results = hashMapOf<RequestResultKey, Any>()
    private val resultsChangedChannel = MutableSharedFlow<Unit>(
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        extraBufferCapacity = 5
    )

    override val resultsChangedFlow: Flow<Unit> = resultsChangedChannel

    override fun saveState(): Bundle = Bundle().apply {
        putBundle(KEY_DATA, prepareBundle())
    }

    override fun restoreState(inState: Bundle) {
        val data = inState.getBundle(KEY_DATA) ?: return
        restoreFromBundle(data)
    }

    override fun setResultForKey(key: RequestResultKey, data: Any) {
        if (data !is Serializable && data !is Parcelable) {
            throw RuntimeException("$data must implement Parcelable or Serializable")
        }
        results[key] = data
        resultsChangedChannel.tryEmit(Unit)
    }

    override fun getResultForKey(key: RequestResultKey): Any? = results[key]

    override fun consumeResultForKey(key: RequestResultKey) {
        results.remove(key)
    }

    private fun restoreFromBundle(bundle: Bundle) {
        bundle.keySet().forEach {
            val data = bundle.get(it)
            if (data != null) {
                results[RequestResultKey(it)] = data
            }
        }
    }

    private fun prepareBundle(): Bundle {
        val output = Bundle()
        results.forEach {
            when (it.value) {
                is Parcelable -> output.putParcelable(it.key.key, it.value as Parcelable)
                is Serializable -> output.putSerializable(it.key.key, it.value as Serializable)
                else -> throw RuntimeException(
                    "RootResultManager: ${it.value} must implement Parcelable or Serializable"
                )
            }
        }
        return output
    }
}