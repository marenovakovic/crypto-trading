package tech.mapps.swissborgtechchallenge.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import javax.inject.Inject

private const val pairs =
    "tBTCUSD,tETHUSD,tCHSB:USD,tLTCUSD,tXRPUSD,tDSHUSD,tRRTUSD," +
            "tEOSUSD,tSANUSD,tDATUSD,tSNTUSD,tDOGE:USD,tLUNA:USD,tMATIC:USD," +
            "tNEXO:USD,tOCEAN:USD,tBEST:USD,tAAVE:USD,tPLUUSD,tFILUSD"

class BitfinexTradingApi @Inject constructor(
    private val httpClient: HttpClient,
) : CryptoTradingApi {

    override suspend fun getTickers(): List<BitfinexTicker> =
        httpClient
            .get {
                url("$BASE_URL/tickers")
                parameter("symbols", pairs)
            }
            .body()

    companion object {
        private const val BASE_URL = "https://api-pub.bitfinex.com/v2"
    }
}