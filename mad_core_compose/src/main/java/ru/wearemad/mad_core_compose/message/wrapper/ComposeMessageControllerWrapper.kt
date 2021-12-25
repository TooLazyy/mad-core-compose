package ru.wearemad.mad_core_compose.message.wrapper

import ru.wearemad.mad_core_compose.message.controller.ComposeMessageController
import ru.wearemad.mad_core_compose.message.holder.MessageControllerHolder

interface ComposeMessageControllerWrapper : ComposeMessageController {

    val holder: MessageControllerHolder
}