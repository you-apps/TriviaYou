package com.bnyro.trivia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bnyro.trivia.databinding.FragmentStatsBinding
import com.bnyro.trivia.util.RetrofitInstance
import com.fasterxml.jackson.databind.ObjectMapper
import java.util.*

class ApiStatsFragment : Fragment() {
    private lateinit var binding: FragmentStatsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            val metadata = try {
                RetrofitInstance.api.getMetadata()
            } catch (e: Exception) {
                return@launchWhenCreated
            }
            kotlin.runCatching {
                val stats = mutableListOf<String>()
                val mapper = ObjectMapper()

                val stateJson = mapper.writeValueAsString(metadata.byState)
                val stateStats = mapper.readTree(stateJson)

                stateStats.fields().forEach { field ->
                    stats += field.toString().replace("=", ": ")
                        .replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                        }
                }

                val categoryJson = mapper.writeValueAsString(metadata.byCategory)
                val categoryStats = mapper.readTree(categoryJson)

                categoryStats.fields().forEach {
                    stats += it.toString().replace("=", ": ")
                }

                val difficultyJson = mapper.writeValueAsString(metadata.byDifficulty)
                val difficultyStats = mapper.readTree(difficultyJson)

                difficultyStats.fields().forEach { field ->
                    stats += field.toString().replace("=", ": ")
                        .replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                        }
                }

                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, stats)
                binding.apiStats.adapter = adapter

                binding.progress.visibility = View.GONE
                binding.apiStats.visibility = View.VISIBLE
            }
        }
    }
}
