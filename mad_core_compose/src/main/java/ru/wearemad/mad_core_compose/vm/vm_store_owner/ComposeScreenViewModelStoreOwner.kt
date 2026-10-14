package ru.wearemad.mad_core_compose.vm.vm_store_owner

import androidx.lifecycle.ViewModelStoreOwner
import androidx.savedstate.SavedStateRegistry
import androidx.savedstate.SavedStateRegistryOwner

interface ComposeScreenViewModelStoreOwner : ViewModelStoreOwner, SavedStateRegistryOwner {

    fun attachToParentRegistry(parentRegistry: SavedStateRegistry)

    fun addCloseable(
        key: String,
        closeable: AutoCloseable,
    )

    fun clear()
}