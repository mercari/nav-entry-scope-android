package com.mercari.naventryscopesample.sample

import com.mercari.naventryscopesample.sample.incrementbutton.IncrementCounterUseCase
import com.mercari.naventryscopesample.sample.incrementbutton.IncrementCounterUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class SampleModule {

    @Binds
    abstract fun bindIncrementCounterUseCase(
        impl: IncrementCounterUseCaseImpl,
    ): IncrementCounterUseCase
}
