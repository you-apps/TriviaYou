package com.bnyro.trivia.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bnyro.trivia.R

fun FragmentManager?.navigate(
    fragment: Fragment,
    addToBackStack: Boolean = true
) {
    val ft = this!!.beginTransaction()
        .replace(R.id.fragment, fragment)

    if (addToBackStack) ft.addToBackStack(null)
    ft.commit()
}
