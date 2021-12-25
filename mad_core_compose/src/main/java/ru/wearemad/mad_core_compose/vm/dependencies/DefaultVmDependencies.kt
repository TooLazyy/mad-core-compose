package ru.wearemad.mad_core_compose.vm.dependencies

import ru.wearemad.mad_base.coroutines.DispatchersProvider
import ru.wearemad.mad_core_compose.message.controller.ComposeMessageController
import ru.wearemad.mad_core_compose.vm.lifecycle.ScreenLifecycleObserver

class DefaultVmDependencies(
    override val dispatchers: DispatchersProvider,
    override val messageController: ComposeMessageController,
    override val screenLifecycleObserver: ScreenLifecycleObserver
) : VmDependencies