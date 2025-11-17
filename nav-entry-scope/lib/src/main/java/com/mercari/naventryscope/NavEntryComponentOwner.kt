package com.mercari.naventryscope

import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class NavEntryComponentOwner @Inject constructor(
    componentBuilder: NavEntryComponentBuilder,
    private val componentStore: NavEntryComponentStore,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val navEntryScopeId = savedStateHandle.get<Bundle>(SAVED_BUNDLE_KEY)
        ?.getString(NAV_ENTRY_SCOPE_ID_KEY)
        ?: UUID.randomUUID().toString()

    init {
        savedStateHandle.setSavedStateProvider(
            key = SAVED_BUNDLE_KEY,
            provider = { Bundle().apply { putString(NAV_ENTRY_SCOPE_ID_KEY, navEntryScopeId) } },
        )

        val component = componentBuilder.build()
        componentStore.storeComponent(navEntryScopeId, component)
    }

    fun getNavEntryScopeId(): String = navEntryScopeId

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    public override fun onCleared() {
        componentStore.releaseComponent(navEntryScopeId)
    }

    @VisibleForTesting
    companion object {
        internal const val SAVED_BUNDLE_KEY = "NavEntryComponentOwner.State"
        internal const val NAV_ENTRY_SCOPE_ID_KEY = "NavEntryScopeId"
    }
}
