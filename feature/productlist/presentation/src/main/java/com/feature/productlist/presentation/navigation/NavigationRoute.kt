package com.feature.productlist.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.common.animation.CustomAnimation.customFadeIn
import com.example.common.animation.CustomAnimation.customFadeOut
import com.example.common.extensions.SingleValueCallback
import com.feature.productlist.presentation.screen.ProductListScreenRoute
import kotlinx.serialization.Serializable

object NavigationRoute {
    fun NavGraphBuilder.productListScreen(
        onNavigateToDetails: SingleValueCallback<Int>
    ) {
        composable<ProductListScreen>(
            enterTransition = { customFadeIn() },
            exitTransition = { customFadeOut() }
        ) {
            ProductListScreenRoute(
                navigateToDetails = { id -> onNavigateToDetails(id) }
            )
        }
    }
}

@Serializable
data object ProductListScreen