package tech.mapps.swissborgtechchallenge

import java.math.RoundingMode
import tech.mapps.swissborgtechchallenge.api.TickerDto

fun TickerDto.toTicker() =
    Ticker(
        ticker = symbol.tickerSymbol,
        currency = currency,
        price = lastTradePrice.roundToTwoDecimalsWithCurrency(),
        change24hPercentage = dailyChangePercentage.percentage,
    )

private val String.tickerSymbol
    get() = trim('"').split(currency).first().drop(1).trimEnd(':')

private const val currency = "USD"

private fun Float.roundToTwoDecimals() =
    toBigDecimal().setScale(2, RoundingMode.HALF_DOWN)

private fun Float.roundToTwoDecimalsWithCurrency() =
    roundToTwoDecimals().toString() + " $currency"

private val Float.percentage
    get() = "%.2f".format(this * 100).toFloat()