package edu.marcosadrian.jobmanagementandroid

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import edu.marcosadrian.jobmanagementandroid.databinding.ItemJobBinding
import edu.marcosadrian.jobmanagementandroid.model.Job

//Adapter temporal para hacer pruebas
class JobsAdapter(val onClick: (Job) -> Unit) : ListAdapter<Job, JobsAdapter.JobsViewHolder>(NotesDiffCallback()) {
    inner class JobsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemJobBinding.bind(view)

        fun bind(job: Job) {
            binding.jobDescription.text = job.descripcion
            binding.jobCategory.text = job.categoria
            if (job.fecFin != null) binding.jobDoneCheck.visibility = VISIBLE
            else binding.jobDoneCheck.visibility = INVISIBLE

                itemView.setOnClickListener{
                onClick(job)
            }

            // Color de prioridad
            binding.jobPriorityColor.setBackgroundColor(
                binding.root.context.resources.getColor(when (job.prioridad) {
                    1 -> R.color.red
                    2 -> R.color.orange
                    3 -> R.color.yellow
                    4 -> R.color.green
                    else -> R.color.white
                }, binding.root.context.theme
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

class NotesDiffCallback: DiffUtil.ItemCallback<Job>() {
    override fun areContentsTheSame(oldItem: Job, newItem: Job): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: Job, newItem: Job): Boolean {
        return oldItem === newItem
    }
}