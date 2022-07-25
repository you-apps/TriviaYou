package com.bnyro.trivia.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.bnyro.trivia.R
import com.bnyro.trivia.api.TheTriviaApiHelper
import com.bnyro.trivia.util.DialogHelper
import com.bnyro.trivia.util.PreferenceHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DownloadDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val (container, input) = DialogHelper.getTextInput(requireContext())
        input.hint = context?.getString(R.string.quiz_name)

        // build the dialog
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.download)
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .setPositiveButton(android.R.string.ok) { _, _ ->
                downloadQuestions(input.text.toString())
            }
            .setView(container)
        return dialog.create()
    }

    private fun downloadQuestions(name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val questions = try {
                TheTriviaApiHelper.getQuestions(null)
            } catch (e: Exception) {
                return@launch
            }
            PreferenceHelper.saveQuiz(name, false, questions)
        }
    }
}
