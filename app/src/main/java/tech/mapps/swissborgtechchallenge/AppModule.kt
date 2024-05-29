package tech.mapps.swissborgtechchallenge

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import tech.mapps.swissborgtechchallenge.api.BitfinexTradingApi
import tech.mapps.swissborgtechchallenge.api.CryptoTradingApi

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Binds
    @Singleton
    fun cryptoTradingApi(impl: BitfinexTradingApi): CryptoTradingApi

    companion object {
        @Provides
        fun coroutineScope() = CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())

        @Provides
        @Singleton
        fun connectivityStatusTracker(@ApplicationContext context: Context): ConnectivityStatusTracker =
            ConnectivityStatusTrackerImpl(context)

        @Provides
        fun observeTickers(cryptoTradingApi: CryptoTradingApi): ObserveTickers =
            ObserveTickersImpl(cryptoTradingApi)
    }
}