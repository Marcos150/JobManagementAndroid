package edu.marcosadrian.jobmanagementandroid

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.marcosadrian.jobmanagementandroid.databinding.ItemJobBinding

//Adapter temporal para hacer pruebas
class JobsAdapter() : ListAdapter<Int, JobsAdapter.JobsViewHolder>(NotesDiffCallback()) {
    inner class JobsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemJobBinding.bind(view)

        fun bind(note: Int) {
            binding.jobDescription.text = note.toString()

            binding.jobPriorityColor.setBackgroundColor(binding.root.context.resources.getColor(
                R.color.yellow,
                binding.root.context.theme
            ))
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobsViewHolder {
        return JobsViewHolder(
            ItemJobBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )
    }

    override fun onBindViewHolder(holder: JobsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class NotesDiffCallback: DiffUtil.ItemCallback<Int>() {
    override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
        return oldItem === newItem
    }
}