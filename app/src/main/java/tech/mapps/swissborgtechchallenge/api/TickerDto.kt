package tech.mapps.swissborgtechchallenge.api

interface TickerDto {
    val symbol: String
    val priceOfLastHighestBid: Float
    val sumOf25HighestBids: Float
    val lastLowestAsk: Float
    val sumOf25LowestAsks: Float
    val dailyChange: Float
    val dailyChangePercentage: Float
    val lastTradePrice: Float
    val dailyVolume: Float
    val dailyHigh: Float
    val dailyLow: Float
}