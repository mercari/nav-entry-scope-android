package com.mercari.naventryscope

import dagger.hilt.DefineComponent
import dagger.hilt.android.components.ActivityRetainedComponent

/**
 * A Dagger component that has the lifetime of a Navigation Back Stack Entry.
 *
 * @see NavEntryScoped
 */
@NavEntryScoped
@DefineComponent(parent = ActivityRetainedComponent::class)
interface NavEntryComponent {

    companion object {
        const val NAV_ENTRY_SCOPE_ID = "_nav_entry_scope_id"
    }
}
