package com.example.fitplanner.data.db.dao

import androidx.room.*
import com.example.fitplanner.data.db.entities.TrainerAthleteEntity
import com.example.fitplanner.data.db.entities.UserEntity

@Dao
interface TrainerAthleteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun assign(entity: TrainerAthleteEntity)

    @Query("""
        SELECT u.* FROM users u
        INNER JOIN trainer_athletes ta ON ta.athleteId = u.id
        WHERE ta.trainerId = :trainerId
    """)
    suspend fun getAthletesByTrainer(trainerId: Long): List<UserEntity>

    @Query("""
        SELECT u.* FROM users u
        INNER JOIN trainer_athletes ta ON ta.trainerId = u.id
        WHERE ta.athleteId = :athleteId
    """)
    suspend fun getTrainerByAthlete(athleteId: Long): UserEntity?
}
