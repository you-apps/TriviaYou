package com.bnyro.trivia.util

import java.util.*

fun Any?.formatStats(): String {
    return this!!.toString()
        .replace("=", ": ")
        .replace("_", " ")
        .replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault())
            else it.toString()
        }
}
