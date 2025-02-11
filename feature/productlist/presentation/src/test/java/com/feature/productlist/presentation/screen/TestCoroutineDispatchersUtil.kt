package com.feature.productlist.presentation.screen

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
val testDispatcher = UnconfinedTestDispatcher()

fun runUnconfinedTest(block: suspend (TestScope) -> Unit) = runTest(testDispatcher) { block(this) }