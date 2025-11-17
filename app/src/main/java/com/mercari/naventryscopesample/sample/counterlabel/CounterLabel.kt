package com.mercari.naventryscopesample.sample.counterlabel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercari.naventryscope.navEntryScopedViewModel
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun CounterLabel(
    modifier: Modifier = Modifier,
    viewModel: CounterLabelViewModel = navEntryScopedViewModel<CounterLabelViewModelImpl>(),
) {
    val state by viewModel.state.collectAsState()

    Card(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "Counter Value",
                style = MaterialTheme.typography.labelSmall,
            )

            Text(
                text = "${state.counter}",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun CounterLabelPreview() {
    MaterialTheme {
        CounterLabel(
            viewModel = object : CounterLabelViewModel {
                override val state = MutableStateFlow(CounterLabelState(counter = 42))
            },
        )
    }
}
