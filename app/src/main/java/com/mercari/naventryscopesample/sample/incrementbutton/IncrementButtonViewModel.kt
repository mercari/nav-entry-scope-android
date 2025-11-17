package com.mercari.naventryscopesample.sample.incrementbutton

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

/**
 * ViewModel that handles button interactions.
 * This ViewModel receives NavEntryScoped dependencies through Hilt injection.
 */
interface IncrementButtonViewModel {
    fun onButtonClick()
}

@HiltViewModel
class IncrementButtonViewModelImpl @Inject constructor(
    private val initializeCounterUseCase: InitializeCounterUseCase,
    private val incrementCounterUseCase: IncrementCounterUseCase,
) : IncrementButtonViewModel, ViewModel() {

    init {
        viewModelScope.launch {
            initializeCounterUseCase(INITIAL_COUNTER_VALUE)
        }
    }

    override fun onButtonClick() {
        viewModelScope.launch {
            incrementCounterUseCase()
        }
    }

    private companion object {
        const val INITIAL_COUNTER_VALUE = 0
    }
}
