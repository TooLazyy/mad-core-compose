package ru.wearemad.mad_core_compose.message.holder

import ru.wearemad.mad_core_compose.message.controller.ComposeMessageController

interface MessageControllerHolder {

    fun attachController(holder: ComposeMessageController)

    fun detachController()

    fun getMessageController(): ComposeMessageController?
}