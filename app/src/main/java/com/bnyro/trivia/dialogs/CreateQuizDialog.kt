package com.bnyro.trivia.dialogs

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.bnyro.trivia.R
import com.bnyro.trivia.databinding.DialogEditTextBinding
import com.bnyro.trivia.extensions.navigate
import com.bnyro.trivia.fragments.CreateQuizFragment
import com.bnyro.trivia.util.BundleArguments
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CreateQuizDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogEditTextBinding.inflate(layoutInflater)
        binding.inputLayout.hint = getString(R.string.quiz_name)

        // build the dialog
        val builder = MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.create_quiz))
            .setView(binding.root)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                if (binding.input.text.toString() != "") {
                    val createQuizFragment = CreateQuizFragment().apply {
                        arguments = bundleOf(
                            BundleArguments.quizName to binding.input.text.toString()
                        )
                    }
                    requireParentFragment().parentFragmentManager.navigate(
                        createQuizFragment,
                        false
                    )
                } else {
                    Toast.makeText(parentFragment?.context, R.string.name_empty, Toast.LENGTH_SHORT)
                        .show()
                }
            }
            .setNegativeButton(android.R.string.cancel, null)
        return builder.show()
    }
}
