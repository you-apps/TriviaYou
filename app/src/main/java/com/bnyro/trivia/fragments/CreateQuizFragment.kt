package com.bnyro.trivia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bnyro.trivia.R
import com.bnyro.trivia.databinding.FragmentCreateQuizBinding
import com.bnyro.trivia.obj.EditModeType
import com.bnyro.trivia.obj.Question
import com.bnyro.trivia.util.BundleArguments
import com.bnyro.trivia.util.PreferenceHelper
import com.bnyro.trivia.util.toHTML
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class CreateQuizFragment : Fragment() {
    private lateinit var binding: FragmentCreateQuizBinding
    private lateinit var editTextViews: List<TextInputEditText>

    private var quizName: String? = null
    private var quizIndex: Int? = null
    private var questionIndex: Int? = null

    private var quizzes = PreferenceHelper.getQuizzes()
    private var questions = mutableListOf<Question>()
    private var editMode: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizName = arguments?.getString(BundleArguments.quizName)
        quizIndex = arguments?.getInt(BundleArguments.quizIndex, Int.MAX_VALUE)
        if (quizIndex == Int.MAX_VALUE) quizIndex = null
        questionIndex = arguments?.getInt(BundleArguments.questionIndex, Int.MAX_VALUE)
        if (questionIndex == Int.MAX_VALUE) questionIndex = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateQuizBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editMode = when {
            quizName != null -> {
                EditModeType.CREATE_NEW
            }
            quizIndex != null && questionIndex != null -> {
                questions = quizzes[quizIndex!!].questions!!.toMutableList()
                quizName = quizzes[quizIndex!!].name
                EditModeType.EDIT_EXISTING
            }
            quizIndex != null -> {
                questions = quizzes[quizIndex!!].questions!!.toMutableList()
                quizName = quizzes[quizIndex!!].name
                EditModeType.EDIT_APPEND
            }
            else -> throw IllegalArgumentException()
        }

        // editing question and not creating a new quiz
        // load the previous quiz data into the input fields
        if (editMode == EditModeType.EDIT_EXISTING) {
            val question = questions[questionIndex!!]
            loadQuestion(question)
        }

        binding.quizName.text = quizName

        editTextViews = listOf(
            binding.questionNameET,
            binding.correctAnswerET,
            binding.incorrectAnswerOne,
            binding.incorrectAnswerTwo,
            binding.incorrectAnswerThree
        )

        binding.save.setOnClickListener {
            if (allFieldsFilled()) {
                appendQuestionToList()
                editTextViews.forEach {
                    it.text?.clear()
                }
                if (editMode == EditModeType.EDIT_EXISTING) {
                    questionIndex = if (questionIndex!! + 1 != questionIndex) questionIndex!! + 1 else null
                    onViewCreated(view, null)
                }
            } else {
                Toast.makeText(context, R.string.item_empty, Toast.LENGTH_SHORT).show()
            }
        }

        binding.finish.setOnClickListener {
            if (allFieldsFilled()) appendQuestionToList()
            if (questions.isNotEmpty()) {
                showFinishDialog()
            } else {
                findNavController().navigate(R.id.libraryFragment)
            }
        }
    }

    private fun showFinishDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.save_quiz)
            .setMessage(R.string.save_quiz_message)
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .setPositiveButton(android.R.string.ok) { _, _ ->
                saveQuiz()
                findNavController().navigate(R.id.libraryFragment)
            }
            .show()
    }

    private fun saveQuiz() {
        if (editMode == EditModeType.EDIT_EXISTING) {
            val quiz = quizzes[quizIndex!!]
            quiz.questions = questions
            PreferenceHelper.replaceQuizByIndex(quizIndex!!, quiz)
        } else {
            PreferenceHelper.saveQuiz(quizName!!, true, questions)
        }
    }

    private fun getInsertedQuestion(): Question {
        val question = Question(
            question = binding.questionNameET.text.toString(),
            correctAnswer = binding.correctAnswerET.text.toString(),
            incorrectAnswers = listOf(
                binding.incorrectAnswerOne.text.toString(),
                binding.incorrectAnswerTwo.text.toString(),
                binding.incorrectAnswerThree.text.toString()
            )
        )
        return question
    }

    private fun appendQuestionToList() {
        val question = getInsertedQuestion()
        if (editMode != EditModeType.EDIT_EXISTING) questions += question
        else questions[questionIndex!!] = question
        binding.questionCount.text = context?.getString(R.string.questions, questions.size)
    }

    private fun loadQuestion(question: Question) {
        binding.questionNameET.setText(question.question.toHTML())
        binding.correctAnswerET.setText(question.correctAnswer.toHTML())
        binding.incorrectAnswerOne.setText(question.incorrectAnswers!![0].toHTML())
        binding.incorrectAnswerTwo.setText(question.incorrectAnswers[1].toHTML())
        binding.incorrectAnswerThree.setText(question.incorrectAnswers[2].toHTML())
    }

    private fun allFieldsFilled(): Boolean {
        editTextViews.forEach {
            if (it.text.toString() == "") return false
        }
        return true
    }
}
