package com.example.common.layout.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.common.layout.Dimensions
import com.example.common.layout.TypographySizes

@Composable
fun ErrorScreen(errorMessage: String) {
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
            Text(
                text = errorMessage,
                fontSize = TypographySizes.sp18,
                color = Color.Black
            )
        }
    }
}