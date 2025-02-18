package com.feature.productlist.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.common.extensions.SingleValueCallback
import com.example.common.extensions.formatPrice
import com.example.common.layout.Colors
import com.example.common.layout.Dimensions
import com.example.common.layout.TypographySizes
import com.example.common.layout.component.CommonScaffold
import com.example.common.layout.component.ErrorScreen
import com.example.common.layout.component.LoadingScreen
import com.example.common.layout.component.ProductImage
import com.feature.productlist.domain.model.Product
import com.feature.productlist.domain.model.Rating
import com.feature.productlist.presentation.utils.Constants.ARROW_DESC
import com.feature.productlist.presentation.utils.Constants.PRODUCT_LIST
import com.feature.productlist.presentation.utils.Constants.RATING

@Composable
fun ProductListScreenRoute(
    viewModel: ProductListViewModel = hiltViewModel(),
    navigateToDetails: SingleValueCallback<Int>,
) {

    val productListState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        viewModel.onEvent(ProductListEvent.LoadProducts)
    }

    when (val state = productListState) {
        is ProductListState.Loading -> {
            LoadingScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimensions.dp16),
            )
        }
        is ProductListState.Error -> {
            ErrorScreen(
                errorMessage = state.message,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimensions.dp16),
                onRetry = {
                    viewModel.onEvent(ProductListEvent.OnRetry)
                }
            )
        }
        is ProductListState.Success -> {
            ProductListScreen(
                products = state.productList,
                onProductClick = { productId ->
                    viewModel.onEvent(ProductListEvent.OnProductClick(productId, navigateToDetails))
                }
            )
        }
    }
}

@Composable
fun ProductListScreen(
    products: List<Product>,
    onProductClick: SingleValueCallback<Int>
) {
    CommonScaffold(
        modifier = Modifier,
        toolbarTitle = stringResource(PRODUCT_LIST),
        navigationButtonAction = null
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Colors.White)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(Dimensions.dp16),
                verticalArrangement = Arrangement.spacedBy(Dimensions.dp12)
            ) {
                items(products) { product ->
                    ProductItem(product = product, onClick = onProductClick)
                }
            }
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    onClick: SingleValueCallback<Int>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimensions.dp8),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimensions.dp4),
        shape = RoundedCornerShape(Dimensions.dp12),
        colors = CardDefaults.cardColors(containerColor = Colors.White),
        onClick = {
            onClick(product.id)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimensions.dp12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductImage(
                productImageUrl = product.image,
                modifier = Modifier
                    .size(Dimensions.dp80)
                    .clip(
                        RoundedCornerShape(
                            bottomStart = Dimensions.dp16,
                            bottomEnd = Dimensions.dp16
                        )
                    )
            )

            Spacer(modifier = Modifier.width(Dimensions.dp12))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = product.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = TypographySizes.sp16,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = product.price.formatPrice(),
                    fontSize = TypographySizes.sp14,
                    fontWeight = FontWeight.Medium
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = stringResource(RATING),
                        tint = Colors.Yellow,
                        modifier = Modifier.size(Dimensions.dp16)
                    )

                    Spacer(modifier = Modifier.width(Dimensions.dp4))

                    Text(
                        text = "${product.rating.rate} ${stringResource(RATING)}",
                        fontSize = TypographySizes.sp12,
                        color = Colors.Gray
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = stringResource(ARROW_DESC),
                modifier = Modifier.size(Dimensions.dp24),
                tint = Colors.Black
            )
        }
    }
}


@Preview
@Composable
fun PreviewProductListScreen() {
    ProductListScreen(
        products = listOf(
            Product(
                id = 1,
                title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
                price = 109.95,
                image = "",
                rating = Rating(rate = 4.3, count = 1)
            ),
            Product(
                id = 1,
                title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
                price = 109.95,
                image = "",
                rating = Rating(rate = 4.3, count = 1)
            )
        ),
        onProductClick = {}
    )
}