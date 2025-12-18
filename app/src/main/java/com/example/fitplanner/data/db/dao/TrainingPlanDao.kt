package com.example.fitplanner.data.db.dao

import com.example.fitplanner.data.db.models.TrainingPlanWithAthlete
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.fitplanner.data.db.entities.TrainingPlanEntity

@Dao
interface TrainingPlanDao {

    @Insert
    suspend fun insert(plan: TrainingPlanEntity)

    @Query("SELECT * FROM training_plans WHERE athleteId = :athleteId")
    suspend fun getByAthlete(athleteId: Long): List<TrainingPlanEntity>

    @Query("SELECT * FROM training_plans WHERE trainerId = :trainerId")
    suspend fun getByTrainer(trainerId: Long): List<TrainingPlanEntity>

    @Query("SELECT * FROM training_plans WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): TrainingPlanEntity?

    @Query("UPDATE training_plans SET isCompleted = 1 WHERE id = :planId")
    suspend fun markCompleted(planId: Long)

    @Query("SELECT COUNT(*) FROM training_plans WHERE athleteId = :athleteId")
    suspend fun countByAthlete(athleteId: Long): Int

    @Query("""
        SELECT COUNT(*) FROM training_plans
        WHERE athleteId = :athleteId AND isCompleted = 1
    """)
    suspend fun countCompletedByAthlete(athleteId: Long): Int

    @Query("""
    SELECT * FROM training_plans
    WHERE athleteId = :athleteId
    AND trainerId = :trainerId
""")
    suspend fun getForAthlete(
        athleteId: Long,
        trainerId: Long
    ): List<TrainingPlanEntity>
    @Query("SELECT * FROM training_plans WHERE trainerId = :trainerId")
    suspend fun getForTrainer(trainerId: Long): List<TrainingPlanEntity>
    @Query("SELECT * FROM training_plans WHERE athleteId = :athleteId AND isCompleted = 1")
    suspend fun getCompletedForAthlete(athleteId: Long): List<TrainingPlanEntity>
    @Query("""
    SELECT tp.*, u.name AS athleteName
    FROM training_plans tp
    JOIN users u ON u.id = tp.athleteId
    WHERE tp.trainerId = :trainerId
""")
    suspend fun getAllForTrainer(trainerId: Long): List<TrainingPlanWithAthlete>

    @Query("""
    SELECT tp.*, u.name AS athleteName
    FROM training_plans tp
    JOIN users u ON u.id = tp.athleteId
    WHERE tp.trainerId = :trainerId AND tp.athleteId = :athleteId
""")
    suspend fun getForTrainerAndAthlete(
        trainerId: Long,
        athleteId: Long
    ): List<TrainingPlanWithAthlete>


}
