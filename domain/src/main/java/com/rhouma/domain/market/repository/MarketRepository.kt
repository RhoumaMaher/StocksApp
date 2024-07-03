package com.rhouma.domain.market.repository

import com.rhouma.domain.common.base_result.BaseResult
import com.rhouma.domain.market.model.MarketModel
import kotlinx.coroutines.flow.Flow

interface MarketRepository {

    fun getMarketsList(region: String): Flow<BaseResult<List<MarketModel>, String>>
}