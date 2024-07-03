package com.rhouma.data.market.remote

import com.rhouma.data.BuildConfig
import com.rhouma.data.market.dto.MarketListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MarketApi {

    @GET("market/v2/get-summary")
    suspend fun getMarketSummary(
        @Header("x-rapidapi-key") apiKey: String = BuildConfig.API_KEY,
        @Query("region") region: String
    ): Response<MarketListResponse>
}