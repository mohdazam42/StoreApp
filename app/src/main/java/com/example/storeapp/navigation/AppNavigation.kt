package com.example.storeapp.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.feature.productdetail.presentation.navigation.NavigationRoute.productDetailsScreen
import com.feature.productdetail.presentation.navigation.ProductDetailsScreen
import com.feature.productlist.presentation.navigation.NavigationRoute.productListScreen
import com.feature.productlist.presentation.navigation.ProductListScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = ProductListScreen,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        productListScreen(
            onNavigateToDetails = { productId ->
                navController.navigate(ProductDetailsScreen(productId = productId))
            }
        )
        productDetailsScreen(onBackAction = { navController.popBackStack() })
    }
}