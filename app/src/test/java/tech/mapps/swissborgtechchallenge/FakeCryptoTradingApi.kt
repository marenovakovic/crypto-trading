package tech.mapps.swissborgtechchallenge

import tech.mapps.swissborgtechchallenge.api.CryptoTradingApi
import tech.mapps.swissborgtechchallenge.api.Ticker

object FailingCryptoTradingApi : CryptoTradingApi {
    override suspend fun getTickers() = throw Throwable()
}

class FakeCryptoTradingApi : CryptoTradingApi {
    val tickers = List(3) { GenericTicker(symbol = it.toString()) }

    override suspend fun getTickers() = tickers
}