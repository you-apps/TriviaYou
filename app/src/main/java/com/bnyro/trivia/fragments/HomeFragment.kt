package com.bnyro.trivia.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bnyro.trivia.databinding.FragmentHomeBinding
import com.bnyro.trivia.obj.Question
import com.bnyro.trivia.util.RetrofitInstance

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var questions: List<Question>
    private var questionIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
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
        fetchQuestions()
    }

    private fun fetchQuestions() {
        lifecycleScope.launchWhenCreated {
            questions = try {
                RetrofitInstance.api.getQuestions(5)
            } catch (e: Exception) {
                Log.e("error", "error")
                return@launchWhenCreated
            }
            showQuestion(questions[0])
        }
    }

    private fun showQuestion(question: Question) {
        binding.questionTV.text = question.question

        val answers = arrayListOf(question.correctAnswer!!)
        // append all incorrect answers
        question.incorrectAnswers?.forEach {
            answers += it
        }
        answers.shuffle()

        val optionButtons = listOf<Button>(
            binding.optionA,
            binding.optionB,
            binding.optionC,
            binding.optionD
        )

        optionButtons.forEachIndexed { index, button ->
            val answer = answers[index]
            button.text = answer
            button.setOnClickListener {
                checkAnswer(answer)
            }
        }
    }

    private fun checkAnswer(answer: String) {
        val isAnswerCorrect = questions[questionIndex].correctAnswer == answer
        Toast.makeText(context, isAnswerCorrect.toString(), Toast.LENGTH_LONG).show()
    }
}
