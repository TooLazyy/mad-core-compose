package ru.wearemad.mad_core_compose.vm.vm_store_holder

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistry
import ru.wearemad.mad_core_compose.vm.vm_store_owner.ComposeScreenViewModelStoreOwner
import ru.wearemad.mad_core_compose.vm.vm_store_owner.DefaultComposeScreenViewModelStoreOwner

class ComposeScreenViewModelStoreHolderVm :
    ViewModel(),
    ComposeScreenViewModelStoreHolder {

    private val screenVmMap = hashMapOf<String, ComposeScreenViewModelStoreOwner>()

    override fun onCleared() {
        clearAll()
    }

    override fun attachToParentRegistry(parentRegistry: SavedStateRegistry) {
        screenVmMap.values
            .filter { it.lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED) }
            .forEach { it.attachToParentRegistry(parentRegistry) }
    }

    override fun getOrCreateScreenVmOwner(
        screenId: String
    ): ComposeScreenViewModelStoreOwner = screenVmMap.getOrPut(screenId) {
        DefaultComposeScreenViewModelStoreOwner(screenId)
    }

    override fun clearScreenVmOwner(screenId: String) {
        screenVmMap.remove(screenId)?.clear()
    }

    override fun clearForUnusedScreens(screensIds: Set<String>): Set<String> {
        val result = mutableSetOf<String>()
        screenVmMap.keys.toSet().forEach { key ->
            if (screensIds.contains(key).not()) {
                result.add(key)
                clearScreenVmOwner(key)
            }
        }
        return result.toSet()
    }

    override fun clearAll() {
        val owners = screenVmMap.values.toList()
        screenVmMap.clear()
        owners.forEach { it.clear() }
    }
}