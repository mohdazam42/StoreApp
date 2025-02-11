package com.feature.productdetail.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.common.animation.CustomAnimation.customFadeIn
import com.example.common.animation.CustomAnimation.customFadeOut
import com.example.common.extensions.EmptyCallback
import com.feature.productdetail.presentation.screen.ProductDetailScreenRoute
import kotlinx.serialization.Serializable

object NavigationRoute {
    fun NavGraphBuilder.productDetailsScreen(onBackAction: EmptyCallback) {
        composable<ProductDetailsScreen>(
            enterTransition = { customFadeIn() },
            exitTransition = { customFadeOut() }
        ) { backStackEntry ->
            val args = backStackEntry.toRoute<ProductDetailsScreen>()
            val productId = args.productId
            if (productId > 0) {
                ProductDetailScreenRoute(
                    productId = productId,
                    navigateBack = onBackAction
                )
            }
        }
    }
}

@Serializable
data class ProductDetailsScreen(val productId: Int)