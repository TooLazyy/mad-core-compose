package ru.wearemad.mad_core_compose.message.holder

import android.util.Log
import ru.wearemad.mad_core_compose.message.controller.ComposeMessageController

class DefaultMessageControllerHolder : MessageControllerHolder {

    private var holder: ComposeMessageController? = null

    init {
        Log.d("MIINE", "DefaultMessageControllerHolder init")
    }

    override fun attachController(holder: ComposeMessageController) {
        Log.d("MIINE", "DefaultMessageControllerHolder attach")
        this.holder = holder
    }

    override fun detachController() {
        Log.d("MIINE", "DefaultMessageControllerHolder detach")
        holder?.cancelAll()
        holder = null
    }

    override fun getMessageController(): ComposeMessageController? = holder
}