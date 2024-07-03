package com.rhouma.domain.stock.model

data class StockInfoModel(
    val symbol: String? = "",
    val shortName: String? = "",
    val regularMarketPrice: Float? = 0.0f,
    val regularMarketChange: Float? = 0.0f,
    val regularMarketChangePercent: Float? = 0.0f,
    val regularMarketVolume: Long? = 0,
)