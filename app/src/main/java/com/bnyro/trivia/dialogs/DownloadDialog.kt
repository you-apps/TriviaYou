package com.bnyro.trivia.dialogs

import android.app.Dialog
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.bnyro.trivia.R
import com.bnyro.trivia.util.PreferenceHelper
import com.bnyro.trivia.util.RetrofitInstance
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DownloadDialog: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // build the view of the dialog
        val container = LinearLayout(requireContext())
        container.orientation = LinearLayout.VERTICAL
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lp.setMargins(60, 0, 60, 0)
        val input = EditText(requireContext())
        input.layoutParams = lp
        input.inputType = InputType.TYPE_CLASS_TEXT
        container.addView(input, lp)

        // build the dialog
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.download)
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .setPositiveButton(android.R.string.ok) { _, _ ->
                downloadQuestions(input.text.toString())
            }
        return dialog.create()
    }

    private fun downloadQuestions(name: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val questions = try {
                RetrofitInstance.api.getQuestions(
                    PreferenceHelper.getLimit(),
                    null,
                    PreferenceHelper.getDifficultyQuery()
                )
            } catch (e: Exception) {
                return@launch
            }
            PreferenceHelper.saveQuiz(name, false, questions)
        }
    }
}