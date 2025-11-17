package com.mercari.naventryscopesample.sample

import com.mercari.naventryscope.NavEntryComponent
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * Repository that holds shared state between ViewModels on the same navigation entry.
 * This repository will be scoped to the [NavEntryComponent] lifecycle.
 */
interface SharedCounterRepository {

    val counterFlow: Flow<Int>

    suspend fun setCounter(value: Int)
    suspend fun incrementCounter()
}

class SharedCounterRepositoryImpl @Inject constructor() : SharedCounterRepository {

    private var counter = -1

    override val counterFlow = MutableSharedFlow<Int>(replay = 1)

    override suspend fun setCounter(value: Int) {
        counter = value
        counterFlow.emit(value)
    }

    override suspend fun incrementCounter() {
        counterFlow.emit(++counter)
    }
}
