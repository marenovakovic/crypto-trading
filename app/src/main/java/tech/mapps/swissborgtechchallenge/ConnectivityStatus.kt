package tech.mapps.swissborgtechchallenge

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

sealed interface ConnectivityStatus {
    data object Available : ConnectivityStatus
    data object Unavailable : ConnectivityStatus
}

fun interface ConnectivityStatusTracker {
    fun observeConnectivity(): Flow<ConnectivityStatus>
}

class ConnectivityStatusTrackerImpl(context: Context) : ConnectivityStatusTracker {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override fun observeConnectivity() = callbackFlow {
        val networkStatusCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onUnavailable() {
                trySend(ConnectivityStatus.Unavailable)
            }

            override fun onAvailable(network: Network) {
                trySend(ConnectivityStatus.Available)
            }

            override fun onLost(network: Network) {
                trySend(ConnectivityStatus.Unavailable)
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, networkStatusCallback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkStatusCallback)
        }
    }
}
