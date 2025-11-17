package com.mercari.naventryscopesample.sample

import com.mercari.naventryscope.NavEntryComponent
import com.mercari.naventryscope.NavEntryScoped
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn

/**
 * Dagger module that provides NavEntryScoped dependencies.
 * This module is installed in the NavEntryComponent, meaning all bindings
 * will be scoped to the lifetime of a navigation entry.
 */
@Module
@InstallIn(NavEntryComponent::class)
interface SampleNavEntryComponentModule {

    @Binds
    @NavEntryScoped
    fun bindSharedCounterRepository(
        impl: SharedCounterRepositoryImpl,
    ): SharedCounterRepository
}
