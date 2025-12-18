package com.example.fitplanner.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.fitplanner.data.db.entities.UserEntity
import com.example.fitplanner.data.db.entities.UserType

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: UserEntity): Long

    @Query("""
        SELECT * FROM users 
        WHERE email = :email AND passwordHash = :password
        LIMIT 1
    """)
    suspend fun login(email: String, password: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): UserEntity?
    @Query("UPDATE users SET weight = :weight WHERE id = :userId")
    suspend fun updateWeight(userId: Long, weight: Float)

    @Query("SELECT * FROM users WHERE userType = :type")
    suspend fun getByType(type: UserType): List<UserEntity>

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE userType = 'ATHLETE' AND trainerId = :trainerId")
    suspend fun getAthletesByTrainer(trainerId: Long): List<UserEntity>

    @Query("SELECT * FROM users WHERE userType = 'ATHLETE' AND trainerId IS NULL")
    suspend fun getUnassignedAthletes(): List<UserEntity>


    @Query("SELECT * FROM users WHERE userType = :type")
    suspend fun getUsersByType(type: UserType): List<UserEntity>

    @Query("SELECT * FROM users WHERE userType = 'ATHLETE'")
    suspend fun getAllAthletes(): List<UserEntity>
    @Query("SELECT * FROM users WHERE userType = 'TRAINER'")
    suspend fun getAllTrainers(): List<UserEntity>
    @Query("SELECT COUNT(*) FROM users WHERE trainerId = :trainerId AND userType = 'ATHLETE'")
    suspend fun countAthletesByTrainer(trainerId: Long): Int

    @Query("""
    SELECT * FROM users 
    WHERE userType = 'ATHLETE'
    AND email LIKE '%' || :email || '%'
""")
    suspend fun searchAthletesByEmail(email: String): List<UserEntity>
    @Query("SELECT * FROM users WHERE userType = :athleteType AND trainerId IS NULL")
    suspend fun getAthletesWithoutTrainer(athleteType: Int = 1): List<UserEntity>
    @Query("SELECT * FROM users WHERE userType = 'ATHLETE' AND trainerId IS NULL")
    suspend fun getAthletesWithoutTrainer(): List<UserEntity>



    @Query("UPDATE users SET trainerId = :trainerId WHERE id = :athleteId")
    suspend fun assignTrainer(
        athleteId: Long,
        trainerId: Long
    )


    @Update

    suspend fun update(user: UserEntity)




}
