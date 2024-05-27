package tech.mapps.swissborgtechchallenge

import arrow.core.left
import arrow.core.right
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.test.runTest
import tech.mapps.swissborgtechchallenge.api.CryptoTradingApi
import tech.mapps.swissborgtechchallenge.api.Ticker
import kotlin.test.Test
import kotlin.test.assertEquals

data class GenericTicker(
    override val symbol: String = "",
    override val priceOfLastHighestBid: Float = 0f,
    override val sumOf25HighestBids: Float = 0f,
    override val lastLowestAsk: Float = 0f,
    override val sumOf25LowestAsks: Float = 0f,
    override val dailyChange: Float = 0f,
    override val dailyChangePercentage: Float = 0f,
    override val lastTradePrice: Float = 0f,
    override val dailyVolume: Float = 0f,
    override val dailyHigh: Float = 0f,
    override val dailyLow: Float = 0f,
) : Ticker

object FailingCryptoTradingApi : CryptoTradingApi {
    override suspend fun getTickers() = throw Throwable()
}

class FakeCryptoTradingApi : CryptoTradingApi {
    val tickers = List(3) { GenericTicker(symbol = it.toString()) }

    override suspend fun getTickers() = tickers
}

class GetTickersTest {

    @Test
    fun `return Left when api call fails`() = runTest {
        val getTickers = GetTickersImpl(FailingCryptoTradingApi)

        assertEquals(Unit.left(), getTickers())
    }

    @Test
    fun `return tickers from the api when call succeeds`() = runTest {
        val api = FakeCryptoTradingApi()
        val getTickers = GetTickersImpl(api)

        assertEquals(api.tickers.toImmutableList().right(), getTickers())
    }
}