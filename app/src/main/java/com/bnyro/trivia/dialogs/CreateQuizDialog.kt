package com.bnyro.trivia.dialogs

import android.app.Dialog
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bnyro.trivia.R
import com.bnyro.trivia.fragments.CreateQuizFragment
import com.bnyro.trivia.util.BundleArguments
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CreateQuizDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // build the view of the dialog
        val container = LinearLayout(requireContext())
        container.orientation = LinearLayout.VERTICAL
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lp.setMargins(60, 30, 60, 0)
        val input = EditText(requireContext())
        input.layoutParams = lp
        input.inputType = InputType.TYPE_CLASS_TEXT
        container.addView(input, lp)

        // build the dialog
        val builder = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.quiz_name))
            .setView(container)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                if (input.text.toString() != "") {
                    val createQuizFragment = CreateQuizFragment()
                    val bundle = Bundle()
                    bundle.putString(BundleArguments.quizName, input.text.toString())
                    createQuizFragment.arguments = bundle
                    parentFragment?.parentFragmentManager!!.beginTransaction()
                        .replace(R.id.fragment, createQuizFragment)
                        .addToBackStack(null)
                        .commit()
                } else {
                    Toast.makeText(parentFragment?.context, R.string.name_empty, Toast.LENGTH_SHORT)
                        .show()
                }
            }
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
        return builder.show()
    }
}
