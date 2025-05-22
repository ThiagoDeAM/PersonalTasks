package com.example.personaltasks.model

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    // Converte um valor Long (timestamp) para um objeto Date.
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    // Converte um objeto Date para um Long (timestamp).
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}