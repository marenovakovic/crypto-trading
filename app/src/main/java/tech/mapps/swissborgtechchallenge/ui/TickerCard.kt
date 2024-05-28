package tech.mapps.swissborgtechchallenge.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.mapps.swissborgtechchallenge.Ticker
import tech.mapps.swissborgtechchallenge.ui.theme.SwissborgTechChallengeTheme

@Composable
fun TickerCard(
    ticker: Ticker,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        style = MaterialTheme.typography.titleLarge,
                        text = "${ticker.ticker}/${ticker.currency}",
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge.copy(
                            color =
                            if (ticker.change24hPercentage > 0) Color(0XFF12B32D)
                            else Color(0xFFCC3F29),
                        ),
                        text = "${ticker.change24hPercentage.sign()}${ticker.change24hPercentage}%",
                    )
                    Text(
                        style = MaterialTheme.typography.titleMedium,
                        text = ticker.price,
                    )
                }
            }
        }
    }
}

private fun Float.sign() = if (this > 0) "+" else ""

@Preview
@Composable
private fun TickerCardPositiveChangePreview() {
    val ticker = Ticker(
        ticker = "BTC",
        currency = "USD",
        price = "69863 USD",
        change24hPercentage = 2.10f,
    )
    SwissborgTechChallengeTheme {
        TickerCard(ticker = ticker)
    }
}

@Preview
@Composable
private fun TickerCardNegativeChangePreview() {
    val ticker = Ticker(
        ticker = "ETH",
        currency = "USD",
        price = "3981 USD",
        change24hPercentage = -1.80f,
    )
    SwissborgTechChallengeTheme {
        TickerCard(ticker = ticker)
    }
}