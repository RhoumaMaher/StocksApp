package com.rhouma.data.stock.di

import com.rhouma.data.stock.remote.StockApi
import com.rhouma.data.stock.repository.StockRepositoryImpl
import com.rhouma.domain.stock.repository.StockRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class StockModule {

    @Singleton
    @Provides
    fun provideStockApi(retrofit: Retrofit): StockApi {
        return retrofit.create(StockApi::class.java)
    }

    @Singleton
    @Provides
    fun provideStockRepository(api: StockApi): StockRepository {
        return StockRepositoryImpl(api)
    }

}