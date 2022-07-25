package com.bnyro.trivia.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.bnyro.trivia.R
import com.bnyro.trivia.util.PreferenceHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class QuizOptionsDialog(
    private val libraryIndex: Int
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val options = arrayOf(
            context?.getString(R.string.delete)
        )

        val builder = MaterialAlertDialogBuilder(requireContext())
            .setItems(options) { _, index ->
                when (index) {
                    0 -> {
                        PreferenceHelper.deleteQuiz(libraryIndex)
                        findNavController().navigate(R.id.libraryFragment)
                    }
                }
            }
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
        return builder.create()
    }
}
