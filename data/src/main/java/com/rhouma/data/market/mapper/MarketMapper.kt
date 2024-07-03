package com.rhouma.data.market.mapper

import com.rhouma.data.market.dto.Result
import com.rhouma.domain.market.model.MarketModel

object MarketMapper {

    fun marketResponseMapper(data:  List<Result>) : List<MarketModel> {
        return data.map {
            MarketModel(
                symbol = it.symbol,
                shortName = it.shortName,
                previousClose = it.regularMarketPreviousClose.fmt,
                marketTime = it.regularMarketTime.fmt
            )
        }
    }

}