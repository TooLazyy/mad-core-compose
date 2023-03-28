package ru.wearemad.mad_core_compose.message

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.wearemad.mad_core_compose.message.controller.ComposeMessageController
import ru.wearemad.mad_core_compose.message.controller.DefaultComposeMessageController
import ru.wearemad.mad_core_compose.message.data.AppSnackDuration
import ru.wearemad.mad_core_compose.message.data.AppSnackState
import ru.wearemad.mad_core_compose.message.holder.MessageControllerHolder
import ru.wearemad.mad_core_compose.utils.rememberLifecycleObserver

@Composable
fun rememberAppSnackState(
    holder: MessageControllerHolder,
    controllerFactory: (
        context: Context,
        onSnackBarEvent: (ComposeMessageController.AppSnackBarEvent) -> Unit
    ) -> ComposeMessageController = { context, onSnackBarEvent ->
        DefaultComposeMessageController(
            context,
            onSnackBarEvent
        )
    }
): AppSnackState {
    val context = LocalContext.current
    val lifecycleOwner = context as? LifecycleOwner

    val coroutineScope = rememberCoroutineScope()

    var snackJob: Job? = remember {
        null
    }
    val appSnackState: MutableState<AppSnackState> = remember {
        mutableStateOf(AppSnackState.Hidden)
    }

    fun cancelAndHideAppSnack() {
        snackJob?.cancel()
        snackJob = null
        appSnackState.value = AppSnackState.Hidden
    }

    val controller = remember {
        controllerFactory(context) { event ->
            when (event) {
                is ComposeMessageController.AppSnackBarEvent.Hide -> {
                    cancelAndHideAppSnack()
                }
                is ComposeMessageController.AppSnackBarEvent.Show -> {
                    when (event.data.duration) {
                        is AppSnackDuration.Forever -> {
                            appSnackState.value = AppSnackState.Visible(
                                event.data.payload
                            )
                        }
                        is AppSnackDuration.WithTime -> {
                            snackJob = coroutineScope.launch {
                                appSnackState.value = AppSnackState.Visible(
                                    event.data.payload
                                )
                                delay(event.data.duration.durationMs)
                                cancelAndHideAppSnack()
                            }
                        }
                    }
                }
            }
        }
    }
    val lifecycleObserver = rememberLifecycleObserver(
        key = Unit,
        onResume = {
            holder.attachController(controller)
        },
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
    return appSnackState.value
}