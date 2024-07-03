package com.rhouma.data.stock.repository

import com.rhouma.data.stock.mapper.StockMapper
import com.rhouma.data.stock.remote.StockApi
import com.rhouma.domain.common.base_result.BaseResult
import com.rhouma.domain.stock.model.StockInfoModel
import com.rhouma.domain.stock.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(val api: StockApi) : StockRepository {
    override fun getStockInfo(region: String, symbol: String): Flow<BaseResult<StockInfoModel, String>> = flow {

        emit(BaseResult.Loading)

        try {
            val response = api.getStockSummary(
                region = region,
                symbol = symbol
            )
            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    // Handle successful response
                    val result = StockMapper.mapToStockInfo(data)
                    emit(BaseResult.Success(result))
                } else {
                    emit(BaseResult.Error("Response body is empty"))
                }
            } else {
                // Handle unsuccessful response
                emit(BaseResult.Error("Unknown error occurred"))
            }

        } catch (e: Exception) {
            println("error: ${e.message}")
            emit(BaseResult.Error("Network error occurred."))
        }

    }
}