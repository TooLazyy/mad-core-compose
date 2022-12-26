package ru.wearemad.mad_core_compose.vm.vm_store_holder

import ru.wearemad.mad_core_compose.vm.vm_store_owner.ComposeScreenViewModelStoreOwner

interface ComposeScreenViewModelStoreHolder {

    fun getOrCreateScreenVmOwner(
        screenId: String
    ): ComposeScreenViewModelStoreOwner

    fun clearScreenVmOwner(screenId: String)

    fun clearAll()
}