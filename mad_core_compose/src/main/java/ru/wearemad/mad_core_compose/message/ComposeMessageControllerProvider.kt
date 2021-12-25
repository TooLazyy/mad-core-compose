package ru.wearemad.mad_core_compose.message

import ru.wearemad.mad_core_compose.message.holder.DefaultMessageControllerHolder
import ru.wearemad.mad_core_compose.message.wrapper.ComposeMessageControllerWrapper
import ru.wearemad.mad_core_compose.message.wrapper.DefaultComposeMessageControllerWrapper

class ComposeMessageControllerProvider<Controller : ComposeMessageControllerWrapper>(
    val messageController: Controller
) {

    companion object {

        fun create(): ComposeMessageControllerProvider<DefaultComposeMessageControllerWrapper> =
            ComposeMessageControllerProvider(
                DefaultComposeMessageControllerWrapper(
                    DefaultMessageControllerHolder()
                )
            )

        fun <Controller : ComposeMessageControllerWrapper> create(
            wrapper: Controller
        ): ComposeMessageControllerProvider<Controller> =
            ComposeMessageControllerProvider(wrapper)
    }
}