package com.bnyro.trivia.extensions

import android.view.View
import android.widget.FrameLayout
import androidx.core.content.res.ResourcesCompat
import com.bnyro.trivia.R
import com.google.android.material.snackbar.Snackbar

fun View.showSnackBar(text: Int) {
    val snackBar = Snackbar.make(this, text, Snackbar.LENGTH_SHORT)
    snackBar.setTextMaxLines(3)
    snackBar.animationMode = Snackbar.ANIMATION_MODE_SLIDE

    val params = snackBar.view.layoutParams as FrameLayout.LayoutParams
    val sideMargin = 70

    params.setMargins(
        sideMargin,
        params.topMargin,
        sideMargin,
        sideMargin + 50
    )
    snackBar.view.layoutParams = params

    snackBar.view.background = ResourcesCompat.getDrawable(
        resources,
        R.drawable.snackbar_shape,
        null
    )
    snackBar.show()
}
