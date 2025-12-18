package com.example.fitplanner.utils

import android.content.Context
import com.example.fitplanner.data.db.entities.UserType

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )

    var userId: Long
        get() = prefs.getLong(KEY_USER_ID, -1)
        private set(value) = prefs.edit().putLong(KEY_USER_ID, value).apply()

    var name: String
        get() = prefs.getString(KEY_NAME, "") ?: ""
        private set(value) = prefs.edit().putString(KEY_NAME, value).apply()

    var email: String
        get() = prefs.getString(KEY_EMAIL, "") ?: ""
        private set(value) = prefs.edit().putString(KEY_EMAIL, value).apply()

    var userType: UserType
        get() {
            val type = prefs.getString(KEY_USER_TYPE, UserType.ATHLETE.name)
            return UserType.valueOf(type!!)
        }
        private set(value) = prefs.edit().putString(KEY_USER_TYPE, value.name).apply()

    var trainerId: Long
        get() = prefs.getLong(KEY_TRAINER_ID, -1)
        private set(value) = prefs.edit().putLong(KEY_TRAINER_ID, value).apply()

    /**
     * Сохраняет пользователя в сессии.
     * Для тренера и атлета trainerId можно передавать (-1 по умолчанию для тренера/админа)
     */
    fun saveUser(
        userId: Long,
        name: String,
        email: String,
        userType: UserType,
        trainerId: Long = -1
    ) {
        this.userId = userId
        this.name = name
        this.email = email
        this.userType = userType
        this.trainerId = trainerId
    }

    fun isLoggedIn(): Boolean = userId != -1L

    fun clear() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val PREFS_NAME = "fitplanner_session"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_NAME = "name"
        private const val KEY_EMAIL = "email"
        private const val KEY_USER_TYPE = "user_type"
        private const val KEY_TRAINER_ID = "trainer_id"
    }
}
