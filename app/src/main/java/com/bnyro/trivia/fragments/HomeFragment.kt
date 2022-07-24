package com.bnyro.trivia.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bnyro.trivia.R
import com.bnyro.trivia.databinding.FragmentHomeBinding
import com.bnyro.trivia.obj.Question
import com.bnyro.trivia.util.PreferenceHelper
import com.bnyro.trivia.util.RetrofitInstance
import com.bnyro.trivia.util.ThemeHelper
import kotlinx.coroutines.delay

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var optionButtons: List<Button>
    private lateinit var questions: List<Question>
    private var questionIndex = 0

    private lateinit var answers: ArrayList<String>
    private var correctAnswerCount = 0

    private var buttonTextColor = 0

    private var category: String? = null
    private var difficulty: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        category = arguments?.getString("category")
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

        // set the difficulty
        val difficultyPref = PreferenceHelper.getString(
            getString(R.string.difficulty_key),
            getString(R.string.difficulty_default)
        )
        difficulty = when (difficultyPref) {
            "random" -> null
            else -> difficultyPref
        }

        optionButtons = listOf(
            binding.optionA,
            binding.optionB,
            binding.optionC,
            binding.optionD
        )

        buttonTextColor = optionButtons[0].currentTextColor

        fetchQuestions()
    }

    private fun fetchQuestions() {
        lifecycleScope.launchWhenCreated {
            questions = try {
                RetrofitInstance.api.getQuestions(50, category, difficulty)
            } catch (e: Exception) {
                Log.e("error", "error")
                return@launchWhenCreated
            }
            showQuestion()
        }
    }

    private fun showQuestion() {
        val question = questions[questionIndex]
        binding.questionTV.text = question.question

        answers = arrayListOf(question.correctAnswer!!)
        // append all incorrect answers
        question.incorrectAnswers?.forEach {
            answers += it
        }
        answers.shuffle()

        optionButtons.forEachIndexed { index, button ->
            // reset button style
            button.setTextColor(buttonTextColor)
            button.setBackgroundColor(Color.TRANSPARENT)

            // set text and onclick listener
            val answer = answers[index]
            button.text = answer
            button.setOnClickListener {
                checkAnswer(index)
            }
        }

        binding.progress.visibility = View.GONE
        binding.questionLL.visibility = View.VISIBLE
    }

    private fun checkAnswer(selectedAnswerIndex: Int) {
        val correctAnswerIndex = answers.indexOf(questions[questionIndex].correctAnswer)
        val isAnswerCorrect = correctAnswerIndex == selectedAnswerIndex
        val secondaryColor = ThemeHelper.getThemeColor(requireContext(), android.R.attr.colorAccent)
        optionButtons[correctAnswerIndex].apply {
            setTextColor(Color.WHITE)
            setBackgroundColor(secondaryColor)
        }
        if (isAnswerCorrect) {
            correctAnswerCount += 1
        } else {
            val colorError = ThemeHelper.getThemeColor(requireContext(), com.google.android.material.R.attr.colorError)
            optionButtons[selectedAnswerIndex].apply {
                setTextColor(Color.WHITE)
                setBackgroundColor(colorError)
            }
        }
        lifecycleScope.launchWhenCreated {
            delay(800)
            if (questionIndex + 1 != questions.size) {
                questionIndex += 1
                showQuestion()
            } else {
                questionIndex = 0
                fetchQuestions()
            }
        }
    }
}
