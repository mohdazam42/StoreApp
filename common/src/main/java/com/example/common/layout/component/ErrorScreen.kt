package com.example.common.layout.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.common.layout.Colors
import com.example.common.layout.TypographySizes

@Composable
fun ErrorScreen(
    errorMessage: String,
    modifier: Modifier
) {
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = errorMessage,
                fontSize = TypographySizes.sp18,
                color = Colors.Black
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun PreviewErrorScreen() {
    MaterialTheme {
        Surface {
            ErrorScreen("Error Message", modifier = Modifier)
        }
    }
}