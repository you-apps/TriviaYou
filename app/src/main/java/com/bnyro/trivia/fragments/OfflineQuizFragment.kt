package com.bnyro.trivia.fragments

import android.os.Bundle
import com.bnyro.trivia.obj.Question
import com.bnyro.trivia.util.BundleArguments
import com.bnyro.trivia.util.PreferenceHelper

class OfflineQuizFragment : QuizFragment() {
    var libraryIndex: Int = 0

    override suspend fun fetchQuestions(): List<Question> {
        val quiz = PreferenceHelper.getQuizzes()[libraryIndex]
        questionIndex = quiz.position
        return quiz.questions.orEmpty()
    }

    override suspend fun prepareNextQuestions(): Boolean {
        val hasNext = questions.size > questionIndex + 1

        PreferenceHelper.setQuizPosition(
            libraryIndex,
            if (hasNext) questionIndex + 1 else 0
        )

        return hasNext
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        libraryIndex = arguments?.getInt(BundleArguments.quizIndex, Int.MAX_VALUE)!!
    }
}
