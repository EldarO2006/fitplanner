package com.example.fitplanner.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.fitplanner.data.db.entities.WorkoutEntity

@Dao
interface WorkoutDao {

    @Insert
    suspend fun insert(workout: WorkoutEntity)

    @Query("SELECT * FROM workouts WHERE trainingPlanId = :planId")
    suspend fun getByPlan(planId: Long): List<WorkoutEntity>

    @Query("DELETE FROM workouts WHERE trainingPlanId = :planId")
    suspend fun deleteByPlan(planId: Long)
}
