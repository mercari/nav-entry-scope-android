package com.mercari.naventryscopesample.sample.counterlabel

import com.mercari.naventryscopesample.sample.FakeSharedCounterRepository
import com.mercari.naventryscopesample.sample.MainDispatcherExtension
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainDispatcherExtension::class)
class CounterLabelViewModelTest {

    private val sharedCounterRepository = FakeSharedCounterRepository()

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        CounterLabelViewModelImpl(
            getCounterFlowUseCase = GetCounterFlowUseCase(sharedCounterRepository),
        )
    }

    @Test
    fun `init updates the counter value by observing the counter repository`() = runTest {
        sharedCounterRepository.setCounter(5)
        assertEquals(5, viewModel.state.value.counter)

        sharedCounterRepository.setCounter(10)
        assertEquals(10, viewModel.state.value.counter)
    }
}