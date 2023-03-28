package ru.wearemad.mad_core_compose.message.controller

import android.content.Context
import android.widget.Toast
import ru.wearemad.mad_core_compose.message.data.AppShackData
import ru.wearemad.mad_core_compose.message.data.AppSnackActionResult

class DefaultComposeMessageController(
    private val context: Context,
    private val onSnackBarEvent: (ComposeMessageController.AppSnackBarEvent) -> Unit
) : ComposeMessageController {

    private var toast: Toast? = null

    override fun showSnack(
        data: AppShackData,
        listener: (result: AppSnackActionResult) -> Unit
    ) {
        cancelSnackbar()
        onSnackBarEvent(
            ComposeMessageController.AppSnackBarEvent.Show(
                data,
                listener
            )
        )
    }

    override fun showToast(text: String, duration: Int) {
        cancelToast()
        toast = Toast.makeText(
            context,
            text,
            duration
        )?.also {
            it.show()
        }
    }

    override fun cancelAll() {
        clear()
    }

    private fun clear() {
        cancelToast()
        cancelSnackbar()
    }

    private fun cancelToast() {
        toast?.cancel()
        toast = null
    }

    private fun cancelSnackbar() {
        onSnackBarEvent(ComposeMessageController.AppSnackBarEvent.Hide)
    }
}