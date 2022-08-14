package com.bnyro.trivia.dialogs

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bnyro.trivia.R
import com.bnyro.trivia.extensions.navigate
import com.bnyro.trivia.fragments.CreateQuizFragment
import com.bnyro.trivia.util.BundleArguments
import com.bnyro.trivia.util.DialogHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CreateQuizDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val (container, input) = DialogHelper.getTextInput(requireContext())
        input.hint = context?.getString(R.string.quiz_name)

        // build the dialog
        val builder = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.create_quiz))
            .setView(container)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                if (input.text.toString() != "") {
                    val createQuizFragment = CreateQuizFragment()
                    val bundle = Bundle()
                    bundle.putString(BundleArguments.quizName, input.text.toString())
                    createQuizFragment.arguments = bundle
                    requireParentFragment().parentFragmentManager.navigate(
                        createQuizFragment,
                        false
                    )
                } else {
                    Toast.makeText(parentFragment?.context, R.string.name_empty, Toast.LENGTH_SHORT)
                        .show()
                }
            }
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
        return builder.show()
    }
}
