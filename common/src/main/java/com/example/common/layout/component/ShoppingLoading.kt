package com.example.common.layout.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.common.layout.Dimensions
import com.example.common.layout.TypographySizes
import com.example.common.utils.FETCHING_DATA

@Composable
fun FullScreenCircularLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimensions.dp16)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(
                color = Color.Blue,
                modifier = Modifier.size(Dimensions.dp50)
            )
            Spacer(modifier = Modifier.height(Dimensions.dp16))
            Text(
                text = FETCHING_DATA,
                fontSize = TypographySizes.sp18,
                color = Color.Black
            )
        }
    }
}

@Preview
@Composable
private fun FullScreenCircularLoadingPreview() {
    MaterialTheme {
        Surface {
            FullScreenCircularLoading()
        }
    }
}