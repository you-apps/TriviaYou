package com.bnyro.trivia.extensions

import android.text.Spanned
import androidx.core.text.HtmlCompat

fun Any?.toHTML(): Spanned {
    return HtmlCompat.fromHtml(this.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
}
