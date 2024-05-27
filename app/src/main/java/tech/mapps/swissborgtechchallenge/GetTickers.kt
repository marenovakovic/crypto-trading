package tech.mapps.swissborgtechchallenge

import arrow.core.Either
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import tech.mapps.swissborgtechchallenge.api.CryptoTradingApi
import tech.mapps.swissborgtechchallenge.api.Ticker

fun interface GetTickers {
    suspend operator fun invoke(): Either<Unit, ImmutableList<Ticker>>
}

class GetTickersImpl @Inject constructor(
    private val api: CryptoTradingApi,
) : GetTickers {

    override suspend fun invoke(): Either<Unit, ImmutableList<Ticker>> =
        Either
            .catch { api.getTickers().toImmutableList() }
            .mapLeft {}
}