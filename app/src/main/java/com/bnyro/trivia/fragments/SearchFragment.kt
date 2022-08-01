package com.bnyro.trivia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bnyro.trivia.adapters.LibraryAdapter
import com.bnyro.trivia.databinding.FragmentSearchBinding
import com.bnyro.trivia.util.BundleArguments
import com.bnyro.trivia.util.PreferenceHelper

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var query: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        query = arguments?.getString(BundleArguments.query)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val quizzes = PreferenceHelper.getQuizzes()
        val results = quizzes.filter { it.name?.contains(query)!! }

        binding.searchResults.layoutManager = LinearLayoutManager(requireContext())
        val adapter = LibraryAdapter(results, this)
        binding.searchResults.adapter = adapter
    }
}
