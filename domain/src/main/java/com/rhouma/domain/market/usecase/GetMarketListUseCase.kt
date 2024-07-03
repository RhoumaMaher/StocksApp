package com.rhouma.domain.market.usecase

import com.rhouma.domain.common.base_result.BaseResult
import com.rhouma.domain.market.model.MarketModel
import com.rhouma.domain.market.repository.MarketRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMarketListUseCase @Inject constructor(private val repo: MarketRepository) {
    operator fun invoke(region: String) : Flow<BaseResult<List<MarketModel>, String>> {
        return repo.getMarketsList(region)
    }
}