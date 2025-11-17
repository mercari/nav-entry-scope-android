package com.mercari.naventryscope

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NavEntryModule {

    @Binds
    @Singleton
    fun bindNavEntryComponentStore(impl: NavEntryComponentStoreImpl): NavEntryComponentStore
}
