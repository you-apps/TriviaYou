package com.bnyro.trivia.fragments

import android.os.Bundle
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.bnyro.trivia.R
import com.bnyro.trivia.databinding.FragmentEditQuizBinding
import com.bnyro.trivia.extensions.navigate
import com.bnyro.trivia.extensions.toHTML
import com.bnyro.trivia.obj.Quiz
import com.bnyro.trivia.util.BundleArguments
import com.bnyro.trivia.util.PreferenceHelper

class EditQuizFragment : Fragment() {
    private lateinit var binding: FragmentEditQuizBinding
    private var quizIndex: Int = -1
    private lateinit var quiz: Quiz

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        quizIndex = arguments?.getInt(BundleArguments.quizIndex)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditQuizBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        quiz = PreferenceHelper.getQuizzes()[quizIndex]

        binding.quizName.text = quiz.name
        binding.questionCount.text = context?.getString(R.string.questions, quiz.questions?.size)

        val adapter = ArrayAdapter<Spanned>(requireContext(), R.layout.list_item)

        quiz.questions?.forEach {
            adapter.add(it.question.toHTML())
        }

        binding.questionsList.adapter = adapter
        binding.questionsList.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, index, _ ->
                val createQuizFragment = CreateQuizFragment()
                val bundle = Bundle()
                bundle.putInt(BundleArguments.quizIndex, quizIndex)
                bundle.putInt(BundleArguments.questionIndex, index)
                createQuizFragment.arguments = bundle
                parentFragmentManager.navigate(createQuizFragment)
            }

        binding.newQuestionFAB.setOnClickListener {
            val createQuizFragment = CreateQuizFragment()
            val bundle = Bundle()
            bundle.putInt(BundleArguments.quizIndex, quizIndex)
            createQuizFragment.arguments = bundle
            parentFragmentManager.navigate(createQuizFragment)
        }
    }
}
