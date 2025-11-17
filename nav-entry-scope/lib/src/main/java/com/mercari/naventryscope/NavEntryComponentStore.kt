package com.mercari.naventryscope

interface NavEntryComponentStore {

    fun getComponent(navEntryScopeId: String): NavEntryComponent

    fun storeComponent(
        navEntryScopeId: String,
        component: NavEntryComponent,
    )

    fun releaseComponent(navEntryScopeId: String)
}
