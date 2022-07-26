package com.bnyro.trivia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bnyro.trivia.R
import com.bnyro.trivia.databinding.FragmentResultBinding
import com.bnyro.trivia.util.BundleArguments
import com.bnyro.trivia.util.navigate

class ResultFragment : Fragment() {

    private lateinit var binding: FragmentResultBinding
    private var correctAnswers: Int = -1
    private var totalQuestions = -1
    private var libraryIndex: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        correctAnswers = arguments?.getInt(BundleArguments.correctAnswers)!!
        totalQuestions = arguments?.getInt(BundleArguments.questionsCount)!!
        libraryIndex = arguments?.getInt(BundleArguments.quizIndex)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentResultBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val correctAnswersPercentage: Float = correctAnswers.toFloat() / totalQuestions
        binding.resultText.text = when {
            correctAnswersPercentage > 2F / 3F -> getString(R.string.result_great)
            correctAnswersPercentage > 1F / 3F -> getString(R.string.result_average)
            else -> getString(R.string.result_fail)
        }

        binding.resultStats.text = context?.getString(R.string.result_stats, "$correctAnswers/$totalQuestions")

        binding.retry.setOnClickListener {
            val quizFragment = QuizFragment()
            val bundle = Bundle()
            if (libraryIndex != null) bundle.putInt(BundleArguments.quizIndex, libraryIndex!!)
            quizFragment.arguments = bundle
            parentFragmentManager.navigate(quizFragment)
        }
    }
}
