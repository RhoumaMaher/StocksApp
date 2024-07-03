package com.rhouma.data.stock.remote

import com.rhouma.data.BuildConfig
import com.rhouma.data.stock.dto.StockInfoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface StockApi {
    @GET("stock/v2/get-summary")
    suspend fun getStockSummary(
        @Header("x-rapidapi-key") apiKey: String = BuildConfig.API_KEY,
        @Query("region") region: String,
        @Query("symbol") symbol: String
    ): Response<StockInfoResponse>
}