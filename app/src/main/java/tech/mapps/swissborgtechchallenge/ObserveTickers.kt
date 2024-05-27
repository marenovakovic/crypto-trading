package tech.mapps.swissborgtechchallenge

import arrow.core.Either
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import tech.mapps.swissborgtechchallenge.api.CryptoTradingApi
import tech.mapps.swissborgtechchallenge.api.Ticker
import kotlin.time.Duration
import kotlin.time.Duration.Companion.ZERO
import kotlin.time.Duration.Companion.seconds

fun interface ObserveTickers : () -> Flow<Either<Unit, ImmutableList<Ticker>>>

class ObserveTickersImpl @Inject constructor(
    private val api: CryptoTradingApi,
    private val period: Duration = 5.seconds,
) : ObserveTickers {
    override fun invoke(): Flow<Either<Unit, ImmutableList<Ticker>>> =
        tickerFlow(period = period, initialDelay = ZERO)
            .flatMapLatest { flowOf(getTickers()) }

    private suspend fun getTickers() =
        Either
            .catch { api.getTickers().toImmutableList() }
            .mapLeft { it.printStackTrace() }
}