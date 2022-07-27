package com.bnyro.trivia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bnyro.trivia.R
import com.bnyro.trivia.databinding.FragmentStatsBinding
import com.bnyro.trivia.util.ApiHelper
import com.google.android.material.snackbar.Snackbar

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
            val stats = try {
                ApiHelper().getStats()
            } catch (e: Exception) {
                Snackbar.make(binding.root, R.string.network_error, Snackbar.LENGTH_LONG)
                    .show()
                return@launchWhenCreated
            }
            kotlin.runCatching {
                val adapter = ArrayAdapter(requireContext(), R.layout.list_item, stats)
                binding.apiStats.adapter = adapter

                binding.progress.visibility = View.GONE
                binding.apiStats.visibility = View.VISIBLE
            }
        }
    }
}
