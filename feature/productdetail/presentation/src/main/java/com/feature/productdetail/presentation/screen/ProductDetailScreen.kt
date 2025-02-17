package com.feature.productdetail.presentation.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.common.extensions.EmptyCallback
import com.example.common.layout.Colors
import com.example.common.layout.Dimensions
import com.example.common.layout.TypographySizes
import com.example.common.layout.component.ErrorScreen
import com.example.common.layout.component.LoadingScreen
import com.example.common.layout.component.ProductImage
import com.example.common.layout.component.TitleBar
import com.feature.productdetail.domain.model.Product
import com.feature.productdetail.domain.model.Rating
import com.feature.productdetail.presentation.utils.DESCRIPTION
import com.feature.productdetail.presentation.utils.RATING
import com.feature.productdetail.presentation.utils.REVIEWS

@Composable
fun ProductDetailScreenRoute(
    viewModel: ProductDetailViewModel = hiltViewModel(),
    productId: Int,
    navigateBack: EmptyCallback
) {
    val productState by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        viewModel.onEvent(ProductDetailEvent.LoadProduct(productId = productId))
    }

    with(productState) {
        if (error.isNotBlank()) {
            ErrorScreen(
                error,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimensions.dp16)
                )
        }
        if (isLoading) {
            LoadingScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimensions.dp16),
            )
        }
        if (product.id != 0) {
            ProductDetailScreen(
                product = product,
                navigateBack = {
                    viewModel.onEvent(ProductDetailEvent.OnNavigateBack(navigateBack))
                }
            )
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun ProductDetailScreen(
    product: Product,
    navigateBack: EmptyCallback = {}
) {
    Scaffold(
        topBar = {
            TitleBar(
                toolbarTitle = product.category,
                navigationButtonAction = navigateBack
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Colors.White)
        ) {
            // Main content that scrolls
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = Dimensions.dp120)
            ) {
                ProductImage(
                    productImageUrl = product.image,
                    modifier = Modifier
                        .padding(top = Dimensions.dp16)
                        .fillMaxWidth()
                        .height(Dimensions.dp300)
                        .clip(
                            RoundedCornerShape(
                                bottomStart = Dimensions.dp16,
                                bottomEnd = Dimensions.dp16
                            )
                        )
                )

                Spacer(modifier = Modifier.height(Dimensions.dp40))

                Column(
                    modifier = Modifier.padding(Dimensions.dp16)
                ) {
                    Text(
                        text = product.title,
                        fontSize = TypographySizes.sp22,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(Dimensions.dp6))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = product.title,
                            fontSize = TypographySizes.sp16,
                            color = Colors.Gray,
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(Dimensions.dp6))

                        Text(
                            text = String.format("$%.2f", product.price),
                            fontSize = TypographySizes.sp18,
                            color = Colors.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(Dimensions.dp6))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = RATING,
                            tint = Colors.Yellow,
                            modifier = Modifier.size(Dimensions.dp20)
                        )

                        Spacer(modifier = Modifier.width(Dimensions.dp6))

                        Text(
                            text = "${product.rating.rate} $RATING",
                            fontSize = TypographySizes.sp16,
                            color = Colors.Gray,
                        )

                        Spacer(modifier = Modifier.width(Dimensions.dp2))

                        Text(
                            text = "(${product.rating.count} $REVIEWS)",
                            fontSize = TypographySizes.sp18,
                            color = Colors.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(Dimensions.dp12))

                    Text(
                        text = DESCRIPTION,
                        fontSize = TypographySizes.sp18,
                        fontWeight = FontWeight.SemiBold,
                        color = Colors.Black
                    )

                    Spacer(modifier = Modifier.height(Dimensions.dp8))

                    Text(
                        text = product.description,
                        fontSize = TypographySizes.sp18,
                        color = Colors.Gray,
                        lineHeight = TypographySizes.sp22
                    )

                    Spacer(modifier = Modifier.height(Dimensions.dp22))
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewProductDetailScreen() {
    ProductDetailScreen(
        Product(
            id = 1,
            title = "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
            price = 109.95,
            image = "",
            rating = Rating(rate = 4.3, count = 1),
            description = "",
            category = ""
        )
    )
}