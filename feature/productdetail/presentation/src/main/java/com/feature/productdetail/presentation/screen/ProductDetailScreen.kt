package com.feature.productdetail.presentation.screen

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.common.extensions.EmptyCallback
import com.example.common.extensions.formatPrice
import com.example.common.layout.Colors
import com.example.common.layout.Dimensions
import com.example.common.layout.TypographySizes
import com.example.common.layout.component.CommonScaffold
import com.example.common.layout.component.ErrorScreen
import com.example.common.layout.component.LoadingScreen
import com.example.common.layout.component.ProductImage
import com.feature.productdetail.domain.model.Product
import com.feature.productdetail.domain.model.Rating
import com.feature.productdetail.presentation.utils.Constants.DESCRIPTION
import com.feature.productdetail.presentation.utils.Constants.RATING
import com.feature.productdetail.presentation.utils.Constants.REVIEWS

@Composable
fun ProductDetailScreenRoute(
    viewModel: ProductDetailViewModel = hiltViewModel(),
    productId: Int,
    navigateBack: EmptyCallback
) {
    val productState by viewModel.state.collectAsStateWithLifecycle()

    val isDataLoaded = rememberSaveable { mutableStateOf(false) }

    if (!isDataLoaded.value) {
        LaunchedEffect(Unit) {
            viewModel.onEvent(ProductDetailEvent.LoadProduct(productId = productId))
            isDataLoaded.value = true
        }
    }

    when(val state = productState) {
        is ProductDetailState.Loading -> {
            LoadingScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimensions.dp16),
            )
        }
        is ProductDetailState.Error -> {
            ErrorScreen(
                errorMessage = state.message,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Dimensions.dp16),
                onRetry = {
                    viewModel.onEvent(ProductDetailEvent.OnRetry(productId = productId))
                }
            )
        }
        is ProductDetailState.Success -> {
            ProductDetailScreen(
                product = state.product,
                navigateBack = {
                    viewModel.onEvent(ProductDetailEvent.OnNavigateBack(navigateBack))
                }
            )
        }
    }
}

@Composable
fun ProductDetailScreen(
    product: Product,
    navigateBack: EmptyCallback = {}
) {
    CommonScaffold(
        modifier = Modifier,
        toolbarTitle = product.category,
        navigationButtonAction = navigateBack
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

                    Spacer(modifier = Modifier.height(Dimensions.dp6))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = stringResource(RATING),
                                tint = Colors.Yellow,
                                modifier = Modifier.size(Dimensions.dp20)
                            )

                            Spacer(modifier = Modifier.width(Dimensions.dp6))

                            Text(
                                text = "${product.rating.rate} ${stringResource(RATING)}",
                                fontSize = TypographySizes.sp16,
                                color = Colors.Gray,
                            )

                            Spacer(modifier = Modifier.width(Dimensions.dp2))

                            Text(
                                text = "(${product.rating.count} ${stringResource(REVIEWS)}",
                                fontSize = TypographySizes.sp18,
                                color = Colors.Gray
                            )
                        }

                        Text(
                            text = product.price.formatPrice(),
                            fontSize = TypographySizes.sp18,
                            color = Colors.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(Dimensions.dp12))

                    Text(
                        text = stringResource(DESCRIPTION),
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