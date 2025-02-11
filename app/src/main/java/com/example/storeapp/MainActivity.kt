package com.example.storeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.storeapp.navigation.AppNavigation
import com.example.storeapp.ui.theme.StoreAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppEntryPoint()
        }
    }
}

@Composable
private fun AppEntryPoint() {
    StoreAppTheme {
        Scaffold(
            modifier = Modifier
                .safeDrawingPadding()
                .fillMaxSize()
        ) { innerPadding ->
            AppNavigation(paddingValues = innerPadding)
        }
    }
}