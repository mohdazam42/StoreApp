package com.feature.productdetail.domain.di

import com.feature.productdetail.domain.repository.ProductDetailRepository
import com.feature.productdetail.domain.usecase.GetProductDetailUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object DomainModule {

    @Provides
    @ViewModelScoped
    fun provideGetProductListUseCase(
        productDetailRepository: ProductDetailRepository
    ): GetProductDetailUseCase {
        return GetProductDetailUseCase(productDetailRepository)
    }

}