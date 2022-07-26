package com.bnyro.trivia.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bnyro.trivia.R
import com.bnyro.trivia.databinding.FragmentHomeBinding
import com.bnyro.trivia.obj.Question
import com.bnyro.trivia.obj.QuizType
import com.bnyro.trivia.util.ApiHelper
import com.bnyro.trivia.util.BundleArguments
import com.bnyro.trivia.util.PreferenceHelper
import com.bnyro.trivia.util.ThemeHelper
import com.bnyro.trivia.util.toHTML
import kotlinx.coroutines.delay

class QuizFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var optionButtons: List<Button>
    private lateinit var questions: List<Question>
    private var questionIndex = 0

    private lateinit var answers: ArrayList<String>
    private var totalAnswersCount = 0
    private var correctAnswerCount = 0

    private var buttonTextColor = 0

    private var category: String? = null

    private var quizType: Int = -1
    private var libraryIndex: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        category = arguments?.getString(BundleArguments.category)

        // circumvent 0 being returned although it's null
        libraryIndex = arguments?.getInt(BundleArguments.libraryIndex, Int.MAX_VALUE)
        if (libraryIndex == Int.MAX_VALUE) libraryIndex = null

        quizType = if (libraryIndex != null) QuizType.OFFLINE else QuizType.ONLINE
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        optionButtons = mutableListOf(
            binding.optionA,
            binding.optionB,
            binding.optionC,
            binding.optionD
        )

        buttonTextColor = binding.optionA.currentTextColor

        if (quizType == QuizType.OFFLINE) {
            binding.progress.visibility = View.GONE
            binding.questionLL.visibility = View.VISIBLE
            questions = PreferenceHelper.getQuizzes()[libraryIndex!!].questions!!
            loadQuestion()
        } else {
            fetchQuestions()
        }
    }

    private fun fetchQuestions() {
        lifecycleScope.launchWhenCreated {
            questions = try {
                ApiHelper().getQuestions(category)
            } catch (e: Exception) {
                return@launchWhenCreated
            }
            loadQuestion()
        }
    }

    private fun loadQuestion() {
        val question = questions[questionIndex]
        binding.questionTV.text = question.question.toHTML()

        answers = arrayListOf(question.correctAnswer!!)
        // append all incorrect answers
        question.incorrectAnswers?.forEach {
            answers += it
        }
        answers.shuffle()

        val tempOptionButtons = optionButtons.toMutableList()
        tempOptionButtons.forEachIndexed { _, button ->
            // reset button style
            button.setTextColor(buttonTextColor)
            button.setBackgroundColor(Color.TRANSPARENT)
            button.visibility = View.VISIBLE
        }

        if (answers.size <= 3) tempOptionButtons.removeAt(0)
        if (answers.size <= 2) tempOptionButtons.removeAt(2)

        optionButtons.forEach {
            if (!tempOptionButtons.contains(it)) it.visibility = View.INVISIBLE
        }

        tempOptionButtons.forEachIndexed { index, button ->
            // set text and onclick listener
            val answer = answers[index]
            button.text = answer.toHTML()
            button.setOnClickListener {
                checkAnswer(index, tempOptionButtons)
            }
        }

        binding.progress.visibility = View.GONE
        binding.questionLL.visibility = View.VISIBLE
    }

    private fun checkAnswer(selectedAnswerIndex: Int, tempOptionButtons: List<Button>) {
        val correctAnswerIndex = answers.indexOf(questions[questionIndex].correctAnswer)
        val isAnswerCorrect = correctAnswerIndex == selectedAnswerIndex

        val secondaryColor = ThemeHelper.getThemeColor(requireContext(), android.R.attr.colorAccent)
        val colorError = ThemeHelper.getThemeColor(requireContext(), com.google.android.material.R.attr.colorError)
        val textColor = ThemeHelper.getThemeColor(requireContext(), android.R.attr.colorBackground)

        tempOptionButtons[correctAnswerIndex].apply {
            setTextColor(textColor)
            setBackgroundColor(secondaryColor)
        }
        totalAnswersCount += 1
        if (isAnswerCorrect) {
            correctAnswerCount += 1
        } else {
            tempOptionButtons[selectedAnswerIndex].apply {
                setTextColor(textColor)
                setBackgroundColor(colorError)
            }
        }

        // save the total usage stats
        PreferenceHelper.addToTotalStats(
            totalQuestions = 1,
            correctAnswers = if (isAnswerCorrect) 1 else 0
        )

        optionButtons.forEach {
            it.setOnClickListener(null)
        }

        lifecycleScope.launchWhenCreated {
            delay(800)
            if (questionIndex + 1 != questions.size) {
                // load next question
                questionIndex += 1
                loadQuestion()
            } else {
                questionIndex = 0
                if (
                    quizType == QuizType.ONLINE &&
                    PreferenceHelper.isUnlimitedMode()
                ) fetchQuestions()
                else showResultFragment()
            }
        }
    }

    private fun showResultFragment() {
        val resultFragment = ResultFragment()
        val bundle = Bundle()
        bundle.putInt(BundleArguments.questionsCount, totalAnswersCount)
        bundle.putInt(BundleArguments.correctAnswers, correctAnswerCount)
        resultFragment.arguments = bundle
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment, resultFragment)
            .addToBackStack(null)
            .commit()
    }
}
