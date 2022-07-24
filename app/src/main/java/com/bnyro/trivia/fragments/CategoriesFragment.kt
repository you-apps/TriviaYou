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
            /*
            val categories = try {
                RetrofitInstance.api.getCategories()
            } catch (e: Exception) {
                return@launchWhenCreated
            }
            Log.e("categories", categories.toString())
            */

            val categoryNames = listOf(
                "Arts & Literature", "Film & TV", "Food & Drink", "General Knowledge", "Geography", "History", "Music", "Science", "Society & Culture", "Sport & Leisure"
            )
            val categoryQueries = listOf(
                "arts", "film", "food", "general_knowledge", "geography", "history", "music", "science", "culture", "sports"
            )

            val categoriesAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, categoryNames)
            binding.categoriesLV.adapter = categoriesAdapter

            binding.categoriesLV.onItemClickListener = OnItemClickListener { _, _, index, _ ->
                val category = categoryQueries[index]
                val homeFragment = HomeFragment()
                val bundle = Bundle()
                bundle.putString(BundleArguments.category, category)
                homeFragment.arguments = bundle

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment, homeFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}
