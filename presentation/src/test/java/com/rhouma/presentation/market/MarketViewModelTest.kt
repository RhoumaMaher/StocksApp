import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rhouma.domain.common.base_result.BaseResult
import com.rhouma.domain.market.model.MarketModel
import com.rhouma.domain.market.usecase.GetMarketListUseCase
import com.rhouma.presentation.market.MarketViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class MarketViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var getMarketListUseCase: GetMarketListUseCase
    private lateinit var viewModel: MarketViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getMarketListUseCase = mock(GetMarketListUseCase::class.java)
        viewModel = MarketViewModel(getMarketListUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testScope.cancel()
    }

    @Test
    fun `test initial state of live data`() = testScope.runTest {
        // Assert initial values of LiveData
        assertEquals(false, viewModel.loading.value)
        assertEquals(emptyList(), viewModel.marketList.value)
        assertEquals("", viewModel.searchQuery.value)
        assertEquals(emptyList(), viewModel.filteredMarketList.value)
    }

    @Test
    fun `test loading state during data fetch`() = testScope.runTest {
        val marketList = listOf(
            MarketModel("AAPL", "Apple Inc.", "145.00", "2023-12-31T12:00:00Z")
        )

        `when`(getMarketListUseCase.invoke(anyString())).thenReturn(flow {
            emit(BaseResult.Loading)
            emit(BaseResult.Success(marketList))
        })

        //viewModel.startCall()

        // Check loading state while fetching data
        assertEquals(true, viewModel.loading.value)

        // Advance time to simulate data fetch completion
        testScheduler.advanceUntilIdle()

        // Check loading state after data fetch
        assertEquals(false, viewModel.loading.value)
    }

    @Test
    fun testSuccessfulDataFetchAndUpdateMarketList() = testScope.runTest {
        val marketList = listOf(
            MarketModel("AAPL", "Apple Inc.", "145.00", "2023-12-31T12:00:00Z")
        )

        `when`(getMarketListUseCase.invoke(anyString())).thenReturn(flow {
            emit(BaseResult.Success(marketList))
        })

        println("Starting the call")
        viewModel.onFragmentResumed()
        //viewModel.startCall()
        println("Started the call")

        // Advance time to simulate data fetch completion
        testScheduler.advanceUntilIdle()
        println("Advanced until idle")

        // Assert market list and filtered list are updated
        assertEquals(marketList, viewModel.marketList.value)
        println("Market list assertion passed")
        assertEquals(marketList, viewModel.filteredMarketList.value)
        println("Filtered market list assertion passed")
    }

    @Test
    fun `test search query filtering`() = testScope.runTest {
        val marketList = listOf(
            MarketModel("AAPL", "Apple Inc.", "145.00", "2023-12-31T12:00:00Z"),
            MarketModel("GOOGL", "Alphabet Inc.", "2750.00", "2023-12-31T12:00:00Z")
        )

        `when`(getMarketListUseCase.invoke(anyString())).thenReturn(flow {
            emit(BaseResult.Success(marketList))
        })

        //viewModel.startCall()
        testScheduler.advanceUntilIdle()

        viewModel.onSearchQueryChanged("Apple")

        // Assert filtered list is updated based on search query
        assertEquals(1, viewModel.filteredMarketList.value.size)
        assertEquals("Apple Inc.", viewModel.filteredMarketList.value[0].shortName)
    }

    @Test
    fun `test fragment visibility and data refresh`() = testScope.runTest {
        val marketList = listOf(
            MarketModel("AAPL", "Apple Inc.", "145.00", "2023-12-31T12:00:00Z")
        )

        `when`(getMarketListUseCase.invoke(anyString())).thenReturn(flow {
            emit(BaseResult.Success(marketList))
        })

        // Fragment resumed, data fetch triggered
        viewModel.onFragmentResumed()
        //viewModel.startCall()
        testScheduler.advanceUntilIdle()

        // Assert data is fetched and job is running
        assertEquals(marketList, viewModel.marketList.value)
        assertTrue(viewModel.loading.value)

        // Fragment paused, data refresh job cancelled
        viewModel.onFragmentPaused()
        assertFalse(viewModel.loading.value)
    }
}
