package com.rhouma.domain.stock.usecase

import com.rhouma.domain.common.base_result.BaseResult
import com.rhouma.domain.stock.model.StockInfoModel
import com.rhouma.domain.stock.repository.StockRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
class GetStockInfoUseCaseTest {

    private var stockRepository = mock(StockRepository::class.java)
    private var getStockInfoUseCase = GetStockInfoUseCase(stockRepository)

    @Test
    fun `invoke should return success result`() = runTest {
        // Given
        val region = "US"
        val symbol = "AAPL"
        val stockInfoModel = StockInfoModel(symbol = symbol, shortName = "Apple Inc.", regularMarketPrice = 150.0f)
        val expectedStockInfoModel = StockInfoModel(symbol = symbol, shortName = "Apple Inc.", regularMarketPrice = 150.0f)

        `when`(stockRepository.getStockInfo(region, symbol)).thenReturn(flow {
            emit(BaseResult.Loading)
            emit(BaseResult.Success(stockInfoModel))
        })

        // When
        val result = getStockInfoUseCase(region, symbol).toList()

        // Then
        assertTrue(result[0] is BaseResult.Loading)
        assertTrue(result[1] is BaseResult.Success)
        assertEquals(expectedStockInfoModel, (result[1] as BaseResult.Success).data)
    }

    @Test
    fun `invoke should return error result`() = runTest {
        // Given
        val region = "US"
        val symbol = "AAPL"
        val errorMessage = "Error fetching data"
        val expectedErrorMessage = "Error fetching data"

        `when`(stockRepository.getStockInfo(region, symbol)).thenReturn(flow {
            emit(BaseResult.Loading)
            emit(BaseResult.Error(errorMessage))
        })

        // When
        val result = getStockInfoUseCase(region, symbol).toList()

        // Then
        assertTrue(result[0] is BaseResult.Loading)
        assertTrue(result[1] is BaseResult.Error)
        assertEquals(expectedErrorMessage, (result[1] as BaseResult.Error).rawResponse)
    }
}
