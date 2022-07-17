package ru.wearemad.mad_core_compose.vm.dependencies

import ru.wearemad.mad_core_compose.dispatcher.DispatchersProvider
import ru.wearemad.mad_core_compose.message.controller.ComposeMessageController
import ru.wearemad.mad_core_compose.vm.lifecycle.ScreenLifecycleObserver
import ru.wearemad.mad_core_compose.vm.result_listener.VmRequestResultHandler

class DefaultVmDependencies(
    override val dispatchers: DispatchersProvider,
    override val messageController: ComposeMessageController,
    override val screenLifecycleObserver: ScreenLifecycleObserver,
    override val requestResultHandler: VmRequestResultHandler
) : VmDependencies