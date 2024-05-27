package tech.mapps.swissborgtechchallenge

import tech.mapps.swissborgtechchallenge.api.Ticker
import kotlin.random.Random

val randomTickers: List<Ticker>
    get() = List(Random.nextInt(1, 5)) {
        GenericTicker(symbol = it.toString())
    }

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