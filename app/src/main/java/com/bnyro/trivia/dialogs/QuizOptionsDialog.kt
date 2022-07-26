package com.bnyro.trivia.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.bnyro.trivia.R
import com.bnyro.trivia.fragments.EditQuizFragment
import com.bnyro.trivia.util.BundleArguments
import com.bnyro.trivia.util.PreferenceHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class QuizOptionsDialog(
    private val libraryIndex: Int
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var options = arrayOf(
            context?.getString(R.string.delete)
        )

        if (PreferenceHelper.getQuizzes()[libraryIndex].creator == true) options += context?.getString(R.string.edit)

        val builder = MaterialAlertDialogBuilder(requireContext())
            .setItems(options) { _, index ->
                when (index) {
                    0 -> {
                        PreferenceHelper.deleteQuiz(libraryIndex)
                        findNavController().navigate(R.id.libraryFragment)
                    }
                    1 -> {
                        val editQuizFragment = EditQuizFragment()
                        val bundle = Bundle()
                        bundle.putInt(BundleArguments.quizIndex, libraryIndex)
                        editQuizFragment.arguments = bundle
                        parentFragment?.parentFragmentManager!!.beginTransaction()
                            .replace(R.id.fragment, editQuizFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                }
            }
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
        return builder.create()
    }
}
