package com.example.common.layout.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.common.utils.PRODUCT_IMAGE_DESC

@Composable
fun ProductImage(
    productImageUrl: String,
    modifier: Modifier
) {
    AsyncImage(
        model =
        ImageRequest.Builder(LocalContext.current)
            .data(productImageUrl)
            .crossfade(true)
            .build(),
        contentDescription = PRODUCT_IMAGE_DESC,
        modifier = modifier
    )
}