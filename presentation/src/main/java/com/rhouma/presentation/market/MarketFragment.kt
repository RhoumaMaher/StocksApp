package com.rhouma.presentation.market

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.rhouma.domain.market.model.MarketModel
import com.rhouma.presentation.R
import com.rhouma.presentation.shared.LoadingIndicator
import com.rhouma.presentation.shared.SearchBar
import com.rhouma.presentation.ui.theme.MarketLightGrayColor
import com.rhouma.presentation.ui.theme.MarketPrimaryColor
import com.rhouma.presentation.ui.theme.MarketWhiteColor
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarketFragment : Fragment() {

    private val viewModel: MarketViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val markets by viewModel.filteredMarketList.collectAsState()
                val searchQuery by viewModel.searchQuery.collectAsState()
                val isLoading by viewModel.loading.collectAsState()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MarketLightGrayColor)
                        .padding(16.dp)
                ) {
                    SearchBar(
                        query = searchQuery,
                        onQueryChanged = viewModel::onSearchQueryChanged
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    MarketComposable(markets) { symbol ->
                        val action = MarketFragmentDirections.actionMarketFragmentToStockFragment(
                            symbol = symbol
                        )
                        findNavController().navigate(action)
                    }
                    if (isLoading && markets.isEmpty()) {
                        LoadingIndicator()
                    }

                }

            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.onFragmentResumed()
    }

    override fun onPause() {
        super.onPause()
        viewModel.onFragmentPaused()
    }
}

@Composable
fun MarketComposable(markets: List<MarketModel>, action: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        MarketList(markets = markets, action = action)
    }
}

@Composable
fun MarketList(markets: List<MarketModel>, action: (String) -> Unit) {
    LazyColumn {
        items(markets) { market ->
            MarketItem(market = market) { symbol ->
                action(symbol)
            }
        }
    }
}

@Composable
fun MarketItem(
    @DrawableRes icon: Int = R.drawable.stock_up_svgrepo_com,
    market: MarketModel,
    onClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MarketPrimaryColor, RoundedCornerShape(16.dp))
            .padding(6.dp)
            .clickable { onClick(market.symbol) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,

        ) {
        Row(
            modifier = Modifier
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
            )

            Text(
                text = market.shortName,
                style = TextStyle(
                    fontSize = 14.sp,
                    lineHeight = 42.sp,
                    fontFamily = FontFamily(Font(R.font.poppins)),
                    fontWeight = FontWeight(500),
                    color = MarketWhiteColor,

                    ),
                modifier = Modifier
                    .padding(start = 12.dp)
            )
        }

        Box(
            modifier = Modifier
                .height(60.dp)
                .width(60.dp)
                .background(MarketWhiteColor, shape = RoundedCornerShape(16.dp))
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.forward_btn),
                contentDescription = "",
                modifier = Modifier
                    .size(24.dp),
                colorFilter = ColorFilter.tint(MarketPrimaryColor)
            )
        }
    }

    Spacer(modifier = Modifier.padding(6.dp))
}

