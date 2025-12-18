package com.example.fitplanner.data.db

import androidx.room.TypeConverter
import com.example.fitplanner.data.db.entities.UserType

class Converters {

    @TypeConverter
    fun fromUserType(type: UserType): String = type.name

    @TypeConverter
    fun toUserType(value: String): UserType = UserType.valueOf(value)
}
