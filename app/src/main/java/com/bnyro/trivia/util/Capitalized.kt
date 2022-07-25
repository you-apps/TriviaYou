package com.bnyro.trivia.util

import java.util.*

fun String?.capitalized(): String {
    return this!!.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault())
        else it.toString()
    }
}
