package ru.wearemad.mad_core_compose.vm.vm_store_owner

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.enableSavedStateHandles
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryController
import java.lang.ref.WeakReference

class DefaultComposeScreenViewModelStoreOwner(
    private val screenId: String,
) : ComposeScreenViewModelStoreOwner {

    private val store = ViewModelStore()
    private val lifecycleRegistry = LifecycleRegistry(this)
    private val savedStateRegistryController = SavedStateRegistryController.create(this)
    private val closeables = linkedMapOf<String, AutoCloseable>()
    private var parentRegistryRef: WeakReference<SavedStateRegistry>? = null
    private var isCleared = false

    override val viewModelStore: ViewModelStore = store

    override val lifecycle: Lifecycle
        get() = lifecycleRegistry

    override val savedStateRegistry: SavedStateRegistry
        get() = savedStateRegistryController.savedStateRegistry

    override fun attachToParentRegistry(parentRegistry: SavedStateRegistry) {
        if (isCleared) {
            return
        }
        val restoredState = parentRegistry.consumeRestoredStateForKey(screenId)
        if (lifecycleRegistry.currentState == Lifecycle.State.INITIALIZED) {
            restoreState(restoredState)
        }
        detachFromParentRegistry()
        parentRegistry.unregisterSavedStateProvider(screenId)
        parentRegistry.registerSavedStateProvider(screenId, ::saveState)
        parentRegistryRef = WeakReference(parentRegistry)
    }

    override fun addCloseable(
        key: String,
        closeable: AutoCloseable,
    ) {
        if (isCleared) {
            closeable.close()
            return
        }
        closeables.putIfAbsent(key, closeable)
    }

    override fun clear() {
        isCleared = true
        try {
            store.clear()
        } finally {
            detachFromParentRegistry()
            if (lifecycleRegistry.currentState.isAtLeast(Lifecycle.State.CREATED)) {
                lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
            }
            closeAll()
        }
    }

    private fun restoreState(restoredState: Bundle?) {
        savedStateRegistryController.performAttach()
        savedStateRegistryController.performRestore(restoredState)
        enableSavedStateHandles()
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
    }

    private fun detachFromParentRegistry() {
        parentRegistryRef?.get()?.unregisterSavedStateProvider(screenId)
        parentRegistryRef = null
    }

    private fun saveState(): Bundle = Bundle().also(savedStateRegistryController::performSave)

    private fun closeAll() {
        val toClose = closeables.values.toList()
        closeables.clear()
        val errors = toClose.mapNotNull { runCatching { it.close() }.exceptionOrNull() }
        val first = errors.firstOrNull() ?: return
        errors.drop(1).forEach(first::addSuppressed)
        throw first
    }
}