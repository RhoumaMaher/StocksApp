package com.rhouma.data.market.repository

import com.rhouma.data.market.dto.MarketListResponse
import com.rhouma.data.market.dto.MarketSummaryAndSparkResponse
import com.rhouma.data.market.dto.RegularMarketPreviousClose
import com.rhouma.data.market.dto.RegularMarketTime
import com.rhouma.data.market.dto.Result
import com.rhouma.data.market.dto.Spark
import com.rhouma.data.market.remote.MarketApi
import com.rhouma.domain.common.base_result.BaseResult
import com.rhouma.domain.market.model.MarketModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.Response

@ExperimentalCoroutinesApi
class MarketRepositoryImplTest {

    private var marketApi = mock(MarketApi::class.java)
    private var marketRepository = MarketRepositoryImpl(marketApi)

    @Test
    fun `getMarketsList should return success result`() = runTest {
        val region = "US"
        val marketListResponse = MarketListResponse(
            marketSummaryAndSparkResponse = MarketSummaryAndSparkResponse(
                error = "",
                result = listOf(
                    Result(
                        cryptoTradeable = false,
                        customPriceAlertConfidence = "HIGH",
                        exchange = "NASDAQ",
                        exchangeDataDelayedBy = 0,
                        exchangeTimezoneName = "America/New_York",
                        exchangeTimezoneShortName = "EST",
                        firstTradeDateMilliseconds = 1279302000000,
                        fullExchangeName = "NasdaqGS",
                        gmtOffSetMilliseconds = -18000000,
                        hasPrePostMarketData = true,
                        language = "en-US",
                        market = "us_market",
                        marketState = "CLOSED",
                        priceHint = 2,
                        quoteType = "EQUITY",
                        region = "US",
                        regularMarketPreviousClose = RegularMarketPreviousClose(fmt = "145.00", raw = 145.00),
                        regularMarketTime = RegularMarketTime(fmt = "2023-12-31T12:00:00Z", raw = 1672540800),
                        shortName = "Apple Inc.",
                        sourceInterval = 15,
                        spark = Spark(0.0, listOf(0.0),0, 0, 0.0, 0, "sym", listOf(0)),
                        symbol = "AAPL",
                        tradeable = true,
                        triggerable = true
                    )
                )
            )
        )

        `when`(marketApi.getMarketSummary(region = region)).thenReturn(Response.success(marketListResponse))

        val result = marketRepository.getMarketsList(region).toList().last()

        val expectedMarketModelList = listOf(
            MarketModel("AAPL", "Apple Inc.", "145.00", "2023-12-31T12:00:00Z")
        )
        val expectedResult = BaseResult.Success(expectedMarketModelList)

        assertEquals(expectedResult, result)
    }

    @Test
    fun `getMarketsList should return error result for unsuccessful response`() = runTest {
        val region = "US"
        val errorMessage = "Unknown error occurred"

        `when`(marketApi.getMarketSummary(region = region)).thenReturn(Response.error(500, ResponseBody.create(null, "Internal Server Error")))

        val result = marketRepository.getMarketsList(region).toList().last()

        val expectedResult = BaseResult.Error(errorMessage)

        assertEquals(expectedResult, result)
    }

    @Test
    fun `getMarketsList should return error result for network error`() = runTest {
        val region = "US"
        val errorMessage = "Network error occurred."

        `when`(marketApi.getMarketSummary(region = region)).thenThrow(RuntimeException("Network error occurred."))

        val result = marketRepository.getMarketsList(region).toList().last()

        val expectedResult = BaseResult.Error(errorMessage)

        assertEquals(expectedResult, result)
    }
}
