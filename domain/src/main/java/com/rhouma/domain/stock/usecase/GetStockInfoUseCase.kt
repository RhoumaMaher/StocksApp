package com.rhouma.domain.stock.usecase

import com.rhouma.domain.common.base_result.BaseResult
import com.rhouma.domain.stock.model.StockInfoModel
import com.rhouma.domain.stock.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetStockInfoUseCase @Inject constructor(private val repo: StockRepository) {
    operator fun invoke(region: String, symbol: String) : Flow<BaseResult<StockInfoModel, String>> {
        return repo.getStockInfo(region, symbol)
    }
}