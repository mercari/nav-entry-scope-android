package com.mercari.naventryscopesample.sample.counterlabel

import com.mercari.naventryscopesample.sample.SharedCounterRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

/**
 * Use case for getting the shared counter flow.
 * This use case operates on the NavEntryScoped repository.
 */
class GetCounterFlowUseCase @Inject constructor(
    private val sharedCounterRepository: SharedCounterRepository,
) {
    operator fun invoke(): Flow<Int> = sharedCounterRepository.counterFlow
}
