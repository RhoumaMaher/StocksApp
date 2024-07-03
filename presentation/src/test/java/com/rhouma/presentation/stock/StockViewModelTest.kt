package com.rhouma.presentation.stock

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rhouma.domain.common.base_result.BaseResult
import com.rhouma.domain.stock.model.StockInfoModel
import com.rhouma.domain.stock.usecase.GetStockInfoUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class StockViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: StockViewModel

    @Mock
    private lateinit var getStockInfoUseCase: GetStockInfoUseCase

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = StockViewModel(getStockInfoUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cancel()  // Cancel any active coroutines in the test scope
    }

    @Test
    fun `getStockInfo emits success result`() = testScope.runTest {
        val stockInfo = StockInfoModel(symbol = "AAPL", shortName = "Apple Inc.")
        val flow = flow {
            emit(BaseResult.Success(stockInfo))
        }

        Mockito.`when`(getStockInfoUseCase.invoke(Mockito.anyString(), Mockito.anyString())).thenReturn(flow)

        viewModel.getStockInfo("US", "AAPL")

        testScheduler.advanceUntilIdle()

        assertEquals(false, viewModel.loading.value)
        assertEquals(stockInfo, viewModel.stockInfo.value)
        assertEquals(null, viewModel.error.value)
    }

    @Test
    fun `getStockInfo emits error result`() = testScope.runTest {
        val errorMessage = "Network error"
        val flow = flow {
            emit(BaseResult.Error(errorMessage))
        }

        Mockito.`when`(getStockInfoUseCase.invoke(Mockito.anyString(), Mockito.anyString())).thenReturn(flow)

        viewModel.getStockInfo("US", "AAPL")

        testScheduler.advanceUntilIdle()

        assertEquals(false, viewModel.loading.value)
        assertEquals(null, viewModel.stockInfo.value)
        assertEquals(errorMessage, viewModel.error.value)
    }

    @Test
    fun `getStockInfo emits loading state`() = testScope.runTest {
        val flow = flow {
            emit(BaseResult.Loading)
        }

        Mockito.`when`(getStockInfoUseCase.invoke(Mockito.anyString(), Mockito.anyString())).thenReturn(flow)

        viewModel.getStockInfo("US", "AAPL")

        testScheduler.advanceUntilIdle()

        assertEquals(true, viewModel.loading.value)
    }
}
