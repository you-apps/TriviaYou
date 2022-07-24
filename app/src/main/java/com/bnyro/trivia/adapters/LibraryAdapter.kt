package com.bnyro.trivia.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bnyro.trivia.R
import com.bnyro.trivia.databinding.RowQuizBinding
import com.bnyro.trivia.util.PreferenceHelper

class LibraryAdapter : RecyclerView.Adapter<LibraryViewHolder>() {
    private var quizzes = PreferenceHelper.getQuizzes()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowQuizBinding.inflate(layoutInflater)
        return LibraryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
        val quiz = quizzes[position]
        holder.binding.apply {
            quizName.text = root.context.getString(R.string.questions, quiz.name)
            quizSize.text = quiz.questions?.size?.toString()
            quizType.setImageResource(
                if (quiz.isCreator == true) R.drawable.ic_bookmark
                else R.drawable.ic_public
            )
        }
    }

    override fun getItemCount(): Int {
        return quizzes.size
    }
}

class LibraryViewHolder(
    val binding: RowQuizBinding
) : RecyclerView.ViewHolder(binding.root)
