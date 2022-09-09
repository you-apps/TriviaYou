package com.bnyro.trivia.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.bnyro.trivia.R
import com.bnyro.trivia.extensions.navigate
import com.bnyro.trivia.fragments.EditQuizFragment
import com.bnyro.trivia.fragments.QuizFragment
import com.bnyro.trivia.util.BundleArguments
import com.bnyro.trivia.util.PreferenceHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class QuizOptionsDialog(
    private val libraryIndex: Int
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var options = arrayOf(
            context?.getString(R.string.restart),
            context?.getString(R.string.rename),
            context?.getString(R.string.delete)
        )

        if (PreferenceHelper.getQuizzes()[libraryIndex].creator == true) {
            options += context?.getString(
                R.string.edit
            )
        }

        val builder = MaterialAlertDialogBuilder(requireContext())
            .setItems(options) { _, index ->
                when (index) {
                    0 -> {
                        PreferenceHelper.setQuizPosition(libraryIndex, 0)
                        val quizFragment = QuizFragment()
                        val bundle = Bundle()
                        bundle.putInt(BundleArguments.quizIndex, libraryIndex)
                        quizFragment.arguments = bundle
                        requireParentFragment().parentFragmentManager.navigate(quizFragment)
                    }
                    1 -> {
                        val renameDialog = RenameDialog(libraryIndex)
                        renameDialog.show(parentFragmentManager, null)
                    }
                    2 -> {
                        val deleteDialog = DeleteDialog(libraryIndex)
                        deleteDialog.show(parentFragmentManager, null)
                    }
                    3 -> {
                        val editQuizFragment = EditQuizFragment()
                        val bundle = Bundle()
                        bundle.putInt(BundleArguments.quizIndex, libraryIndex)
                        editQuizFragment.arguments = bundle
                        requireParentFragment().parentFragmentManager.navigate(editQuizFragment)
                    }
                }
            }
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
        return builder.create()
    }
}
