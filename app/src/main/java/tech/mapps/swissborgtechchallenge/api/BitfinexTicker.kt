package tech.mapps.swissborgtechchallenge.api

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.float
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive

@Serializable(with = BitfinexTickerSerializer::class)
data class BitfinexTicker(
    override val symbol: String = "",
    override val priceOfLastHighestBid: Float = 0f,
    override val sumOf25HighestBids: Float = 0f,
    override val lastLowestAsk: Float = 0f,
    override val sumOf25LowestAsks: Float = 0f,
    override val dailyChange: Float = 0f,
    override val dailyChangePercentage: Float = 0f,
    override val lastTradePrice: Float = 0f,
    override val dailyVolume: Float = 0f,
    override val dailyHigh: Float = 0f,
    override val dailyLow: Float = 0f,
) : Ticker

object BitfinexTickerSerializer : KSerializer<BitfinexTicker> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("BitfinexTicker", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): BitfinexTicker {
        decoder as JsonDecoder
        val array = decoder.decodeJsonElement().jsonArray
        return BitfinexTicker(
            symbol = array[0].jsonPrimitive.toString(),
            priceOfLastHighestBid = array[1].jsonPrimitive.float,
            sumOf25HighestBids = array[2].jsonPrimitive.float,
            lastLowestAsk = array[3].jsonPrimitive.float,
            sumOf25LowestAsks = array[4].jsonPrimitive.float,
            dailyChange = array[5].jsonPrimitive.float,
            dailyChangePercentage = array[6].jsonPrimitive.float,
            lastTradePrice = array[7].jsonPrimitive.float,
            dailyVolume = array[8].jsonPrimitive.float,
            dailyHigh = array[9].jsonPrimitive.float,
            dailyLow = array[10].jsonPrimitive.float,
        )
    }

    override fun serialize(encoder: Encoder, value: BitfinexTicker) {
        val list = listOf(
            value.symbol,
            value.priceOfLastHighestBid,
            value.sumOf25HighestBids,
            value.lastLowestAsk,
            value.sumOf25LowestAsks,
            value.dailyChange,
            value.dailyChangePercentage,
            value.lastTradePrice,
            value.dailyVolume,
            value.dailyHigh,
            value.dailyLow,
        )
        encoder.encodeString(list.joinToString(","))
    }
}