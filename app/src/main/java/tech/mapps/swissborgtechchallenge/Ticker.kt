package tech.mapps.swissborgtechchallenge

data class Ticker(
    val ticker: String,
    val price: String,
    val change24hPercentage: Float,
)