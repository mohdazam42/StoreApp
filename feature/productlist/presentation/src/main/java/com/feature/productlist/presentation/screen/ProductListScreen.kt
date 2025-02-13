package com.feature.productlist.presentation.screen

import android.annotation.SuppressLint
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.common.extensions.SingleValueCallback
import com.example.common.layout.Dimensions
import com.example.common.layout.TypographySizes
import com.example.common.layout.component.ErrorScreen
import com.example.common.layout.component.FullScreenCircularLoading
import com.example.common.layout.component.ProductImage
import com.example.common.layout.component.TitleBar
import com.feature.productlist.domain.model.Product
import com.feature.productlist.domain.model.Rating
import com.feature.productlist.presentation.utils.ARROW_DESC
import com.feature.productlist.presentation.utils.PRODUCT_LIST
import com.feature.productlist.presentation.utils.RATING

@Composable
fun ProductListScreenRoute(
    viewModel: ProductListViewModel = hiltViewModel(),
    navigateToDetails: SingleValueCallback<Int>,
) {

    val productListState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        viewModel.onEvent(ProductListEvent.LoadProducts)
    }

    with(productListState) {
        if (error.isNotBlank()) {
            ErrorScreen(error)
        }
        if (isLoading) {
            FullScreenCircularLoading()
        }
        if (productList.isNotEmpty()) {
            ProductListScreen(
                products = productList,
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
    Scaffold(
        topBar = {
            TitleBar(
                toolbarTitle = PRODUCT_LIST,
                navigationButtonAction = null
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
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

@SuppressLint("DefaultLocale")
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
        colors = CardDefaults.cardColors(containerColor = Color.White),
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
                    text = String.format("$%.2f", product.price),
                    fontSize = TypographySizes.sp14,
                    fontWeight = FontWeight.Medium
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = RATING,
                        tint = Color.Yellow,
                        modifier = Modifier.size(Dimensions.dp16)
                    )

                    Spacer(modifier = Modifier.width(Dimensions.dp4))

                    Text(
                        text = "${product.rating.rate} $RATING",
                        fontSize = TypographySizes.sp12,
                        color = Color.Gray
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = ARROW_DESC,
                modifier = Modifier.size(Dimensions.dp24),
                tint = Color.Black
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