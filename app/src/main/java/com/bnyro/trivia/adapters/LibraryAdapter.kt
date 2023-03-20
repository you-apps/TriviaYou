package com.bnyro.trivia.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bnyro.trivia.R
import com.bnyro.trivia.databinding.RowQuizBinding
import com.bnyro.trivia.dialogs.QuizOptionsDialog
import com.bnyro.trivia.extensions.navigate
import com.bnyro.trivia.fragments.OfflineQuizFragment
import com.bnyro.trivia.obj.Quiz
import com.bnyro.trivia.util.BundleArguments
import com.bnyro.trivia.util.PreferenceHelper

class LibraryAdapter(
    private val quizzes: List<Quiz>,
    private val parentFragment: Fragment
) : RecyclerView.Adapter<LibraryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RowQuizBinding.inflate(layoutInflater, parent, false)
        return LibraryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
        val quiz = quizzes[position]
        holder.binding.apply {
            quizName.text = quiz.name
            quizSize.text = root.context.getString(
                R.string.questions,
                quiz.questions?.size.toString()
            )
            quizType.setImageResource(
                if (quiz.creator == true) {
                    R.drawable.ic_bookmark
                } else {
                    R.drawable.ic_public
                }
            )
            root.setOnClickListener {
                val quizFragment = OfflineQuizFragment().apply {
                    val quizIndex = PreferenceHelper.getQuizzes().indexOf(quiz)
                    arguments = bundleOf(BundleArguments.quizIndex to quizIndex)
                }
                parentFragment.parentFragmentManager.navigate(quizFragment)
            }
            root.setOnLongClickListener {
                val quizOptionsDialog = QuizOptionsDialog(position)
                quizOptionsDialog.show(parentFragment.childFragmentManager, null)
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return quizzes.size
    }
}

class LibraryViewHolder(
    val binding: RowQuizBinding
) : RecyclerView.ViewHolder(binding.root)
