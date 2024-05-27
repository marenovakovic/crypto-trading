package tech.mapps.swissborgtechchallenge

import tech.mapps.swissborgtechchallenge.api.TickerDto

fun TickerDto.toTicker() =
    Ticker(
        ticker = symbol,
        currency = symbol,
        price = lastTradePrice.toString(),
        dailyVolume = dailyVolume.toString(),
        dailyHigh = dailyHigh.toString(),
        dailyLow = dailyLow.toString(),
        change24h = dailyChange.toString(),
        change24hPercentage = (dailyChangePercentage * 100).toString(),
    )