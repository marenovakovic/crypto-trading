package tech.mapps.swissborgtechchallenge

import java.math.BigDecimal

data class Ticker(
    val ticker: String,
    val currency: String,
    val price: String,
    val dailyVolume: String,
    val dailyHigh: String,
    val dailyLow: String,
    val change24h: Float,
    val change24hPercentage: Float,
)