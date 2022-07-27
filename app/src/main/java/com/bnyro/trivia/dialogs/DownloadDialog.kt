package com.bnyro.trivia.dialogs

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.bnyro.trivia.R
import com.bnyro.trivia.obj.Quiz
import com.bnyro.trivia.util.ApiHelper
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

        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lp.setMargins(60, 30, 60, 0)

        val categoryNames = mutableListOf(context?.getString(R.string.none)!!)
        val categoryQueries = mutableListOf("")

        val spinner = Spinner(requireContext())
        lifecycleScope.launchWhenCreated {
            val categories = ApiHelper().getCategories()

            categories.forEach {
                categoryNames += it.name!!
                categoryQueries += it.id!!
            }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryNames)
            spinner.adapter = adapter
        }

        container.addView(spinner, lp)

        // build the dialog
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.download)
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .setPositiveButton(android.R.string.ok) { _, _ ->
                val position = spinner.selectedItemPosition
                val category = if (position == 0) null else categoryQueries[position]
                downloadQuestions(input.text.toString(), category)
            }
            .setView(container)
        return dialog.create()
    }

    private fun downloadQuestions(name: String, category: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            Log.e("category", category.toString())
            val questions = try {
                ApiHelper().getQuestions(category)
            } catch (e: Exception) {
                return@launch
            }
            val quiz = Quiz(
                name = name,
                creator = false,
                questions = questions
            )
            PreferenceHelper.saveQuiz(quiz)
        }
    }
}
