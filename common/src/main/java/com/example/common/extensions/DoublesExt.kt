package com.example.common.extensions

import java.util.Locale

fun Double.formatPrice(): String {
    return String.format(Locale.US, "$%.2f", this)
}