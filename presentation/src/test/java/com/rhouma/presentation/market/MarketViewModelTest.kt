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
    fun `test initial state of flows`() = testScope.runTest {
        // Assert initial values of the flows
        assertEquals(false, viewModel.loading.value)
        assertEquals(emptyList(), viewModel.marketList.value)
        assertEquals("", viewModel.searchQuery.value)
        assertEquals(emptyList(), viewModel.filteredMarketList.value)
    }

}
