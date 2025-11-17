package com.mercari.naventryscope

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavEntryComponentStoreImpl @Inject constructor() : NavEntryComponentStore {
    private val componentsMap = mutableMapOf<String, NavEntryComponent>()

    override fun storeComponent(navEntryScopeId: String, component: NavEntryComponent) {
        componentsMap[navEntryScopeId] = component
    }

    override fun getComponent(navEntryScopeId: String) = componentsMap[navEntryScopeId]
        ?: throw IllegalStateException("NavEntryComponent not found for navEntryScopeId: $navEntryScopeId")

    override fun releaseComponent(navEntryScopeId: String) {
        componentsMap.remove(navEntryScopeId)
    }
}
