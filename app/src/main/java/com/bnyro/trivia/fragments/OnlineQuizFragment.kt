package com.bnyro.trivia.fragments

import android.os.Bundle
import android.util.Log
import com.bnyro.trivia.R
import com.bnyro.trivia.extensions.showStyledSnackBar
import com.bnyro.trivia.obj.Question
import com.bnyro.trivia.util.ApiInstance
import com.bnyro.trivia.util.BundleArguments
import com.bnyro.trivia.util.PreferenceHelper

class OnlineQuizFragment : QuizFragment() {
    private var category: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        category = arguments?.getString(BundleArguments.category)
    }

    override suspend fun fetchQuestions(): List<Question> {
        return try {
            ApiInstance.apiHelper.getQuestions(category)
        } catch (e: Exception) {
            Log.e(this::class.java.simpleName, e.toString())
            binding.root.showStyledSnackBar(R.string.network_error)
            listOf()
        }
    }

    override suspend fun prepareNextQuestions(): Boolean {
        if (questions.size > questionIndex + 1) return true

        if (PreferenceHelper.isUnlimitedMode()) {
            val newQuestions = fetchQuestions()
            questions += newQuestions
            return newQuestions.isNotEmpty()
        }

        return false
    }
}
