package com.rhouma.data.stock.repository

import com.rhouma.data.stock.dto.EmptyObject
import com.rhouma.data.stock.dto.MarketData
import com.rhouma.data.stock.dto.MarketPrice
import com.rhouma.data.stock.dto.MarketVolume
import com.rhouma.data.stock.dto.QuoteType
import com.rhouma.data.stock.dto.StockInfoResponse
import com.rhouma.data.stock.dto.SummaryDetail
import com.rhouma.data.stock.remote.StockApi
import com.rhouma.domain.common.base_result.BaseResult
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
class StockRepositoryImplTest {

    private var stockApi = mock(StockApi::class.java)
    private var stockRepository = StockRepositoryImpl(stockApi)

    @Test
    fun `getStockInfo should return success result for successful response`() = runTest {
        // Given
        val region = "US"
        val symbol = "AAPL"
        val stockInfoResponse = StockInfoResponse(
            quoteType = QuoteType("NASDAQ", "Apple Inc.", "Apple Inc.", "America/New_York", false, "EST", false, "3600000", "EQUITY", "AAPL", "fms", "us_market"),
            symbol = symbol,
            price = MarketPrice(
                "NasdaqGS",
                MarketData(100f, "100", null),
                MarketVolume(1000000L, "1M", "1,000,000"),
                "NasdaqGS",
                1621612800L,
                EmptyObject(),
                MarketData(101f, "101", null),
                "Apple Inc.",
                MarketVolume(100000L, "100K", "100,000"),
                "Apple Inc.",
                MarketData(0.5f, "0.5", null),
                "$",
                MarketData(99f, "99", null),
                EmptyObject(),
                15,
                null,
                EmptyObject(),
                EmptyObject(),
                "Nasdaq",
                EmptyObject(),
                EmptyObject(),
                MarketData(98f, "98", null),
                MarketData(2f, "2", null),
                "USD",
                MarketData(150f, "150", null),
                MarketVolume(200000L, "200K", "200,000"),
                null,
                "REALTIME",
                EmptyObject(),
                "OPEN",
                null,
                EmptyObject(),
                "EQUITY",
                EmptyObject(),
                EmptyObject(),
                symbol,
                1,
                null,
                MarketData(0.33f, "0.33", null)
            ),
            summaryDetail = SummaryDetail(
                previousClose = MarketData(150f, "150", null),
                regularMarketOpen = MarketData(151f, "151", null),
                twoHundredDayAverage = MarketData(140f, "140", null),
                trailingAnnualDividendYield = EmptyObject(),
                payoutRatio = EmptyObject(),
                volume24Hr = EmptyObject(),
                regularMarketDayHigh = MarketData(152f, "152", null),
                navPrice = EmptyObject(),
                averageDailyVolume10Day = MarketVolume(100000L, "100K", "100,000"),
                totalAssets = EmptyObject(),
                regularMarketPreviousClose = MarketData(149f, "149", null),
                fiftyDayAverage = MarketData(145f, "145", null),
                trailingAnnualDividendRate = EmptyObject(),
                open = MarketData(150f, "150", null),
                toCurrency = null,
                averageVolume10days = MarketVolume(100000L, "100K", "100,000"),
                expireDate = EmptyObject(),
                yield = EmptyObject(),
                algorithm = null,
                dividendRate = EmptyObject(),
                exDividendDate = EmptyObject(),
                beta = EmptyObject(),
                circulatingSupply = EmptyObject(),
                startDate = EmptyObject(),
                regularMarketDayLow = MarketData(2f, "2", null),
                priceHint = MarketData(2f, "2", null),
                currency = "USD",
                regularMarketVolume = MarketVolume(200000L, "200K", "200,000"),
                lastMarket = null,
                maxSupply = EmptyObject(),
                openInterest = EmptyObject(),
                marketCap = EmptyObject(),
                volumeAllCurrencies = EmptyObject(),
                strikePrice = EmptyObject(),
                averageVolume = MarketVolume(100000L, "100K", "100,000"),
                priceToSalesTrailing12Months = EmptyObject(),
                dayLow = MarketData(120f, "120", null),
                ask = MarketData(120f, "120", null),
                ytdReturn = EmptyObject(),
                askSize = EmptyObject(),
                volume = MarketVolume(100000L, "100K", "100,000"),
                fiftyTwoWeekHigh = MarketData(80f, "80", null),
                forwardPE = EmptyObject(),
                maxAge = 1,
                fromCurrency = null,
                fiveYearAvgDividendYield = EmptyObject(),
                fiftyTwoWeekLow = MarketData(90f, "90", null),
                bid = MarketData(120f, "120", null),
                tradeable = true,
                dividendYield = EmptyObject(),
                bidSize = EmptyObject(),
                dayHigh = MarketData(100f, "100", null),
                coinMarketCapLink = null
            )

        )

        `when`(stockApi.getStockSummary(region = region, symbol = symbol)).thenReturn(Response.success(stockInfoResponse))

        // When
        val result = stockRepository.getStockInfo(region, symbol).toList()

        // Then
        assertTrue(result[0] is BaseResult.Loading)
        assertTrue(result[1] is BaseResult.Success)
        assertEquals(stockInfoResponse.symbol, (result[1] as BaseResult.Success).data.symbol)
    }

    @Test
    fun `getStockInfo should return error result for unsuccessful response`() = runTest {
        // Given
        val region = "US"
        val symbol = "AAPL"
        val errorMessage = "Unknown error occurred"

        `when`(stockApi.getStockSummary(region = region, symbol = symbol)).thenReturn(Response.error(500, ResponseBody.create(null, "Internal Server Error")))

        // When
        val result = stockRepository.getStockInfo(region = region, symbol = symbol).toList()

        // Then
        assertTrue(result[0] is BaseResult.Loading)
        assertTrue(result[1] is BaseResult.Error)
        assertEquals(errorMessage, (result[1] as BaseResult.Error).rawResponse)
    }

    @Test
    fun `getStockInfo should return error result for network error`() = runTest {
        // Given
        val region = "US"
        val symbol = "AAPL"
        val errorMessage = "Network error occurred."

        `when`(stockApi.getStockSummary(region = region, symbol = symbol)).thenThrow(RuntimeException("Network error occurred."))

        // When
        val result = stockRepository.getStockInfo(region = region, symbol = symbol).toList()

        // Then
        assertTrue(result[0] is BaseResult.Loading)
        assertTrue(result[1] is BaseResult.Error)
        assertEquals(errorMessage, (result[1] as BaseResult.Error).rawResponse)
    }
}
