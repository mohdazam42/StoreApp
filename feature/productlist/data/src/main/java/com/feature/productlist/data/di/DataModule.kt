package com.feature.productlist.data.di

import com.feature.productlist.data.remote.ProductListApiService
import com.feature.productlist.data.repository.ProductListRepositoryImpl
import com.feature.productlist.domain.repository.ProductListRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
class DataModule {

    @Provides
    @ViewModelScoped
    fun provideProductListApiService(retrofit: Retrofit): ProductListApiService {
        return retrofit.create(ProductListApiService::class.java)
    }

    @Provides
    @ViewModelScoped
    fun provideRepository(
        productListApiService: ProductListApiService
    ): ProductListRepository = ProductListRepositoryImpl(
        productListApiService = productListApiService
    )

}