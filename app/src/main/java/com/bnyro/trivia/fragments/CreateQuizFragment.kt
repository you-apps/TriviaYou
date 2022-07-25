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
import com.bnyro.trivia.obj.Question
import com.bnyro.trivia.util.BundleArguments
import com.bnyro.trivia.util.PreferenceHelper
import com.google.android.material.textfield.TextInputEditText

class CreateQuizFragment : Fragment() {
    private lateinit var binding: FragmentCreateQuizBinding
    private lateinit var editTextViews: List<TextInputEditText>

    private lateinit var quizName: String
    private val questions = mutableListOf<Question>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizName = arguments?.getString(BundleArguments.quizName)!!
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
            } else {
                Toast.makeText(context, R.string.item_empty, Toast.LENGTH_SHORT).show()
            }
        }

        binding.finish.setOnClickListener {
            if (allFieldsFilled()) appendQuestionToList()
            if (questions.isNotEmpty()) {
                PreferenceHelper.saveQuiz(quizName, true, questions)
            }
            findNavController().navigate(R.id.libraryFragment)
        }
    }

    private fun appendQuestionToList() {
        questions += Question(
            category = null,
            correctAnswer = binding.correctAnswerET.text.toString(),
            difficulty = null,
            id = null,
            incorrectAnswers = listOf(
                binding.incorrectAnswerOne.text.toString(),
                binding.incorrectAnswerTwo.text.toString(),
                binding.incorrectAnswerThree.text.toString()
            ),
            question = binding.questionNameET.text.toString(),
            regions = null,
            tags = null,
            type = null
        )
    }

    private fun allFieldsFilled(): Boolean {
        editTextViews.forEach {
            if (it.text.toString() == "") return false
        }
        return true
    }
}
