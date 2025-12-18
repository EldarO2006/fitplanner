package com.example.fitplanner.data.db.models

import androidx.room.Embedded
import com.example.fitplanner.data.db.entities.TrainingPlanEntity

data class TrainingPlanWithAthlete(
    @Embedded val plan: TrainingPlanEntity,
    val athleteName: String
)
