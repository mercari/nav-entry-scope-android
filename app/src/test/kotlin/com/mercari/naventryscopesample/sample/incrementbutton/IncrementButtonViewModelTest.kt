package com.mercari.naventryscopesample.sample.incrementbutton

import com.mercari.naventryscopesample.sample.FakeSharedCounterRepository
import com.mercari.naventryscopesample.sample.MainDispatcherExtension
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MainDispatcherExtension::class)
class IncrementButtonViewModelTest {

    private val sharedCounterRepository = FakeSharedCounterRepository()

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        IncrementButtonViewModelImpl(
            initializeCounterUseCase = InitializeCounterUseCase(sharedCounterRepository),
            incrementCounterUseCase = IncrementCounterUseCaseImpl(sharedCounterRepository),
        )
    }

    @Test
    fun `init sets the initial counter value`() = runTest {
        val recordedCounterValues = mutableListOf<Int>()
        val job = launch(UnconfinedTestDispatcher()) {
            sharedCounterRepository.counterFlow.toList(recordedCounterValues)
        }

        viewModel.run { /* init */ }

        job.cancel()

        assertEquals(0, recordedCounterValues.last())
    }

    @Test
    fun `onButtonClick increments the counter value`() = runTest {
        val recordedCounterValues = mutableListOf<Int>()
        val job = launch(UnconfinedTestDispatcher()) {
            sharedCounterRepository.counterFlow.toList(recordedCounterValues)
        }

        viewModel.run {
            onButtonClick() // 1
            onButtonClick() // 2
            onButtonClick() // 3
        }

        job.cancel()

        assertEquals(3, recordedCounterValues.last())
    }
}