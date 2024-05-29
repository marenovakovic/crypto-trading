package tech.mapps.swissborgtechchallenge

import tech.mapps.swissborgtechchallenge.api.TickerDto
import kotlin.random.Random

val randomTickers: List<Ticker>
    get() = List(Random.nextInt(1, 5)) {
        Ticker(
            ticker = it.toString(),
            price = "0 USD",
            change24hPercentage = 0f,
        )
    }

val randomTickerDtos: List<TickerDto>
    get() = List(Random.nextInt(1, 5)) {
        GenericTickerDto(symbol = it.toString())
    }

data class GenericTickerDto(
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
) : TickerDto