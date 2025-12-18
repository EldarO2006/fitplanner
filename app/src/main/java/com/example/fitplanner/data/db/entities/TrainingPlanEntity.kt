package com.example.fitplanner.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "training_plans",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["athleteId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["trainerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["athleteId"]),
        Index(value = ["trainerId"])
    ]
)
data class TrainingPlanEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val athleteId: Long,
    val trainerId: Long,

    val title: String,
    val description: String,
    val trainingDate: Long,

    val isCompleted: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

