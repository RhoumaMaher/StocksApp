package com.rhouma.data.market.di

import com.rhouma.data.market.remote.MarketApi
import com.rhouma.data.market.repository.MarketRepositoryImpl
import com.rhouma.domain.market.repository.MarketRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MarketModule {

    @Singleton
    @Provides
    fun provideMarketApi(retrofit: Retrofit): MarketApi {
        return retrofit.create(MarketApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMarketRepository(api: MarketApi): MarketRepository {
        return MarketRepositoryImpl(api)
    }

}