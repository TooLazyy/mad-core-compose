package ru.wearemad.mad_core_compose.message.wrapper

import ru.wearemad.mad_core_compose.message.data.AppShackData
import ru.wearemad.mad_core_compose.message.data.AppSnackActionResult
import ru.wearemad.mad_core_compose.message.holder.MessageControllerHolder

class DefaultComposeMessageControllerWrapper(
    override val holder: MessageControllerHolder
) : ComposeMessageControllerWrapper {

    override fun cancelAll() {
        holder.getMessageController()?.cancelAll()
    }

    override fun showSnack(
        data: AppShackData,
        listener: (result: AppSnackActionResult) -> Unit
    ) {
        holder.getMessageController()?.showSnack(
            data, listener
        )
    }

    override fun showToast(text: String, duration: Int) {
        holder.getMessageController()?.showToast(text, duration)
    }
}