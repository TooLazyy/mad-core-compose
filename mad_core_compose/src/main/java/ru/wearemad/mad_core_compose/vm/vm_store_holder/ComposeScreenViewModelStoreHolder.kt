package ru.wearemad.mad_core_compose.vm.vm_store_holder

import ru.wearemad.mad_core_compose.vm.vm_store_owner.ComposeScreenViewModelStoreOwner

interface ComposeScreenViewModelStoreHolder {

    fun getOrCreateScreenVmOwner(
        screenId: String
    ): ComposeScreenViewModelStoreOwner

    fun clearScreenVmOwner(screenId: String)

    /**
     * returns a set of closed vms
     */
    fun clearForUnusedScreens(screensIds: Set<String>): Set<String>

    fun clearAll()
}