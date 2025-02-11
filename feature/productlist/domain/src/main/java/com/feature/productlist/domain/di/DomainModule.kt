package com.feature.productlist.domain.di

import com.feature.productlist.domain.repository.ProductListRepository
import com.feature.productlist.domain.usecase.GetProductListUseCase
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
        productListRepository: ProductListRepository
    ): GetProductListUseCase {
        return GetProductListUseCase(productListRepository)
    }

}