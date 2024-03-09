package com.example.taskschedule.utils

import androidx.room.TypeConverter
import java.time.LocalDate

class DateConverter {@TypeConverter
fun fromLocalDate(value: LocalDate?): String? {
    return value?.toString()
}

    @TypeConverter
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }
}