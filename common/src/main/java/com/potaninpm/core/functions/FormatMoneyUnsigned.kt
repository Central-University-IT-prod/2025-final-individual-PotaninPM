package com.potaninpm.core.functions

import java.text.NumberFormat
import java.util.Locale

fun formatMoneyUnsigned(value: Long): String {
    val numberFormat = NumberFormat.getInstance(Locale("ru", "RU"))
    val formattedValue = numberFormat.format(value)

    return formattedValue
}
