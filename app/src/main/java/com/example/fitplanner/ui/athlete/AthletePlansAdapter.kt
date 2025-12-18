package com.example.fitplanner.ui.athlete

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitplanner.data.db.entities.TrainingPlanEntity
import com.example.fitplanner.databinding.ItemTrainingPlanBinding
import java.text.SimpleDateFormat
import java.util.*

class AthletePlansAdapter(
    private val onClick: (TrainingPlanEntity) -> Unit
) : ListAdapter<TrainingPlanEntity, AthletePlansAdapter.VH>(DiffCallback) {

    inner class VH(
        private val binding: ItemTrainingPlanBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(plan: TrainingPlanEntity, position: Int) {

            val dateText = SimpleDateFormat(
                "dd MMMM",
                Locale("ru")
            ).format(Date(plan.trainingDate))

            // Тренировка №X
            binding.tvTitle.text = "Тренировка №${position + 1} • $dateText"

            if (plan.isCompleted) {
                binding.tvStatus.text = "Выполнено"
                binding.tvStatus.setTextColor(
                    binding.root.context.getColor(android.R.color.holo_green_dark)
                )
                binding.root.alpha = 0.6f
            } else {
                binding.tvStatus.text = "В обработке"
                binding.tvStatus.setTextColor(
                    binding.root.context.getColor(android.R.color.holo_orange_dark)
                )
                binding.root.alpha = 1f
            }

            binding.root.setOnClickListener {
                onClick(plan)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemTrainingPlanBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position), position)
    }

    companion object {
        private val DiffCallback =
            object : DiffUtil.ItemCallback<TrainingPlanEntity>() {
                override fun areItemsTheSame(a: TrainingPlanEntity, b: TrainingPlanEntity) =
                    a.id == b.id

                override fun areContentsTheSame(a: TrainingPlanEntity, b: TrainingPlanEntity) =
                    a == b
            }
    }
}
