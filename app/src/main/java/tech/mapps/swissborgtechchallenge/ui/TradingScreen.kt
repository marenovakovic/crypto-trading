@file:OptIn(ExperimentalMaterial3Api::class)

package tech.mapps.swissborgtechchallenge.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import tech.mapps.swissborgtechchallenge.TradingViewModel

@Composable
fun TradingScreen(
    modifier: Modifier = Modifier,
    viewModel: TradingViewModel = viewModel(),
) {
    rememberSaveable { viewModel.init(); 1 }

    val state by viewModel.state.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column {
                        OutlinedTextField(
                            value = state.query,
                            onValueChange = viewModel::search,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                },
            )
        },
        content = { innerPadding ->
            LazyColumn(
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.padding(innerPadding),
            ) {
                items(state.tickers) { ticker ->
                    TickerCard(
                        ticker = ticker,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                }
            }
        },
    )
}