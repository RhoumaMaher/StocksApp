package com.rhouma.data.stock.dto

data class StockInfoResponse(
    val quoteType: QuoteType,
    val symbol: String,
    val price: MarketPrice,
    val summaryDetail: SummaryDetail
)

data class QuoteType(
    val exchange: String,
    val shortName: String,
    val longName: String,
    val exchangeTimezoneName: String,
    val hasSelerityEarnings: Boolean,
    val exchangeTimezoneShortName: String,
    val isEsgPopulated: Boolean,
    val gmtOffSetMilliseconds: String,
    val quoteType: String,
    val symbol: String,
    val messageBoardId: String,
    val market: String
)

data class MarketPrice(
    val quoteSourceName: String,
    val regularMarketOpen: MarketData,
    val averageDailyVolume3Month: MarketVolume,
    val exchange: String,
    val regularMarketTime: Long,
    val volume24Hr: EmptyObject,
    val regularMarketDayHigh: MarketData,
    val shortName: String,
    val averageDailyVolume10Day: MarketVolume,
    val longName: String,
    val regularMarketChange: MarketData,
    val currencySymbol: String,
    val regularMarketPreviousClose: MarketData,
    val preMarketPrice: EmptyObject,
    val exchangeDataDelayedBy: Int,
    val toCurrency: String?,
    val postMarketChange: EmptyObject,
    val postMarketPrice: EmptyObject,
    val exchangeName: String,
    val preMarketChange: EmptyObject,
    val circulatingSupply: EmptyObject,
    val regularMarketDayLow: MarketData,
    val priceHint: MarketData,
    val currency: String,
    val regularMarketPrice: MarketData,
    val regularMarketVolume: MarketVolume,
    val lastMarket: String?,
    val regularMarketSource: String,
    val openInterest: EmptyObject,
    val marketState: String,
    val underlyingSymbol: String?,
    val marketCap: EmptyObject,
    val quoteType: String,
    val volumeAllCurrencies: EmptyObject,
    val strikePrice: EmptyObject,
    val symbol: String,
    val maxAge: Int,
    val fromCurrency: String?,
    val regularMarketChangePercent: MarketData
)

data class SummaryDetail(
    val previousClose: MarketData,
    val regularMarketOpen: MarketData,
    val twoHundredDayAverage: MarketData,
    val trailingAnnualDividendYield: EmptyObject,
    val payoutRatio: EmptyObject,
    val volume24Hr: EmptyObject,
    val regularMarketDayHigh: MarketData,
    val navPrice: EmptyObject,
    val averageDailyVolume10Day: MarketVolume,
    val totalAssets: EmptyObject,
    val regularMarketPreviousClose: MarketData,
    val fiftyDayAverage: MarketData,
    val trailingAnnualDividendRate: EmptyObject,
    val open: MarketData,
    val toCurrency: String?,
    val averageVolume10days: MarketVolume,
    val expireDate: EmptyObject,
    val yield: EmptyObject,
    val algorithm: String?,
    val dividendRate: EmptyObject,
    val exDividendDate: EmptyObject,
    val beta: EmptyObject,
    val circulatingSupply: EmptyObject,
    val startDate: EmptyObject,
    val regularMarketDayLow: MarketData,
    val priceHint: MarketData,
    val currency: String,
    val regularMarketVolume: MarketVolume,
    val lastMarket: String?,
    val maxSupply: EmptyObject,
    val openInterest: EmptyObject,
    val marketCap: EmptyObject,
    val volumeAllCurrencies: EmptyObject,
    val strikePrice: EmptyObject,
    val averageVolume: MarketVolume,
    val priceToSalesTrailing12Months: EmptyObject,
    val dayLow: MarketData,
    val ask: MarketData,
    val ytdReturn: EmptyObject,
    val askSize: EmptyObject,
    val volume: MarketVolume,
    val fiftyTwoWeekHigh: MarketData,
    val forwardPE: EmptyObject,
    val maxAge: Int,
    val fromCurrency: String?,
    val fiveYearAvgDividendYield: EmptyObject,
    val fiftyTwoWeekLow: MarketData,
    val bid: MarketData,
    val tradeable: Boolean,
    val dividendYield: EmptyObject,
    val bidSize: EmptyObject,
    val dayHigh: MarketData,
    val coinMarketCapLink: String?
)

data class MarketData(
    val raw: Float,
    val fmt: String,
    val longFmt: String?
)

data class MarketVolume(
    val raw: Long,
    val fmt: String,
    val longFmt: String
)

data class EmptyObject(
    val raw: Any? = null,
    val fmt: Any? = null,
    val longFmt: Any? = null
)
