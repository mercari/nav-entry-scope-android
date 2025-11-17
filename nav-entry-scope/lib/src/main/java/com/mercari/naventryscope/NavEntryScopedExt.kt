package com.mercari.naventryscope

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.HiltViewModelFactory
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.DEFAULT_ARGS_KEY
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mercari.naventryscope.NavEntryComponent.Companion.NAV_ENTRY_SCOPE_ID

/**
 * Allows to inject `NavEntryScoped` dependencies into a ViewModel. The ViewModel will be scoped to the current
 * navigation graph entry.
 *
 * @see hiltViewModel
 */
@Composable
inline fun <reified VM : ViewModel> navEntryScopedViewModel(
    key: String? = null,
    viewModelStoreOwner: ViewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    },
): VM = viewModel(
    modelClass = VM::class.java,
    viewModelStoreOwner = viewModelStoreOwner,
    key = key,
    factory = createHiltViewModelFactory(viewModelStoreOwner),
    extras = if (viewModelStoreOwner is HasDefaultViewModelProviderFactory) {
        viewModelStoreOwner.defaultViewModelCreationExtras
    } else {
        CreationExtras.Empty
    }.let { extras ->
        val initialArgs = extras[DEFAULT_ARGS_KEY] ?: Bundle()
        val navEntryScopeId = hiltViewModel<NavEntryComponentOwner>(viewModelStoreOwner).getNavEntryScopeId()
        MutableCreationExtras(extras).apply {
            set(DEFAULT_ARGS_KEY, Bundle(initialArgs).apply { putString(NAV_ENTRY_SCOPE_ID, navEntryScopeId) })
        }
    }
)

@Composable
fun createHiltViewModelFactory(
    viewModelStoreOwner: ViewModelStoreOwner,
): ViewModelProvider.Factory? = if (viewModelStoreOwner is HasDefaultViewModelProviderFactory) {
    HiltViewModelFactory(
        context = LocalContext.current,
        delegateFactory = viewModelStoreOwner.defaultViewModelProviderFactory
    )
} else {
    // Use the default factory provided by the ViewModelStoreOwner
    // and assume it is an @AndroidEntryPoint annotated fragment or activity
    null
}
