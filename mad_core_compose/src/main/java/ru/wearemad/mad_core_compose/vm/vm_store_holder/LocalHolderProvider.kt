package ru.wearemad.mad_core_compose.vm.vm_store_holder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.wearemad.mad_core_compose.utils.noLocalProvidedFor

val LocalComposeScreenViewModelStoreHolder =
    staticCompositionLocalOf<ComposeScreenViewModelStoreHolder> {
        noLocalProvidedFor("LocalComposeViewModelStoreHolder")
    }

@Composable
fun getComposeViewModelStoreHolder(): ComposeScreenViewModelStoreHolder =
    viewModel(modelClass = ComposeScreenViewModelStoreHolderVm::class.java)