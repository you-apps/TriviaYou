package com.bnyro.trivia.dialogs

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.bnyro.trivia.R
import com.bnyro.trivia.util.DialogHelper
import com.bnyro.trivia.util.PreferenceHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RenameDialog(
    private val libraryIndex: Int
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val (container, input) = DialogHelper.getTextInput(requireContext())
        val builder = MaterialAlertDialogBuilder(requireContext())
            .setTitle(context?.getString(R.string.rename))
            .setView(container)
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .setPositiveButton(android.R.string.ok) { _, _ ->
                if (input.text.toString() != "") {
                    val quiz = PreferenceHelper.getQuizzes()[libraryIndex]
                    quiz.name = input.text.toString()
                    PreferenceHelper.replaceQuizByIndex(libraryIndex, quiz)
                    findNavController().navigate(R.id.libraryFragment)
                } else {
                    Toast.makeText(context, R.string.name_empty, Toast.LENGTH_SHORT).show()
                }
            }
        return builder.show()
    }
}
