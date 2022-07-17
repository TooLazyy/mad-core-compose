package ru.wearemad.mad_core_compose.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatchersProvider {

    fun main(): CoroutineDispatcher

    fun mainImmediate(): CoroutineDispatcher

    fun io(): CoroutineDispatcher

    fun default(): CoroutineDispatcher
}

class DefaultDispatchersProvider : DispatchersProvider {

    override fun main(): CoroutineDispatcher = Dispatchers.Main

    override fun mainImmediate(): CoroutineDispatcher = Dispatchers.Main.immediate

    override fun io(): CoroutineDispatcher = Dispatchers.IO

    override fun default(): CoroutineDispatcher = Dispatchers.Default
}