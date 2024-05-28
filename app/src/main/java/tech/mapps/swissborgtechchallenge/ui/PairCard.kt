package tech.mapps.swissborgtechchallenge.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import tech.mapps.swissborgtechchallenge.Ticker
import tech.mapps.swissborgtechchallenge.ui.theme.SwissborgTechChallengeTheme

@Composable
fun PairCard(
    ticker: Ticker,
    modifier: Modifier = Modifier,
) {
    Surface(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth(fraction = 0.4f),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = ticker.ticker)
                    Text(
                        style = MaterialTheme.typography.bodyLarge,
                        text = "/${ticker.currency}",
                        modifier = Modifier.alpha(0.56f),
                    )
                }
                Text(
                    style = MaterialTheme.typography.bodyLarge,
                    text = ticker.dailyVolume,
                    modifier = Modifier.alpha(0.56f),
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.fillMaxWidth(fraction = 0.5f),
            ) {
                Text(
                    style = MaterialTheme.typography.titleLarge,
                    text = ticker.dailyHigh,
                )
                Text(
                    style = MaterialTheme.typography.titleMedium.copy(),
                    text = ticker.dailyLow,
                    modifier = Modifier.alpha(0.56f),
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Surface(
                color = if (ticker.change24hPercentage > 0) Color.Green else Color.Red,
                shape = CircleShape,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    text = ticker.change24hPercentage.toString(),
                )
            }
        }
    }
}

@Preview
@Composable
private fun PairCardPreview() {
    val ticker = Ticker(
        ticker = "BTC",
        currency = "USD",
        price = "",
        dailyVolume = "132000",
        dailyHigh = "70000",
        dailyLow = "69000",
        change24h = 0f,
        change24hPercentage = 2.1f,
    )
    SwissborgTechChallengeTheme {
        PairCard(ticker = ticker)
    }
}