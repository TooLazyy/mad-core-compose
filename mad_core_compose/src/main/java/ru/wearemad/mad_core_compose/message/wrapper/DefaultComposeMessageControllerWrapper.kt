package ru.wearemad.mad_core_compose.message.wrapper

import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarResult
import ru.wearemad.mad_core_compose.message.holder.MessageControllerHolder

class DefaultComposeMessageControllerWrapper(
    override val holder: MessageControllerHolder
) : ComposeMessageControllerWrapper {

    override fun cancelAll() {
        holder.getMessageController()?.cancelAll()
    }

    override fun showSnack(
        text: String,
        actionText: String?,
        duration: SnackbarDuration,
        listener: (result: SnackbarResult) -> Unit
    ) {
        holder.getMessageController()?.showSnack(text, actionText, duration, listener)
    }

    override fun showToast(text: String, duration: Int) {
        holder.getMessageController()?.showToast(text, duration)
    }
}