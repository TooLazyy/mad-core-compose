package ru.wearemad.mad_core_compose.message.wrapper

import ru.wearemad.mad_core_compose.message.data.AppShackData
import ru.wearemad.mad_core_compose.message.holder.MessageControllerHolder

class DefaultComposeMessageControllerWrapper(
    override val holder: MessageControllerHolder
) : ComposeMessageControllerWrapper {

    override fun cancelAll() {
        holder.getMessageController()?.cancelAll()
    }

    override fun showSnack(
        data: AppShackData,
    ) {
        holder.getMessageController()?.showSnack(data)
    }

    override fun showToast(text: String, duration: Int) {
        holder.getMessageController()?.showToast(text, duration)
    }
}