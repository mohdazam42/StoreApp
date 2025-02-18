package com.example.common.extensions

import java.util.Locale

fun Double.formatPrice(): String = String.format(Locale.US, "$%.2f", this)