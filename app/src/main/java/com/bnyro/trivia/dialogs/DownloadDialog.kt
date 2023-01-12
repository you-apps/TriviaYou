package com.bnyro.trivia.dialogs

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.bnyro.trivia.R
import com.bnyro.trivia.databinding.DialogDownloadBinding
import com.bnyro.trivia.obj.Quiz
import com.bnyro.trivia.util.ApiHelper
import com.bnyro.trivia.util.ApiInstance
import com.bnyro.trivia.util.PreferenceHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DownloadDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogDownloadBinding.inflate(layoutInflater)
        binding.input.hint = getString(R.string.quiz_name)

        val categoryNames = mutableListOf(getString(R.string.none))
        val categoryQueries = mutableListOf("")

        lifecycleScope.launchWhenCreated {
            ApiInstance.apiHelper.getCategories().forEach {
                categoryNames += it.name!!
                categoryQueries += it.id!!
            }
            binding.spinner.adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item,
                categoryNames
            )
        }

        // build the dialog
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.download)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                val name = binding.input.text.toString()
                if (name != "") {
                    val position = binding.spinner.selectedItemPosition
                    val category = if (position == 0) null else categoryQueries[position]
                    downloadQuestions(binding.input.text.toString(), category)
                } else {
                    Toast.makeText(context, R.string.name_empty, Toast.LENGTH_SHORT).show()
                }
            }
            .setView(binding.root)
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
