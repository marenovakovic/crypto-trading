@file:OptIn(ExperimentalMaterial3Api::class)

package tech.mapps.swissborgtechchallenge.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import tech.mapps.swissborgtechchallenge.ConnectivityStatus
import tech.mapps.swissborgtechchallenge.TradingState
import tech.mapps.swissborgtechchallenge.TradingViewModel

@Composable
fun TradingScreen(
    modifier: Modifier = Modifier,
    viewModel: TradingViewModel = viewModel(),
) {
    rememberSaveable { viewModel.init(); 1 }

    val state by viewModel.state.collectAsState()

    TradingScreenContent(
        tradingState = state,
        queryTickers = viewModel::search,
        sortByPrice = viewModel::sortByPrice,
        sortByName = viewModel::sortByName,
        modifier = modifier,
    )
}

@Composable
private fun TradingScreenContent(
    tradingState: TradingState,
    queryTickers: (String) -> Unit,
    sortByPrice: () -> Unit,
    sortByName: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        content = { innerPadding ->
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(vertical = 8.dp),
                ) {
                    SearchField(
                        query = tradingState.query,
                        onQueryChange = queryTickers,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Crossfade(
                        targetState = tradingState,
                        label = "TradingScreen cross-fade",
                    ) { targetState ->
                        if (targetState.isLoading)
                            LoadingIndicator()
                        else
                            TickersList(
                                tickers = tradingState.tickers,
                                sorting = tradingState.tickerSorting,
                                sortByPrice = sortByPrice,
                                sortByName = sortByName,
                            )
                    }
                }
                androidx.compose.animation.AnimatedVisibility(
                    visible = tradingState.connectivityStatus == ConnectivityStatus.Unavailable || tradingState.error != null,
                    enter = slideInVertically { it } + fadeIn(),
                    exit = slideOutVertically { it } + fadeOut(),
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .windowInsetsPadding(WindowInsets.navigationBars),
                ) {
                    ErrorWidget(tradingState = tradingState)
                }
            }
        },
    )
}