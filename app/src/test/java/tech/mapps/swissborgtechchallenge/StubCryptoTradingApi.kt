package tech.mapps.swissborgtechchallenge

import tech.mapps.swissborgtechchallenge.api.CryptoTradingApi
import tech.mapps.swissborgtechchallenge.api.Ticker

@Suppress("TestFunctionName")
fun StubCryptoTradingApi(getTickers: () -> List<Ticker>) = object : CryptoTradingApi {
    override suspend fun getTickers() = getTickers()
}