package ru.wearemad.mad_core_compose.message.holder

import ru.wearemad.mad_core_compose.message.controller.ComposeMessageController

class DefaultMessageControllerHolder : MessageControllerHolder {

    private var holder: ComposeMessageController? = null

    override fun attachController(holder: ComposeMessageController) {
        this.holder = holder
    }

    override fun detachController() {
        holder?.cancelAll()
        holder = null
    }

    override fun getMessageController(): ComposeMessageController? = holder
}