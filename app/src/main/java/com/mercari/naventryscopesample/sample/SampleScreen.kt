package com.mercari.naventryscopesample.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mercari.naventryscopesample.sample.counterlabel.CounterLabel
import com.mercari.naventryscopesample.sample.incrementbutton.IncrementButton

/**
 * Sample screen demonstrating NavEntryScope usage.
 * This screen shows how two ViewModels can share state through NavEntryScoped dependencies.
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SampleScreen(
    modifier: Modifier = Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("NavEntryScope Sample") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CounterLabel(modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(24.dp))

            IncrementButton(modifier = Modifier.fillMaxWidth())
        }
    }
}
