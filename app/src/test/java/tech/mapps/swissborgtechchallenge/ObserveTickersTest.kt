package tech.mapps.swissborgtechchallenge

import app.cash.turbine.test
import arrow.atomic.AtomicBoolean
import arrow.core.left
import arrow.core.right
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class ObserveTickersTest {

    private fun TestScope.startEmitting() {
        testScheduler.advanceTimeBy(1.milliseconds)
    }

    @Test
    fun `emit error(Unit) when getting tickers fails`() = runTest {
        val observeTickers = ObserveTickersImpl(FailingCryptoTradingApi)

        observeTickers().test {
            assertEquals(Unit.left(), awaitItem())
        }
    }

    @Test
    fun `emit tickers`() = runTest {
        val api = FakeCryptoTradingApi()
        val observeTickers = ObserveTickersImpl(api)

        startEmitting()

        observeTickers().test {
            val expectedTickers =
                api.tickerDtos.map { it.toTicker() }.toImmutableList()
            assertEquals(expectedTickers.right(), awaitItem())
        }
    }

    @Test
    fun `emit tickers on a given period`() = runTest {
        val period = 1.seconds
        val api = FakeCryptoTradingApi()
        val observeTickers = ObserveTickersImpl(api, period)

        var count = 0

        val job = launch {
            observeTickers().collect { count++ }
        }

        startEmitting()
        assertEquals(1, count)

        testScheduler.advanceTimeBy(period)
        assertEquals(2, count)

        testScheduler.advanceTimeBy(period)
        assertEquals(3, count)

        testScheduler.advanceTimeBy(period * 3)
        assertEquals(6, count)

        job.cancel()
    }

    @Test
    fun `emit tickers and then error`() = runTest {
        val period = 1.seconds
        val shouldFail = AtomicBoolean(false)
        val tickerDtos = randomTickerDtos
        val api = StubCryptoTradingApi {
            if (shouldFail.getAndSet(true)) throw Throwable()
            else tickerDtos
        }
        val observeTickers = ObserveTickersImpl(api, period)

        startEmitting()
        testScheduler.advanceTimeBy(period)

        observeTickers().test {
            val expectedTickers = tickerDtos.map { it.toTicker() }.toImmutableList()
            assertEquals(expectedTickers.right(), awaitItem())
            assertEquals(Unit.left(), awaitItem())
        }
    }
}