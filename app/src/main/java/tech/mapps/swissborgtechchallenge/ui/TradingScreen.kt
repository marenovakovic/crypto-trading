@file:OptIn(ExperimentalMaterial3Api::class)

package tech.mapps.swissborgtechchallenge.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import tech.mapps.swissborgtechchallenge.ConnectivityStatus
import tech.mapps.swissborgtechchallenge.R
import tech.mapps.swissborgtechchallenge.TickerSorting
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
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(vertical = 8.dp),
            ) {
                OutlinedTextField(
                    label = { Text(text = stringResource(R.string.ticker)) },
                    value = state.query,
                    onValueChange = viewModel::search,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .background(color = MaterialTheme.colorScheme.background),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 2.dp),
                ) {
                    Row(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { viewModel.sortByName() },
                        ),
                    ) {
                        Text(text = stringResource(R.string.name))
                        Spacer(modifier = Modifier.width(2.dp))
                        when (state.tickerSorting) {
                            TickerSorting.ByNameAscending -> Icon(
                                imageVector = Icons.Default.ArrowUpward,
                                contentDescription = null,
                            )
                            TickerSorting.ByNameDescending -> Icon(
                                imageVector = Icons.Default.ArrowDownward,
                                contentDescription = null,
                            )
                            else -> Unit
                        }
                    }
                    Row(
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { viewModel.sortByPrice() },
                        ),
                    ) {
                        Text(text = stringResource(R.string.price))
                        Spacer(modifier = Modifier.width(2.dp))
                        when (state.tickerSorting) {
                            TickerSorting.ByPriceAscending -> Icon(
                                imageVector = Icons.Default.ArrowUpward,
                                contentDescription = null,
                            )
                            TickerSorting.ByPriceDescending -> Icon(
                                imageVector = Icons.Default.ArrowDownward,
                                contentDescription = null,
                            )
                            else -> Unit
                        }
                    }
                }
                Box(modifier = Modifier.fillMaxSize()) {
                    Crossfade(
                        targetState = state,
                        label = "TradingScreen cross-fade",
                    ) { targetState ->
                        if (targetState.isLoading)
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                CircularProgressIndicator()
                            }
                        else
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 8.dp),
                            ) {
                                items(targetState.tickers) { ticker ->
                                    TickerCard(
                                        ticker = ticker,
                                        modifier = Modifier.padding(bottom = 8.dp),
                                    )
                                }
                            }
                    }
                    androidx.compose.animation.AnimatedVisibility(
                        visible = state.connectivityStatus == ConnectivityStatus.Unavailable || state.error != null,
                        enter = slideInVertically { it } + fadeIn(),
                        exit = slideOutVertically { it } + fadeOut(),
                        modifier = Modifier.align(Alignment.BottomCenter),
                    ) {
                        ErrorWidget(tradingState = state)
                    }
                }
            }
        },
    )
}