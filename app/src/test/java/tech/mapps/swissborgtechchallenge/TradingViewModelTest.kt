@file:Suppress("IllegalIdentifier")

package tech.mapps.swissborgtechchallenge

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.TurbineTestContext
import app.cash.turbine.test
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Nested
import kotlin.test.Test
import kotlin.test.assertEquals

class TradingViewModelTest {

    private val coroutineScope = CoroutineScope(UnconfinedTestDispatcher())

    private fun viewModel(
        savedStateHandle: SavedStateHandle = SavedStateHandle(),
        coroutineScope: CoroutineScope = this.coroutineScope,
        connectivity: Flow<ConnectivityStatus> = flowOf(ConnectivityStatus.Available),
        tickers: Either<Unit, ImmutableList<Ticker>>? = randomTickers.toImmutableList().right(),
        tickersFlow: Flow<Either<Unit, ImmutableList<Ticker>>>? = null,
    ) =
        TradingViewModel(
            savedStateHandle = savedStateHandle,
            coroutineScope = coroutineScope,
            connectivityStatusTracker = { connectivity },
            observeTickers = { tickersFlow ?: flowOf(tickers!!) },
        )

    @Test
    fun `initial state`() =
        assertEquals(TradingState(), viewModel().state.value)

    @Test
    fun `monitor connectivity status`() = runTest {
        val connectivity = MutableStateFlow<ConnectivityStatus?>(null)

        val viewModel = viewModel(connectivity = connectivity.filterNotNull())

        viewModel.init()

        viewModel.state.test {
            skipItems(1)

            connectivity.update { ConnectivityStatus.Available }
            assertEquals(
                ConnectivityStatus.Available,
                awaitItem().connectivityStatus,
            )

            connectivity.update { ConnectivityStatus.Unavailable }
            assertEquals(
                ConnectivityStatus.Unavailable,
                awaitItem().connectivityStatus,
            )
        }
    }

    @Nested
    inner class ObserveTickers {

        @Test
        fun `observe tickers`() = runTest {
            val tickers = randomTickers.toImmutableList()
            val viewModel = viewModel(tickers = tickers.right())

            viewModel.init()

            viewModel.state.test {
                assertEquals(
                    TradingState(ConnectivityStatus.Available, tickers),
                    awaitItem(),
                )
            }
        }

        @Test
        fun `observe tickers error`() = runTest {
            val viewModel = viewModel(tickers = Unit.left())

            viewModel.init()

            viewModel.state.test {
                assertEquals(
                    TradingState(
                        connectivityStatus = ConnectivityStatus.Available,
                        tickers = persistentListOf(),
                        error = Unit,
                    ),
                    awaitItem(),
                )
            }
        }

        @Test
        fun `when error occurs still show stale data`() = runTest {
            val tickers = randomTickers.toImmutableList()
            val tickerFlow = MutableStateFlow<Either<Unit, ImmutableList<Ticker>>?>(null)
            val viewModel = viewModel(tickersFlow = tickerFlow.filterNotNull())

            viewModel.init()

            viewModel.state.test {
                skipItems(1)

                tickerFlow.update { tickers.right() }
                assertEquals(
                    TradingState(
                        connectivityStatus = ConnectivityStatus.Available,
                        tickers = tickers,
                        error = null,
                    ),
                    awaitItem(),
                )

                tickerFlow.update { Unit.left() }
                assertEquals(
                    TradingState(
                        connectivityStatus = ConnectivityStatus.Available,
                        tickers = tickers,
                        error = Unit,
                    ),
                    awaitItem(),
                )
            }
        }

        @Test
        fun `error is null when tickers are successfully observed`() = runTest {
            val tickers = randomTickers.toImmutableList()
            val tickerFlow = MutableStateFlow<Either<Unit, ImmutableList<Ticker>>?>(null)
            val viewModel = viewModel(tickersFlow = tickerFlow.filterNotNull())

            viewModel.init()

            viewModel.state.test {
                skipItems(1)

                tickerFlow.update { Unit.left() }
                assertEquals(
                    TradingState(
                        connectivityStatus = ConnectivityStatus.Available,
                        tickers = persistentListOf(),
                        error = Unit,
                    ),
                    awaitItem(),
                )

                tickerFlow.update { tickers.right() }
                assertEquals(
                    TradingState(
                        connectivityStatus = ConnectivityStatus.Available,
                        tickers = tickers,
                        error = null,
                    ),
                    awaitItem(),
                )
            }
        }

        @Test
        fun `success, error, success`() = runTest {
            val tickers = randomTickers.toImmutableList()
            val tickerFlow = MutableStateFlow<Either<Unit, ImmutableList<Ticker>>?>(null)
            val viewModel = viewModel(tickersFlow = tickerFlow.filterNotNull())

            viewModel.init()

            viewModel.state.test {
                skipItems(1)

                tickerFlow.update { tickers.right() }
                assertEquals(
                    TradingState(
                        connectivityStatus = ConnectivityStatus.Available,
                        tickers = tickers,
                        error = null,
                    ),
                    awaitItem(),
                )

                tickerFlow.update { Unit.left() }
                assertEquals(
                    TradingState(
                        connectivityStatus = ConnectivityStatus.Available,
                        tickers = tickers,
                        error = Unit,
                    ),
                    awaitItem(),
                )

                val newTickers = randomTickers.toImmutableList()
                tickerFlow.update { newTickers.right() }
                assertEquals(
                    TradingState(
                        connectivityStatus = ConnectivityStatus.Available,
                        tickers = newTickers,
                        error = null
                    ),
                    awaitItem(),
                )
            }
        }
    }

