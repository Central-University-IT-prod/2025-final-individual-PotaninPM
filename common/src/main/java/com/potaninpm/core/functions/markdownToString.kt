package com.potaninpm.core.functions

import androidx.compose.ui.text.AnnotatedString

fun markdownToString(str: String): AnnotatedString {
    return AnnotatedString(str.replace("**", ""))
}


