package com.rhouma.presentation.stock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rhouma.domain.common.base_result.BaseResult
import com.rhouma.domain.stock.model.StockInfoModel
import com.rhouma.domain.stock.usecase.GetStockInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StockViewModel @Inject constructor(private val getStockInfoUseCase: GetStockInfoUseCase) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _stockInfo = MutableStateFlow<StockInfoModel?>(null)
    val stockInfo = _stockInfo.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun getStockInfo(region: String, symbol: String) {
        viewModelScope.launch {
            getStockInfoUseCase(region, symbol).collect { result ->
                when (result) {
                    is BaseResult.Loading -> {
                        _loading.value = true
                    }
                    is BaseResult.Success -> {
                        _loading.value = false
                        _stockInfo.value = result.data
                    }
                    is BaseResult.Error -> {
                        _loading.value = false
                        _error.value = result.rawResponse
                    }

                    else -> {}
                }
            }
        }
    }
}
