package com.example.fitplanner.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fitplanner.data.db.dao.*
import com.example.fitplanner.data.db.entities.*

@Database(
    entities = [
        UserEntity::class,
        TrainingPlanEntity::class,
        WorkoutEntity::class,
        TrainerAthleteEntity::class // ← ВАЖНО
    ],
    version = 2, // ← ОБЯЗАТЕЛЬНО увеличить
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun trainingPlanDao(): TrainingPlanDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun trainerAthleteDao(): TrainerAthleteDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fitplanner.db"
                )
                    // ⚠️ НА ВРЕМЯ РАЗРАБОТКИ
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
