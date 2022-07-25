package com.bnyro.trivia.util

import android.content.Context
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout

object DialogHelper {
    fun getTextInput(context: Context): Pair<LinearLayout, EditText> {
        // build the view of the dialog
        val container = LinearLayout(context)
        container.orientation = LinearLayout.VERTICAL
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lp.setMargins(60, 30, 60, 0)
        val input = EditText(context)
        input.layoutParams = lp
        input.inputType = InputType.TYPE_CLASS_TEXT
        container.addView(input, lp)

        return Pair(container, input)
    }
}
