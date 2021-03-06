package ru.wearemad.mad_core_compose.vm.dependencies

import ru.wearemad.mad_core_compose.message.controller.ComposeMessageController
import ru.wearemad.mad_core_compose.vm.lifecycle.ScreenLifecycleObserver
import ru.wearemad.mad_core_compose.vm.result_listener.VmRequestResultHandler
import ru.wearemad.mad_utils.dispatcher.DispatchersProvider

interface VmDependencies {

    val dispatchers: DispatchersProvider

    val messageController: ComposeMessageController

    val screenLifecycleObserver: ScreenLifecycleObserver

    val requestResultHandler: VmRequestResultHandler
}