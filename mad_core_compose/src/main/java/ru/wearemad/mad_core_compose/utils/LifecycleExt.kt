package ru.wearemad.mad_core_compose.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import ru.wearemad.mad_core_compose.vm.lifecycle.ScreenLifecycleObserver
import ru.wearemad.mad_core_compose.vm.lifecycle.data.ActivityLifecycleState
import ru.wearemad.mad_core_compose.vm.lifecycle.data.ScreenLifecycleState

@Composable
fun rememberLifecycleObserver(
    key: Any? = Unit,
    onCreate: (owner: LifecycleOwner) -> Unit = {},
    onResume: (owner: LifecycleOwner) -> Unit = {},
    onStart: (owner: LifecycleOwner) -> Unit = {},
    onPause: (owner: LifecycleOwner) -> Unit = {},
    onStop: (owner: LifecycleOwner) -> Unit = {},
    onDestroy: (owner: LifecycleOwner) -> Unit = {},
): LifecycleObserver = remember<LifecycleObserver>(key) {
    object : DefaultLifecycleObserver {

        override fun onCreate(owner: LifecycleOwner) {
            onCreate(owner)
        }

        override fun onResume(owner: LifecycleOwner) {
            onResume(owner)
        }

        override fun onStart(owner: LifecycleOwner) {
            onStart(owner)
        }

        override fun onPause(owner: LifecycleOwner) {
            onPause(owner)
        }

        override fun onStop(owner: LifecycleOwner) {
            onStop(owner)
        }

        override fun onDestroy(owner: LifecycleOwner) {
            onDestroy(owner)
        }
    }
}

@Composable
fun SubscribeToLifecycle(
    screenLifecycleObserver: ScreenLifecycleObserver
) {
    val lifecycleOwner = LocalContext.current as? LifecycleOwner
    val activityLifecycleObserver = rememberLifecycleObserver(
        onCreate = {
            screenLifecycleObserver.onActivityStateChanged(ActivityLifecycleState.Created)
        },
        onStart = {
            screenLifecycleObserver.onActivityStateChanged(ActivityLifecycleState.Started)
        },
        onResume = {
            screenLifecycleObserver.onActivityStateChanged(ActivityLifecycleState.Resumed)
        },
        onPause = {
            screenLifecycleObserver.onActivityStateChanged(ActivityLifecycleState.Paused)
        },
        onStop = {
            screenLifecycleObserver.onActivityStateChanged(ActivityLifecycleState.Stopped)
        },
        onDestroy = {
            screenLifecycleObserver.onActivityStateChanged(ActivityLifecycleState.Destroyed)
        }
    )
    LaunchedEffect(Unit) {
        screenLifecycleObserver.onScreenStateChanged(ScreenLifecycleState.Active)
        lifecycleOwner?.lifecycle?.addObserver(activityLifecycleObserver)
    }
    DisposableEffect(Unit) {

        onDispose {
            screenLifecycleObserver.onScreenStateChanged(ScreenLifecycleState.Inactive)
            lifecycleOwner?.lifecycle?.removeObserver(activityLifecycleObserver)
        }
    }
}