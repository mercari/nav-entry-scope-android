package com.mercari.naventryscopesample.sample.incrementbutton

import com.mercari.naventryscope.NavEntryScoped
import com.mercari.naventryscopesample.sample.SampleModule
import com.mercari.naventryscopesample.sample.SharedCounterRepository
import javax.inject.Inject

/**
 * Use case for incrementing the shared counter.
 * The implementation is injected by [SampleModule]. The repository it uses is
 * [NavEntryScoped] and shared across ViewModels on the same navigation entry.
 */
interface IncrementCounterUseCase {
    suspend operator fun invoke()
}

class IncrementCounterUseCaseImpl @Inject constructor(
    private val sharedCounterRepository: SharedCounterRepository,
) : IncrementCounterUseCase {

    override suspend operator fun invoke() {
        sharedCounterRepository.incrementCounter()
    }
}
