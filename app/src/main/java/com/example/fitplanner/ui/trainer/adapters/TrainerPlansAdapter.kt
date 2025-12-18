package com.example.fitplanner.ui.trainer


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitplanner.data.db.models.TrainingPlanWithAthlete
import com.example.fitplanner.databinding.ItemTrainerPlanBinding
import java.text.SimpleDateFormat
import java.util.*

class TrainerPlansAdapter :
    ListAdapter<TrainingPlanWithAthlete, TrainerPlansAdapter.VH>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemTrainerPlanBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VH(
        private val binding: ItemTrainerPlanBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: TrainingPlanWithAthlete) {
            val plan = item.plan

            binding.tvTitle.text = "Тренировка №${plan.id}"
            binding.tvAthlete.text = "Атлет: ${item.athleteName}"

            binding.tvStatus.text =
                if (plan.isCompleted) "Выполнена" else "В обработке"
        }
    }

    companion object {
        private val DiffCallback =
            object : DiffUtil.ItemCallback<TrainingPlanWithAthlete>() {

                override fun areItemsTheSame(
                    oldItem: TrainingPlanWithAthlete,
                    newItem: TrainingPlanWithAthlete
                ) = oldItem.plan.id == newItem.plan.id

                override fun areContentsTheSame(
                    oldItem: TrainingPlanWithAthlete,
                    newItem: TrainingPlanWithAthlete
                ) = oldItem == newItem
            }
    }
}
