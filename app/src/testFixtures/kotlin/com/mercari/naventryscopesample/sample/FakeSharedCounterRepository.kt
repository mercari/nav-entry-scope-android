package com.mercari.naventryscopesample.sample

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class FakeSharedCounterRepository : SharedCounterRepository {
    override val counterFlow = MutableStateFlow(-1)

    override suspend fun setCounter(value: Int) {
        counterFlow.update { value }
    }

    override suspend fun incrementCounter() {
        counterFlow.update { it + 1 }
    }
}
