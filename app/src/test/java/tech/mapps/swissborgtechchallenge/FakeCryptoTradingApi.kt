package tech.mapps.swissborgtechchallenge

import tech.mapps.swissborgtechchallenge.api.CryptoTradingApi
import tech.mapps.swissborgtechchallenge.api.Ticker

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