package ru.wearemad.mad_core_compose.message.controller

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DefaultComposeMessageController(
    private val snackbarHostState: SnackbarHostState,
    private val context: Context,
    private val coroutineScope: CoroutineScope
) : ComposeMessageController {

    private var toast: Toast? = null
    private var snackJob: Job? = null

    init {
        Log.d("MIINE", "DefaultComposeMessageController init")
    }

    override fun showSnack(
        text: String,
        actionText: String?,
        duration: SnackbarDuration,
        listener: (result: SnackbarResult) -> Unit
    ) {
        cancelSnackbar()
        snackJob = coroutineScope.launch {
            val result = snackbarHostState.showSnackbar(
                message = text,
                actionLabel = actionText
            )
            listener(result)
        }
    }

    override fun showToast(text: String, duration: Int) {
        cancelToast()
        toast = Toast.makeText(
            context,
            text,
            duration
        )?.also {
            it.show()
        }
    }

    override fun cancelAll() {
        clear()
    }

    private fun clear() {
        cancelToast()
        cancelSnackbar()
    }

    private fun cancelToast() {
        toast?.cancel()
        toast = null
    }

    private fun cancelSnackbar() {
        snackbarHostState.currentSnackbarData?.dismiss()
        snackJob?.cancel()
        snackJob = null
    }
}