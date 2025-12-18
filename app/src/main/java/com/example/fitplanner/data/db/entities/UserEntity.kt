// UserEntity.kt
package com.example.fitplanner.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val email: String,
    val passwordHash: String,
    val userType: UserType,

    // 🔥 ГЛАВНОЕ
    var trainerId: Long? = null, // ТОЛЬКО ДЛЯ ATHLETE

    val height: Int? = null,
    val weight: Int? = null,
    val experienceYears: Int? = null,
    val registrationDate: Long
)

