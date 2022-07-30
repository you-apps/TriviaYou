package com.bnyro.trivia.extensions

fun String?.wordCount(): Int {
    return this!!
        .trim()
        .split(
            "\\s+"
                .toRegex()
        ).size
}
