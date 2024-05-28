package tech.mapps.swissborgtechchallenge

import kotlin.test.Test
import kotlin.test.assertEquals

class TickerMapperTest {

    @Test
    fun `remove t and split ticker and currency`() {
        val tickerOne = GenericTickerDto("tBTCUSD").toTicker()

        assertEquals("BTC", tickerOne.ticker)

        val tickerTwo = GenericTickerDto("\"tBTC:USD\"").toTicker()

        assertEquals("BTC", tickerTwo.ticker)
    }

    @Test
    fun `price in currency`() {
        assertEquals(
            "1000.00 USD",
            GenericTickerDto(lastTradePrice = 1000f).toTicker().price,
        )
        assertEquals(
            "1000.00 USD",
            GenericTickerDto(lastTradePrice = 1000f).toTicker().price,
        )
        assertEquals(
            "1000.10 USD",
            GenericTickerDto(lastTradePrice = 1000.1f).toTicker().price,
        )
        assertEquals(
            "1000.11 USD",
            GenericTickerDto(lastTradePrice = 1000.1111f).toTicker().price,
        )
        assertEquals(
            "1000.12 USD",
            GenericTickerDto(lastTradePrice = 1000.1199f).toTicker().price,
        )
    }

    @Test
    fun `daily change percentage`() {
        assertEquals(
            -0.03f,
            GenericTickerDto(dailyChangePercentage = -0.00027681f).toTicker().change24hPercentage,
        )
        assertEquals(
            -1.01f,
            GenericTickerDto(dailyChangePercentage = -0.01008861f).toTicker().change24hPercentage,
        )
        assertEquals(
            2.33f,
            GenericTickerDto(dailyChangePercentage = 0.023333f).toTicker().change24hPercentage,
        )
        assertEquals(
            1.00f,
            GenericTickerDto(dailyChangePercentage = 0.0100000f).toTicker().change24hPercentage,
        )
        assertEquals(
            0.39f,
            GenericTickerDto(dailyChangePercentage = 0.00391999f).toTicker().change24hPercentage,
        )
    }
}