package com.example.fitplanner.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class WorkoutEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val trainingPlanId: Long,

    val name: String,
    val sets: Int,
    val reps: Int,
    val weight: Float?
)
