package ru.wearemad.mad_core_compose.vm.vm_store_owner

import androidx.lifecycle.ViewModelStore

class DefaultComposeScreenViewModelStoreOwner : ComposeScreenViewModelStoreOwner {

    private val store = ViewModelStore()

    override fun getViewModelStore(): ViewModelStore = store
}