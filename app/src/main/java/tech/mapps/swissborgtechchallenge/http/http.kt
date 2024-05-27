package tech.mapps.swissborgtechchallenge.http

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

val httpClient =
    HttpClient {
        expectSuccess = true
        logging()
        httpTimeout()
        contentNegotiation()
    }

private fun HttpClientConfig<*>.logging() {
    install(Logging) {
        logger = Logger.DEFAULT
        level = LogLevel.ALL
    }
}

private fun HttpClientConfig<*>.httpTimeout() {
    install(HttpTimeout) {
        requestTimeoutMillis = 15_000
    }
}

private fun HttpClientConfig<*>.contentNegotiation() {
    install(ContentNegotiation) {
        json(
            Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            },
        )
    }
}