package tech.mapps.swissborgtechchallenge

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class TradingState(
    private val allTickers: ImmutableList<Ticker> = persistentListOf(),
    val connectivityStatus: ConnectivityStatus? = null,
    val tickerSorting: TickerSorting = TickerSorting.ByPriceDescending,
    val query: String = "",
    val error: Unit? = null,
) {
    val isLoading =
        connectivityStatus != ConnectivityStatus.Unavailable &&
                error == null &&
                allTickers.isEmpty() &&
                query.isBlank()

    val tickers = tickerSorting.sort(allTickers.search(query))

    fun sortByPrice() =
        copy(
            tickerSorting =
            when (this.tickerSorting) {
                TickerSorting.ByPriceDescending -> TickerSorting.ByPriceAscending
                else -> TickerSorting.ByPriceDescending
            },
        )

    fun sortByName() =
        copy(
            tickerSorting =
            when (this.tickerSorting) {
                TickerSorting.ByNameAscending -> TickerSorting.ByNameDescending
                else -> TickerSorting.ByNameAscending
            },
        )
}