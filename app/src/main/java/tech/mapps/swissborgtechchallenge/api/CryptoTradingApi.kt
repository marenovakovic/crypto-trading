package tech.mapps.swissborgtechchallenge.api

interface CryptoTradingApi {
    suspend fun getTickers(): List<TickerDto>
}