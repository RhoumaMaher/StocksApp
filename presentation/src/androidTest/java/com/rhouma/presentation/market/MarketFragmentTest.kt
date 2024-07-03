package com.rhouma.presentation.market

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.rhouma.domain.market.model.MarketModel
import com.rhouma.domain.market.usecase.GetMarketListUseCase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@LargeTest
class MarketFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeTestRule = createComposeRule()

    @Inject
    lateinit var getMarketListUseCase: GetMarketListUseCase

    private lateinit var viewModel: MarketViewModel
    private val marketListFlow = MutableStateFlow(emptyList<MarketModel>())
    private val searchQueryFlow = MutableStateFlow("")
    private val loadingFlow = MutableStateFlow(false)

    @Before
    fun setUp() {
        hiltRule.inject()
        MockKAnnotations.init(this)

        viewModel = mockk(relaxed = true) {
            every { filteredMarketList } returns marketListFlow
            every { searchQuery } returns searchQueryFlow
            every { loading } returns loadingFlow
        }
    }

    @Test
    fun testMarketFragmentDisplaysMarketItems() = runBlocking {
        // Given
        val marketList = listOf(
            MarketModel("AAPL", "Apple Inc.", "145.00", "2023-12-31T12:00:00Z"),
            MarketModel("GOOGL", "Alphabet Inc.", "2750.00", "2023-12-31T12:00:00Z")
        )
        marketListFlow.value = marketList

        val scenario = launchFragmentInContainer<MarketFragment>()

        scenario.onFragment { fragment ->
            // Use reflection to set the mock ViewModel into the fragment
            val viewModelField = MarketFragment::class.java.getDeclaredField("viewModel")
            viewModelField.isAccessible = true
            viewModelField.set(fragment, viewModel)
        }

        // When
        // Perform actions or set state in the fragment

        // Then
        // Verify that the fragment displays the market items
        composeTestRule.onNodeWithText("Apple Inc.").assertExists()
        composeTestRule.onNodeWithText("Alphabet Inc.").assertExists()
    }
}