    @Nested
    inner class Search {

        @Test
        fun `search tickers`() = runTest {
            val tickers = randomTickers.toImmutableList()
            val query = tickers.first().ticker
            val viewModel = viewModel(tickers = tickers.right())

            viewModel.init()

            viewModel.state.test {
                skipItems(1)

                assertSearched(viewModel, query, tickers)
            }
        }

        @Test
        fun `search multiple times`() = runTest {
            val tickers = randomTickers.toImmutableList()
            val viewModel = viewModel(tickers = tickers.right())

            viewModel.init()

            viewModel.state.test {
                skipItems(1)

                assertSearched(viewModel, "a", tickers)
                assertSearched(viewModel, "b", tickers)
                assertSearched(viewModel, "c", tickers)
            }
        }

        private suspend fun TurbineTestContext<TradingState>.assertSearched(
            viewModel: TradingViewModel,
            query: String,
            tickers: ImmutableList<Ticker>,
        ) {
            viewModel.search(query)

            assertEquals(
                TradingState(
                    connectivityStatus = ConnectivityStatus.Available,
                    tickers = tickers.search(query),
                    query = query,
                    error = null,
                ),
                awaitItem()
            )
        }

        @Test
        fun `restore query after process death`() = runTest {
            val tickers = randomTickers.toImmutableList()
            val query = tickers.first().ticker
            val viewModel = viewModel(tickers = tickers.right())

            viewModel.init()

            viewModel.state.test {
                skipItems(1)

                viewModel.search(query)

                assertEquals(
                    TradingState(
                        connectivityStatus = ConnectivityStatus.Available,
                        tickers = tickers.search(query),
                        query = query,
                        error = null,
                    ),
                    awaitItem()
                )
            }
        }
    }

    @Nested
    inner class Sorting {

        private val tickersAscendingPrice =
            persistentListOf(
                Ticker(ticker = "z", price = "1", change24hPercentage = 1f),
                Ticker(ticker = "f", price = "2", change24hPercentage = 1f),
                Ticker(ticker = "a", price = "3", change24hPercentage = 1f),
            )
        private val tickersDescendingPrice = tickersAscendingPrice.reversed().toImmutableList()

        private val tickersAscendingName =
            persistentListOf(
                Ticker(ticker = "a", price = "", change24hPercentage = 1f),
                Ticker(ticker = "b", price = "", change24hPercentage = 1f),
                Ticker(ticker = "c", price = "", change24hPercentage = 1f),
            )
        private val tickersDescendingName = tickersAscendingName.reversed().toImmutableList()

        @Test
        fun `initially tickers are sorted by descending price`() = runTest {
            val viewModel = viewModel(tickers = tickersAscendingPrice.right())

            viewModel.init()

            viewModel.state.test {
                assertEquals(tickersDescendingPrice, awaitItem().tickers)
            }
        }

        @Test
        fun `calling sort by price on initial tickers sorts them by ascending price`() = runTest {
            val viewModel = viewModel(tickers = tickersAscendingPrice.right())

            viewModel.init()
            viewModel.sortByPrice()

            viewModel.state.test {
                assertEquals(tickersAscendingPrice, awaitItem().tickers)
            }
        }

        @Test
        fun `calling sort by price after sorting initial tickers sorts them by descending price`() =
            runTest {
                val viewModel = viewModel(tickers = tickersAscendingPrice.right())

                viewModel.init()
                viewModel.sortByPrice()
                viewModel.sortByPrice()

                viewModel.state.test {
                    assertEquals(tickersDescendingPrice, awaitItem().tickers)
                }
            }

        @Test
        fun `calling sort by name on initial tickers sorts them by ascending name`() = runTest {
            val viewModel = viewModel(tickers = tickersAscendingName.right())

            viewModel.init()
            viewModel.sortByName()

            viewModel.state.test {
                assertEquals(tickersAscendingName, awaitItem().tickers)
            }
        }

        @Test
        fun `calling sort by name after sorting initial tickers by name sorts them by descending name`() =
            runTest {
                val viewModel = viewModel(tickers = tickersAscendingName.right())

                viewModel.init()
                viewModel.sortByName()
                viewModel.sortByName()

                viewModel.state.test {
                    assertEquals(tickersDescendingName, awaitItem().tickers)
                }
            }
    }
}