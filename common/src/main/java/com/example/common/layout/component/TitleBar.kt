package com.example.common.layout.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.common.extensions.EmptyCallback
import com.example.common.utils.BACK_ARROW


@Composable
fun CommonScaffold(
    modifier: Modifier = Modifier,
    toolbarTitle: String,
    navigationButtonAction: EmptyCallback? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TitleBar(
                toolbarTitle = toolbarTitle,
                navigationButtonAction = navigationButtonAction
            )
        },
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleBar(
    toolbarTitle: String,
    modifier: Modifier = Modifier,
    navigationButtonAction: EmptyCallback? = null,
    actions: @Composable RowScope.() -> Unit = {},
) = TopAppBar(
    modifier = modifier,
    colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        titleContentColor = MaterialTheme.colorScheme.primary,
    ),
    navigationIcon = {
        navigationButtonAction?.let {
            IconButton(onClick = it) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = BACK_ARROW
                )
            }
        }
    },
    actions = actions,
    title = {
        Text(text = toolbarTitle)
    }
)