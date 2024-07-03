package com.rhouma.domain.market.usecase


import com.rhouma.domain.common.base_result.BaseResult
import com.rhouma.domain.market.model.MarketModel
import com.rhouma.domain.market.repository.MarketRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.Assert.assertEquals
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class GetMarketListUseCaseTest {

    private val marketRepository = mock(MarketRepository::class.java)
    private val getMarketListUseCase = GetMarketListUseCase(marketRepository)

    @Test
    fun `invoke should return success result`() = runTest {
        val region = "US"
        val marketModelList = listOf(
            MarketModel("AAPL", "Apple Inc.", "145.00", "2023-12-31T12:00:00Z"),
            MarketModel("GOOGL", "Alphabet Inc.", "2730.00", "2023-12-31T12:00:00Z")
        )
        val expectedMarketModelList = listOf(
            MarketModel("AAPL", "Apple Inc.", "145.00", "2023-12-31T12:00:00Z"),
            MarketModel("GOOGL", "Alphabet Inc.", "2730.00", "2023-12-31T12:00:00Z")
        )

        val successResult = BaseResult.Success(marketModelList)
        val expectedResult = BaseResult.Success(expectedMarketModelList)

        `when`(marketRepository.getMarketsList(region)).thenReturn(flowOf(successResult))

        val result = getMarketListUseCase(region).first()

        assertEquals(expectedResult, result)
        verify(marketRepository).getMarketsList(region)
    }

    @Test
    fun `invoke should return error result`() = runTest {
        val region = "US"
        val errorResult = BaseResult.Error("Network error occurred.")
        val expectedErrorResult = BaseResult.Error("Network error occurred.")

        `when`(marketRepository.getMarketsList(region)).thenReturn(flowOf(errorResult))

        val result = getMarketListUseCase(region).first()

        assertEquals(expectedErrorResult, result)
        verify(marketRepository).getMarketsList(region)
    }
}
