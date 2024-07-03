package com.rhouma.data.market.dto

data class Result(
    val cryptoTradeable: Boolean,
    val customPriceAlertConfidence: String,
    val exchange: String,
    val exchangeDataDelayedBy: Int,
    val exchangeTimezoneName: String,
    val exchangeTimezoneShortName: String,
    val firstTradeDateMilliseconds: Long,
    val fullExchangeName: String,
    val gmtOffSetMilliseconds: Int,
    val hasPrePostMarketData: Boolean,
    val language: String,
    val market: String,
    val marketState: String,
    val priceHint: Int,
    val quoteType: String,
    val region: String,
    val regularMarketPreviousClose: RegularMarketPreviousClose,
    val regularMarketTime: RegularMarketTime,
    val shortName: String,
    val sourceInterval: Int,
    val spark: Spark,
    val symbol: String,
    val tradeable: Boolean,
    val triggerable: Boolean
)