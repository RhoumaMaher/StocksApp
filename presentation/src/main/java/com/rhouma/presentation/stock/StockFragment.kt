package com.rhouma.presentation.stock

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.rhouma.presentation.R
import com.rhouma.presentation.databinding.FragmentStockBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StockFragment : Fragment(R.layout.fragment_stock) {

    private var _binding: FragmentStockBinding? = null
    private val binding get() = _binding!!
    internal val viewModel: StockViewModel by viewModels()
    private var loadingDialog: Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStockBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Show loading indicator
        showLoadingIndicator(true)
        //Retrieve navigation argument
        getArgument()
        //Observe data and display it
        observeStockInfo()
        //Handle back button
        handleBackButton()
    }

    private fun getArgument() {

        arguments?.let {
            val safeArgs = StockFragmentArgs.fromBundle(it)
            safeArgs.symbol?.let { symbol ->
                viewModel.getStockInfo(
                    region = "US",
                    symbol = symbol
                )
            }
        }

    }

    private fun observeStockInfo() {
        lifecycleScope.launch {
            viewModel.stockInfo.collectLatest { stockInfo ->
                if (stockInfo != null) {

                    binding.apply {
                        tvSymbol.text = stockInfo.symbol
                        tvShortName.text = stockInfo.shortName
                        tvRegularMarketPrice.text = stockInfo.regularMarketPrice.toString()
                        tvRegularMarketChange.text = stockInfo.regularMarketChange.toString()
                        tvRegularMarketChangePercent.text = "${stockInfo.regularMarketChangePercent}%"
                        tvRegularMarketVolume.text = stockInfo.regularMarketVolume.toString()
                        showLoadingIndicator(false)
                        clDataContainer.visibility = VISIBLE
                    }

                }
            }
        }
    }

    private fun handleBackButton() {
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun showLoadingIndicator(show: Boolean) {
        if (show) {
            if (loadingDialog == null) {
                loadingDialog = Dialog(requireContext()).apply {
                    val view = LayoutInflater.from(requireContext()).inflate(R.layout.loading_indicator_layout, null)
                    setContentView(view)
                    window?.setBackgroundDrawableResource(android.R.color.transparent)
                    setCancelable(false)
                    setCanceledOnTouchOutside(false)
                }
            }
            loadingDialog?.show()
        } else {
            loadingDialog?.takeIf { it.isShowing }?.dismiss()
        }
    }

}