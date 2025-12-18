package com.example.fitplanner.ui.trainer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fitplanner.R
import com.example.fitplanner.data.db.entities.UserEntity

class TrainerAthletesAdapter(
    private var athletes: List<UserEntity>,
    private val onDoubleClick: (athlete: UserEntity) -> Unit
) : RecyclerView.Adapter<TrainerAthletesAdapter.AthleteViewHolder>() {

    private var lastClickTime = 0L
    private var lastClickedPosition = -1

    fun updateList(list: List<UserEntity>) {
        athletes = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AthleteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_athlete, parent, false)
        return AthleteViewHolder(view)
    }

    override fun getItemCount(): Int = athletes.size

    override fun onBindViewHolder(holder: AthleteViewHolder, position: Int) {
        val athlete = athletes[position]
        holder.tvName.text = athlete.name
        holder.tvEmail.text = athlete.email

        holder.itemView.setOnClickListener {
            val clickTime = System.currentTimeMillis()
            val currentPosition = holder.adapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                if (currentPosition == lastClickedPosition && clickTime - lastClickTime < 400) {
                    // Двойной клик
                    onDoubleClick(athletes[currentPosition])
                }
                lastClickTime = clickTime
                lastClickedPosition = currentPosition
            }
        }
    }


    class AthleteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvEmail: TextView = view.findViewById(R.id.tvEmail)
    }
}
