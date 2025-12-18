package com.example.fitplanner.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fitplanner.data.db.entities.UserEntity
import com.example.fitplanner.databinding.ItemAthleteBinding

class AthleteAdapter(
    private val onClick: (UserEntity) -> Unit
) : ListAdapter<UserEntity, AthleteAdapter.VH>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemAthleteBinding.inflate(
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
        private val binding: ItemAthleteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(athlete: UserEntity) {
            binding.tvName.text = athlete.name
            binding.tvEmail.text = athlete.email

            binding.root.setOnClickListener {
                onClick(athlete)
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<UserEntity>() {
            override fun areItemsTheSame(o: UserEntity, n: UserEntity) = o.id == n.id
            override fun areContentsTheSame(o: UserEntity, n: UserEntity) = o == n
        }
    }
}
