package tech.mapps.swissborgtechchallenge

data class Ticker(
    val ticker: String,
    val currency: String,
    val price: String,
    val dailyVolume: String,
    val dailyHigh: String,
    val dailyLow: String,
    val change24h: String,
    val change24hPercentage: Float,
)