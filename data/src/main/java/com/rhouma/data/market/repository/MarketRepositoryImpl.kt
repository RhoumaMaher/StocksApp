package com.rhouma.data.market.repository

import com.rhouma.data.market.mapper.MarketMapper
import com.rhouma.data.market.remote.MarketApi
import com.rhouma.domain.common.base_result.BaseResult
import com.rhouma.domain.market.model.MarketModel
import com.rhouma.domain.market.repository.MarketRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.json.JSONObject
import javax.inject.Inject

class MarketRepositoryImpl @Inject constructor(private val api: MarketApi) : MarketRepository {
    override fun getMarketsList(region: String): Flow<BaseResult<List<MarketModel>, String>> = flow {
        emit(BaseResult.Loading)
        try {
            val response = api.getMarketSummary(
                region = region
            )
            if (response.isSuccessful) {
                val data = response.body()
                if (data != null){
                    // Handle successful response
                    val result = MarketMapper.marketResponseMapper(data.marketSummaryAndSparkResponse.result)
                    emit(BaseResult.Success(result))
                } else {
                    emit(BaseResult.Error("Response body is empty"))
                }
            } else {
                // Handle unsuccessful response
                emit(BaseResult.Error("Unknown error occurred"))
            }

        }catch (e: Exception){
            println("error: ${e.message}")
            emit(BaseResult.Error("Network error occurred."))
        }

    }
}