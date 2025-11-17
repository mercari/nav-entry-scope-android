package com.mercari.naventryscopesample.sample

import kotlin.test.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class SharedCounterRepositoryTest {

    private val repository = SharedCounterRepositoryImpl()

    @Test
    fun `setCounter emits value on counterFlow`() = runTest {
        val expected = 2

        repository.setCounter(expected)

        assertEquals(expected, repository.counterFlow.first())
    }

    @Test
    fun incrementCounter() = runTest {
        val initial = 5
        val expected = 6

        repository.setCounter(initial)
        repository.incrementCounter()

        assertEquals(expected, repository.counterFlow.first())
    }
}