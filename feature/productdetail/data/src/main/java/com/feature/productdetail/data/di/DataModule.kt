package com.feature.productdetail.data.di

import com.feature.productdetail.data.remote.ProductDetailApiService
import com.feature.productdetail.data.repository.ProductDetailRepositoryImpl
import com.feature.productdetail.domain.repository.ProductDetailRepository
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
    fun provideProductDetailApiService(retrofit: Retrofit): ProductDetailApiService {
        return retrofit.create(ProductDetailApiService::class.java)
    }

    @Provides
    @ViewModelScoped
    fun provideRepository(
        productDetailApiService: ProductDetailApiService
    ): ProductDetailRepository = ProductDetailRepositoryImpl(
        productDetailApiService = productDetailApiService
    )
}