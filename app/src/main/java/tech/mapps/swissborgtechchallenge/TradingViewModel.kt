@file:OptIn(ExperimentalCoroutinesApi::class)

package tech.mapps.swissborgtechchallenge

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tech.mapps.swissborgtechchallenge.TickerSorting.ByNameAscending
import tech.mapps.swissborgtechchallenge.TickerSorting.ByNameDescending
import tech.mapps.swissborgtechchallenge.TickerSorting.ByPriceAscending
import tech.mapps.swissborgtechchallenge.TickerSorting.ByPriceDescending

data class TradingState(
    private val allTickers: ImmutableList<Ticker> = persistentListOf(),
    val connectivityStatus: ConnectivityStatus? = null,
    val tickerSorting: TickerSorting = ByPriceDescending,
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
                ByPriceDescending -> ByPriceAscending
                else -> ByPriceDescending
            },
        )

    fun sortByName() =
        copy(
            tickerSorting =
            when (this.tickerSorting) {
                ByNameAscending -> ByNameDescending
                else -> ByNameAscending
            },
        )
}

@HiltViewModel
class TradingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    coroutineScope: CoroutineScope,
    private val connectivityStatusTracker: ConnectivityStatusTracker,
    private val observeTickers: ObserveTickers,
) : ViewModel(coroutineScope) {
    private val query = savedStateHandle.getStateFlow(QUERY, "")

    private val _state = MutableStateFlow(TradingState())
    val state = combine(_state, query) { tradingState, query ->
        tradingState.copy(
            allTickers = tradingState.tickers,
            query = query,
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TradingState(),
        )

    fun init() {
        viewModelScope.launch {
            connectivityStatusTracker
                .observeConnectivity()
                .flatMapLatest { connectivityStatus ->
                    observeTickers()
                        .mapLatest { it to connectivityStatus }
                }
                .collect { (either, connectivityStatus) ->
                    _state.update { currentState ->
                        either.fold(
                            ifLeft = { currentState.copy(error = it) },
                            ifRight = { currentState.copy(allTickers = it, error = null) },
                        )
                            .copy(connectivityStatus = connectivityStatus)
                    }
                }
        }
    }

    fun search(query: String) {
        savedStateHandle[QUERY] = query
    }

    fun sortByPrice() =
        _state.update { it.sortByPrice() }

    fun sortByName() =
        _state.update { it.sortByName() }

    companion object {
        const val QUERY = "query"
    }
}

fun ImmutableList<Ticker>.search(query: String) =
    filter { it.ticker.contains(query, ignoreCase = true) }.toImmutableList()