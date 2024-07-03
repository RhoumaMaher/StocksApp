package com.rhouma.presentation.stock

import android.os.Bundle
import android.view.View
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rhouma.domain.stock.model.StockInfoModel
import com.rhouma.presentation.R
import com.rhouma.presentation.databinding.FragmentStockBinding
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class StockFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val mockNavController: NavController = mockk(relaxed = true)

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var scenario: FragmentScenario<StockFragment>

    @Before
    fun setUp() {
        hiltRule.inject()

        // Launch the fragment in a container
        scenario = launchFragmentInContainer<StockFragment>(Bundle(), R.style.AppTheme)

        // Set the NavController to the mock NavController
        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)

            // Use reflection to set the ViewModel
            val viewModelField = StockFragment::class.java.getDeclaredField("viewModel")
            viewModelField.isAccessible = true
            viewModelField.set(fragment, ViewModelProvider(fragment as ViewModelStoreOwner, viewModelFactory).get(StockViewModel::class.java))
        }
    }

    @Test
    fun testGetArgument_andObserveStockInfo() {
        val testStockInfo = StockInfoModel(
            symbol = "AAPL",
            shortName = "Apple Inc.",
            regularMarketPrice = 150.0f,
            regularMarketChange = 1.5f,
            regularMarketChangePercent = 1.0f,
            regularMarketVolume = 1000
        )

        val stockInfoFlow = MutableStateFlow<StockInfoModel?>(null)

        // Mock ViewModel behavior
        scenario.onFragment { fragment ->
            val viewModel = fragment.viewModel
            every { viewModel.stockInfo } returns stockInfoFlow
        }

        // Set the argument for the fragment
        scenario.onFragment { fragment ->
            fragment.arguments = Bundle().apply {
                putString("symbol", "AAPL")
            }

            // Trigger the ViewModel to emit the test data
            stockInfoFlow.value = testStockInfo
        }

        // Verify the UI elements are updated
        scenario.onFragment { fragment ->
            val binding = FragmentStockBinding.bind(fragment.requireView())
            assert(binding.tvSymbol.text == "AAPL")
            assert(binding.tvShortName.text == "Apple Inc.")
            assert(binding.tvRegularMarketPrice.text == "150.0")
            assert(binding.tvRegularMarketChange.text == "1.5")
            assert(binding.tvRegularMarketChangePercent.text == "1.0%")
            assert(binding.tvRegularMarketVolume.text == "1000")
        }
    }

    @Test
    fun testHandleBackButton() {
        scenario.onFragment { fragment ->
            fragment.view?.let { view ->
                view.findViewById<View>(R.id.back_btn).performClick()
                verify { mockNavController.popBackStack() }
            }
        }
    }
}
