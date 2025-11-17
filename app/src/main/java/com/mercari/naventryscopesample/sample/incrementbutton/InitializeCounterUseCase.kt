package com.mercari.naventryscopesample.sample.incrementbutton

import com.mercari.naventryscopesample.sample.SharedCounterRepository
import javax.inject.Inject

class InitializeCounterUseCase @Inject constructor(
    private val sharedCounterRepository: SharedCounterRepository,
) {

    suspend operator fun invoke(initialValue: Int) {
        sharedCounterRepository.setCounter(initialValue)
    }
}