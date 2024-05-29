package tech.mapps.swissborgtechchallenge

import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class IntervalFlowTest {

    @Test
    fun `initial delay`() = runTest {
        val initialDelay = 500.milliseconds
        val tickerFlow = intervalFlow(interval = 1.seconds, initialDelay = initialDelay)

        var tickCount = 0

        val job = launch {
            tickerFlow.collect { tickCount++ }
        }

        assertEquals(0, tickCount)

        testScheduler.advanceTimeBy(initialDelay + 1.milliseconds)

        assertEquals(1, tickCount)

        job.cancel()
    }

    @Test
    fun period() = runTest {
        val period = 1.seconds
        val tickerFlow = intervalFlow(interval = period, initialDelay = Duration.ZERO)

        var count = 0

        val job = launch {
            tickerFlow.collect { count++ }
        }

        testScheduler.advanceTimeBy(1.milliseconds)

        assertEquals(1, count)

        val beforeTick = period - 100.milliseconds
        val delta = period - beforeTick
        testScheduler.advanceTimeBy(beforeTick)
        assertEquals(1, count)

        testScheduler.advanceTimeBy(delta)
        assertEquals(2, count)

        job.cancel()
    }
}