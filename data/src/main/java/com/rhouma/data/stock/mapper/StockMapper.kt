package com.rhouma.data.stock.mapper

import com.rhouma.data.stock.dto.StockInfoResponse
import com.rhouma.domain.stock.model.StockInfoModel

object StockMapper {
    fun mapToStockInfo(response: StockInfoResponse): StockInfoModel {
        val marketPrice = response.price

        return StockInfoModel(
            symbol = response.symbol,
            shortName = marketPrice.shortName,
            regularMarketPrice = marketPrice.regularMarketPrice.raw,
            regularMarketChange = marketPrice.regularMarketChange.raw,
            regularMarketChangePercent = marketPrice.regularMarketChangePercent.raw,
            regularMarketVolume = marketPrice.regularMarketVolume.raw,
        )
    }
}