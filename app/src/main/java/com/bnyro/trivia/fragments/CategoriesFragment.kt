package com.bnyro.trivia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bnyro.trivia.R
import com.bnyro.trivia.databinding.FragmentCategoriesBinding
import com.bnyro.trivia.extensions.navigate
import com.bnyro.trivia.extensions.showSnackBar
import com.bnyro.trivia.util.ApiHelper
import com.bnyro.trivia.util.BundleArguments

class CategoriesFragment : Fragment() {
    private lateinit var binding: FragmentCategoriesBinding

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
        binding = FragmentCategoriesBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchCategories()
    }

    private fun fetchCategories() {
        lifecycleScope.launchWhenCreated {
            val categories = try {
                ApiHelper().getCategories()
            } catch (e: Exception) {
                binding.root.showSnackBar(R.string.network_error)
                return@launchWhenCreated
            }

            val sortedCategories = categories.sortedBy { it.name }

            val categoryNames = mutableListOf<String>()
            val categoryQueries = mutableListOf<String>()
            sortedCategories.forEach {
                categoryNames += it.name!!
                categoryQueries += it.id!!
            }

            val categoriesAdapter =
                ArrayAdapter(requireContext(), R.layout.list_item, categoryNames)
            binding.categoriesLV.adapter = categoriesAdapter

            binding.progress.visibility = View.GONE
            binding.categoriesLV.visibility = View.VISIBLE

            binding.categoriesLV.onItemClickListener = OnItemClickListener { _, _, index, _ ->
                val category = categoryQueries[index]
                val quizFragment = QuizFragment()
                val bundle = Bundle()
                bundle.putString(BundleArguments.category, category)
                quizFragment.arguments = bundle

                parentFragmentManager.navigate(quizFragment)
            }
        }
    }
}
