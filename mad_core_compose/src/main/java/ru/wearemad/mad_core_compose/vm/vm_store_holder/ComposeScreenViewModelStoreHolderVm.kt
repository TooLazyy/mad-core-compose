package ru.wearemad.mad_core_compose.vm.vm_store_holder

import android.util.Log
import androidx.lifecycle.ViewModel
import ru.wearemad.mad_core_compose.vm.vm_store_owner.ComposeScreenViewModelStoreOwner
import ru.wearemad.mad_core_compose.vm.vm_store_owner.DefaultComposeScreenViewModelStoreOwner

class ComposeScreenViewModelStoreHolderVm :
    ViewModel(),
    ComposeScreenViewModelStoreHolder {

    private val screenVmMap = hashMapOf<String, ComposeScreenViewModelStoreOwner>()

    init {
        Log.d("MIINE", "ComposeScreenViewModelStoreHolderVm")
    }

    override fun getOrCreateScreenVmOwner(
        screenId: String
    ): ComposeScreenViewModelStoreOwner = screenVmMap.getOrPut(screenId) {
        DefaultComposeScreenViewModelStoreOwner()
    }

    override fun clearScreenVmOwner(screenId: String) {
        screenVmMap.remove(screenId)?.viewModelStore?.clear()
    }

    override fun clearAll() {
        screenVmMap.forEach {
            it.value.viewModelStore.clear()
        }
        screenVmMap.clear()
    }
}