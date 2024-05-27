package tech.mapps.swissborgtechchallenge

import tech.mapps.swissborgtechchallenge.api.CryptoTradingApi
import tech.mapps.swissborgtechchallenge.api.TickerDto

@Suppress("TestFunctionName")
fun StubCryptoTradingApi(getTickers: () -> List<TickerDto>) = object : CryptoTradingApi {
    override suspend fun getTickers() = getTickers()
}