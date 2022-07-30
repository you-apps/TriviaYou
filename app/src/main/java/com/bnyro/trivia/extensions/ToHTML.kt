package com.bnyro.trivia.extensions

import android.text.Html
import android.text.Spanned

fun Any?.toHTML(): Spanned {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(this.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this.toString())
    }
}
