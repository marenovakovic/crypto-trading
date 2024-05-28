package tech.mapps.swissborgtechchallenge.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import tech.mapps.swissborgtechchallenge.ConnectivityStatus
import tech.mapps.swissborgtechchallenge.R
import tech.mapps.swissborgtechchallenge.TradingState

@Composable
fun ErrorWidget(
    tradingState: TradingState,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.error),
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides LocalTextStyle.current.copy(color = Color.White)
        ) {
            when {
                tradingState.connectivityStatus == ConnectivityStatus.Unavailable ->
                    Text(text = stringResource(id = R.string.internet_connection_is_unavailable))
                tradingState.error != null ->
                    Text(text = stringResource(R.string.error_occurred))
            }
            if (tradingState.tickers.isNotEmpty())
                Text(text = stringResource(R.string.you_are_seeing_old_data))
        }
    }
}