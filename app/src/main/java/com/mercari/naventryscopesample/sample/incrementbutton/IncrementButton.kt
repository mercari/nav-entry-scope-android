package com.mercari.naventryscopesample.sample.incrementbutton

import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercari.naventryscope.navEntryScopedViewModel

@Composable
fun IncrementButton(
    modifier: Modifier = Modifier,
    viewModel: IncrementButtonViewModel = navEntryScopedViewModel<IncrementButtonViewModelImpl>(),
) {
    Button(
        modifier = modifier.height(48.dp),
        onClick = viewModel::onButtonClick,
    ) {
        Text("Increment Counter")
    }
}

@Composable
@Preview(showBackground = true)
fun IncrementButtonPreview() {
    MaterialTheme {
        IncrementButton(
            viewModel = object : IncrementButtonViewModel {
                override fun onButtonClick() = Unit
            },
        )
    }
}