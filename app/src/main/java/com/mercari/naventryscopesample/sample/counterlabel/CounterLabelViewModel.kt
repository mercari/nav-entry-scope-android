package com.mercari.naventryscopesample.sample.counterlabel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel that displays the shared counter state.
 * This ViewModel receives NavEntryScoped dependencies through Hilt injection.
 */
interface CounterLabelViewModel {
    val state: StateFlow<CounterLabelState>
}

@HiltViewModel
class CounterLabelViewModelImpl @Inject constructor(
    getCounterFlowUseCase: GetCounterFlowUseCase,
) : CounterLabelViewModel, ViewModel() {

    override val state = MutableStateFlow(CounterLabelState())

    init {
        viewModelScope.launch {
            getCounterFlowUseCase()
                .collect { counter ->
                    state.update { it.copy(counter = counter) }
                }
        }
    }
}
