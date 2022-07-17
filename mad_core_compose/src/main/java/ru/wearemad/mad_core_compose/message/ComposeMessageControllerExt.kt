package ru.wearemad.mad_core_compose.message

import android.content.Context
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineScope
import ru.wearemad.mad_core_compose.message.controller.ComposeMessageController
import ru.wearemad.mad_core_compose.message.controller.DefaultComposeMessageController
import ru.wearemad.mad_core_compose.message.holder.MessageControllerHolder
import ru.wearemad.mad_core_compose.utils.rememberLifecycleObserver

@Composable
fun rememberSnackbarHostState(
    holder: MessageControllerHolder,
    controllerFactory: (
        snackbarHostState: SnackbarHostState,
        context: Context,
        coroutineScope: CoroutineScope
    ) -> ComposeMessageController = { snackbarHostState, context, coroutineScope ->
        DefaultComposeMessageController(
            snackbarHostState,
            context,
            coroutineScope
        )
    }
): SnackbarHostState {
    val context = LocalContext.current
    val lifecycleOwner = context as? LifecycleOwner
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val coroutineScope = rememberCoroutineScope()
    val controller = remember(snackbarHostState) {
        controllerFactory(
            snackbarHostState,
            context,
            coroutineScope
        )
    }
    val lifecycleObserver = rememberLifecycleObserver(
        key = Unit,
        onPause = {
            holder.detachController()
        }
    )
    LaunchedEffect(Unit) {
        lifecycleOwner?.lifecycle?.addObserver(lifecycleObserver)
        holder.attachController(controller)
    }
    DisposableEffect(Unit) {

        onDispose {
            holder.detachController()
            lifecycleOwner?.lifecycle?.removeObserver(lifecycleObserver)
        }
    }
    return snackbarHostState
}