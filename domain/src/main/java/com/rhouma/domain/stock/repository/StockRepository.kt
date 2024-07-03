package com.rhouma.domain.stock.repository

import com.rhouma.domain.common.base_result.BaseResult
import com.rhouma.domain.stock.model.StockInfoModel
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    fun getStockInfo(region: String, symbol: String): Flow<BaseResult<StockInfoModel, String>>
}