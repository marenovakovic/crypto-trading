package tech.mapps.swissborgtechchallenge

import kotlinx.collections.immutable.persistentListOf
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TradingStateTest {

    @Test
    fun isLoading() {
        assertTrue(TradingState().isLoading)
        assertTrue(TradingState(connectivityStatus = ConnectivityStatus.Available).isLoading)

        assertFalse(TradingState(connectivityStatus = ConnectivityStatus.Unavailable).isLoading)
        assertFalse(TradingState(query = "non empty").isLoading)
        assertFalse(TradingState(error = Unit).isLoading)
        assertFalse(TradingState(allTickers = persistentListOf(Ticker("", "", 1f))).isLoading)
        assertFalse(
            TradingState(
                allTickers = persistentListOf(Ticker("", "", 1f)),
                query = "non empty",
            ).isLoading,
        )
    }
}