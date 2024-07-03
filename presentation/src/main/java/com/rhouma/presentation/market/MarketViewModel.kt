package com.rhouma.presentation.market

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.rhouma.domain.common.base_result.BaseResult
import com.rhouma.domain.market.model.MarketModel
import com.rhouma.domain.market.usecase.GetMarketListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class MarketViewModel @Inject constructor(
    private val getMarketListUseCase: GetMarketListUseCase
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _marketsList = MutableStateFlow(emptyList<MarketModel>())
    val marketList = _marketsList.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _filteredMarketList = MutableStateFlow(emptyList<MarketModel>())
    val filteredMarketList = _filteredMarketList.asStateFlow()

    private val _isVisible = MutableStateFlow(false)
    val isVisible: StateFlow<Boolean> = _isVisible

    private val refreshInterval = 8 // 8 seconds

    private var job: Job? = null

    init {
        startMarketListRefresh("US") // Or pass the desired region here (One of the following is allowed US|BR|AU|CA|FR|DE|HK|IN|IT|ES|GB|SG)
        observeSearchQuery()
    }

    // re-call getMarketList every 8 seconds (only if isVisible == true which means the UI is visible for the user)
    private fun startMarketListRefresh(region: String) {
        viewModelScope.launch {
            _isVisible.collectLatest { isVisible ->
                if (isVisible) {
                    flow {
                        while (true) {
                            emit(Unit)
                            delay(refreshInterval.seconds)
                        }
                    }.collectLatest {
                        job = viewModelScope.launch {
                            getMarketList(region)
                        }
                    }
                } else {
                    job?.cancel()
                }
            }
        }
    }

    //Get the market list
    private fun getMarketList(region: String) = viewModelScope.launch {
        _loading.value = true
        getMarketListUseCase(region).collectLatest {
            when (it) {
                is BaseResult.Init -> {
                    Log.e("Init", "called")
                }

                is BaseResult.Error -> {
                    _loading.value = false
                    Log.e("Error", "called")
                }

                is BaseResult.Loading -> {
                    Log.e("Loading", "called")
                }

                is BaseResult.NoContent -> {
                    _loading.value = false
                    Log.e("NoContent", "called")
                }

                is BaseResult.Success -> {
                    _loading.value = false
                    _marketsList.value = it.data
                    filterMarketList()
                    Log.e("Success", "called")
                }
            }
        }
    }

    private fun observeSearchQuery() = viewModelScope.launch {
        searchQuery.collectLatest {
            filterMarketList()
        }
    }

    // Filter the market list locally because the api doesn't have the option to filter
    private fun filterMarketList() {
        val query = _searchQuery.value.lowercase()
        _filteredMarketList.value = if (query.isEmpty()) {
            _marketsList.value
        } else {
            _marketsList.value.filter { it.shortName.lowercase().contains(query) }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    //To keep track of the fragment state - true : fragment is visible or false if not
    fun onFragmentResumed() {
        _isVisible.value = true
    }

    fun onFragmentPaused() {
        _isVisible.value = false
    }
}


