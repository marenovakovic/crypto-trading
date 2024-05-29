package tech.mapps.swissborgtechchallenge.api

fun interface CryptoTradingApi {
    suspend fun getTickers(): List<TickerDto>
}