package tech.mapps.swissborgtechchallenge

import tech.mapps.swissborgtechchallenge.api.Ticker
import kotlin.random.Random

val randomTickers: List<Ticker>
    get() = List(Random.nextInt(1, 5)) {
        GenericTicker(symbol = it.toString())
    }