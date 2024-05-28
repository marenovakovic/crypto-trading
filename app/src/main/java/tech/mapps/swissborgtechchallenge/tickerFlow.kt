package tech.mapps.swissborgtechchallenge

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration

fun tickerFlow(
    period: Duration,
    initialDelay: Duration = Duration.ZERO,
) = flow {
    delay(initialDelay)
    while (true) {
        emit(Unit)
        delay(period)
    }
}