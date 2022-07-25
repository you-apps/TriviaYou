package com.bnyro.trivia.util

import android.content.Context
import com.bnyro.trivia.R

object StatsHelper {
    fun getStats(context: Context): List<String> {
        val totalStats = PreferenceHelper.getTotalStats()
        val quizzes = PreferenceHelper.getQuizzes()

        // get library stats
        var libraryQuestions = 0
        var userCreatedQuizzesCount = 0
        var userCreatedQuestionsCount = 0
        quizzes.forEach {
            libraryQuestions += it.questions?.size!!
            if (it.creator == true) {
                userCreatedQuizzesCount += 1
                userCreatedQuestionsCount += it.questions.size
            }
        }

        return listOf(
            "${context.getString(R.string.answered_questions)}: ${totalStats.totalAnswers}",
            "${context.getString(R.string.correct_answers)}: ${totalStats.correctAnswers}",
            "${context.getString(R.string.incorrect_answers)}: ${totalStats.totalAnswers - totalStats.correctAnswers}",
            "${context.getString(R.string.percentage_correct_answers)}: ${
            if (totalStats.totalAnswers != 0) totalStats.correctAnswers / totalStats.totalAnswers * 100 else 100
            }%",
            "${context.getString(R.string.library_quizzes)}: ${quizzes.size}",
            "${context.getString(R.string.library_questions)}: $libraryQuestions",
            "${context.getString(R.string.created_quizzes)}: $userCreatedQuizzesCount",
            "${context.getString(R.string.created_questions)}: $userCreatedQuestionsCount",
            "${context.getString(R.string.downloaded_quizzes)}: ${quizzes.size - userCreatedQuizzesCount}",
            "${context.getString(R.string.downloaded_questions)}: ${libraryQuestions - userCreatedQuestionsCount}"
        )
    }
}
