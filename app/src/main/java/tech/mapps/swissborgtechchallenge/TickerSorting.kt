package tech.mapps.swissborgtechchallenge

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

enum class TickerSorting(private val comparator: Comparator<Ticker>) {
    ByPriceAscending(compareBy { it.price }),
    ByPriceDescending(compareByDescending { it.price }),
    ByNameAscending(compareBy { it.ticker }),
    ByNameDescending(compareByDescending { it.ticker });

    fun sort(tickers: ImmutableList<Ticker>) =
        tickers
            .sortedWith(this.comparator)
            .toImmutableList()
}