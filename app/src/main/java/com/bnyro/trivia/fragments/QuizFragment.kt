package com.bnyro.trivia.fragments

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bnyro.trivia.R
import com.bnyro.trivia.databinding.FragmentQuizBinding
import com.bnyro.trivia.extensions.navigate
import com.bnyro.trivia.extensions.showStyledSnackBar
import com.bnyro.trivia.extensions.toHTML
import com.bnyro.trivia.obj.Question
import com.bnyro.trivia.util.BundleArguments
import com.bnyro.trivia.util.PreferenceHelper
import com.bnyro.trivia.util.ThemeHelper
import com.google.android.material.elevation.SurfaceColors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class QuizFragment : Fragment() {
    lateinit var binding: FragmentQuizBinding

    private lateinit var optionButtons: List<Button>
    lateinit var questions: MutableList<Question>
    var questionIndex = 0

    private lateinit var answers: ArrayList<String>
    private var totalAnswersCount = 0
    private var correctAnswerCount = 0

    private var buttonTextColor = 0
    private var buttonBackgroundColor = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuizBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)

        optionButtons = binding.answersLL.children.map { it as Button }.toList()

        // disable click sounds of the buttons
        optionButtons.forEach {
            it.isSoundEffectsEnabled = false
        }

        buttonTextColor = optionButtons[0].currentTextColor

        // gets the surface color of the bottom navigation view
        buttonBackgroundColor = SurfaceColors.getColorForElevation(requireContext(), 10F)

        lifecycleScope.launch(Dispatchers.IO) {
            questions = fetchQuestions().toMutableList()
            withContext(Dispatchers.Main) {
                loadQuestion()
            }
        }
    }

    abstract suspend fun fetchQuestions(): List<Question>

    private fun loadQuestion(incrementIndex: Boolean = false) {
        if (incrementIndex) questionIndex += 1

        val question = questions.getOrNull(questionIndex) ?: run {
            binding.root.showStyledSnackBar(R.string.unknown_error)
            return
        }
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
            button.setBackgroundColor(buttonBackgroundColor)
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
        val question = questions[questionIndex]
        val correctAnswerIndex = answers.indexOf(question.correctAnswer)
        val isAnswerCorrect = correctAnswerIndex == selectedAnswerIndex

        val secondaryColor = ThemeHelper.getThemeColor(requireContext(), android.R.attr.colorAccent)
        val colorError = ThemeHelper.getThemeColor(
            requireContext(),
            com.google.android.material.R.attr.colorError
        )
        val textColor = ThemeHelper.getThemeColor(requireContext(), android.R.attr.colorBackground)

        tempOptionButtons[correctAnswerIndex].apply {
            setTextColor(textColor)
            setBackgroundColor(secondaryColor)
        }
        totalAnswersCount += 1
        if (isAnswerCorrect) {
            playSoundIfEnabled(R.raw.right)
            correctAnswerCount += 1
        } else {
            playSoundIfEnabled(R.raw.wrong)
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
            val delay = PreferenceHelper.getDelay(question.correctAnswer!!)
            if (delay == null) {
                // infinite delay
                optionButtons.forEach {
                    it.setOnClickListener {
                        lifecycleScope.launchWhenCreated {
                            loadNextQuestion()
                        }
                    }
                }
            } else {
                delay(delay)
                loadNextQuestion()
            }
        }
    }

    private fun loadNextQuestion() {
        lifecycleScope.launch(Dispatchers.IO) {
            if (prepareNextQuestions()) {
                withContext(Dispatchers.Main) {
                    loadQuestion(true)
                }
            } else {
                withContext(Dispatchers.Main) {
                    showResultFragment()
                }
            }
        }
    }

    abstract suspend fun prepareNextQuestions(): Boolean

    private fun showResultFragment() {
        val resultFragment = ResultFragment()
        val bundle = Bundle()
        bundle.putInt(BundleArguments.questionsCount, totalAnswersCount)
        bundle.putInt(BundleArguments.correctAnswers, correctAnswerCount)
        (this as? OfflineQuizFragment)?.let {
            bundle.putInt(BundleArguments.quizIndex, it.libraryIndex)
        }
        resultFragment.arguments = bundle
        parentFragmentManager.navigate(resultFragment)
    }

    private fun playSoundIfEnabled(resource: Int) {
        if (!PreferenceHelper.areSoundsEnabled()) return

        val mediaPlayer = MediaPlayer.create(context, resource)
        mediaPlayer.setOnCompletionListener {
            it.reset()
            it.release()
        }
        mediaPlayer.start()
    }
}
