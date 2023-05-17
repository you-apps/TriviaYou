package com.bnyro.trivia.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bnyro.trivia.adapters.LibraryAdapter
import com.bnyro.trivia.databinding.FragmentLibraryBinding
import com.bnyro.trivia.dialogs.CreateQuizDialog
import com.bnyro.trivia.dialogs.DownloadDialog
import com.bnyro.trivia.util.NetworkHelper
import com.bnyro.trivia.util.PreferenceHelper

class LibraryFragment : Fragment() {
    private lateinit var binding: FragmentLibraryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibraryBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val quizzes = PreferenceHelper.getQuizzes()
        if (quizzes.isNotEmpty()) binding.libraryEmpty.visibility = View.GONE

        binding.libraryRV.layoutManager = LinearLayoutManager(context)
        binding.libraryRV.adapter = LibraryAdapter(quizzes, this)

        binding.downloadFAB.setOnClickListener {
            if (!NetworkHelper.isOnline(requireContext())) return@setOnClickListener
            DownloadDialog().show(childFragmentManager, null)
        }

        binding.createFAB.setOnClickListener {
            CreateQuizDialog().show(childFragmentManager, null)
        }
    }
}
