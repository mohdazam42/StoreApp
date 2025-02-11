package com.example.storeapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.common.layout.TypographySizes

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = TypographySizes.sp16,
        lineHeight = TypographySizes.sp24,
        letterSpacing = TypographySizes.sp05
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = TypographySizes.sp22,
        lineHeight = TypographySizes.sp28,
        letterSpacing = TypographySizes.sp0
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = TypographySizes.sp11,
        lineHeight = TypographySizes.sp16,
        letterSpacing = TypographySizes.sp05
    )
)