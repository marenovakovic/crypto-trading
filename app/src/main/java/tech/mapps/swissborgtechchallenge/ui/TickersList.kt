package tech.mapps.swissborgtechchallenge.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import tech.mapps.swissborgtechchallenge.R
import tech.mapps.swissborgtechchallenge.Ticker
import tech.mapps.swissborgtechchallenge.TickerSorting
import tech.mapps.swissborgtechchallenge.ui.theme.SwissborgTechChallengeTheme

@Composable
fun TickersList(
    tickers: ImmutableList<Ticker>,
    sorting: TickerSorting,
    sortByPrice: () -> Unit,
    sortByName: () -> Unit,
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 2.dp),
        ) {
            RowLabel(
                text = stringResource(R.string.name),
                imageVector = when (sorting) {
                    TickerSorting.ByNameAscending -> Icons.Default.ArrowUpward
                    TickerSorting.ByNameDescending -> Icons.Default.ArrowDownward
                    else -> null
                },
                onClick = sortByName,
            )
            RowLabel(
                text = stringResource(R.string.price),
                imageVector = when (sorting) {
                    TickerSorting.ByPriceAscending -> Icons.Default.ArrowUpward
                    TickerSorting.ByPriceDescending -> Icons.Default.ArrowDownward
                    else -> null
                },
                onClick = sortByPrice,
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
        ) {
            items(tickers) { ticker ->
                TickerCard(
                    ticker = ticker,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
            }
        }
    }
}

@Preview
@Composable
private fun TickersListPreview() {
    SwissborgTechChallengeTheme {
        TickersList(
            tickers = persistentListOf(Ticker("BTC", "68491 USD", 1f)),
            sorting = TickerSorting.ByPriceDescending,
            sortByPrice = {},
            sortByName = {},
        )
    }
}