package com.ayitinya.taskplanner.data.todos.source.local

import androidx.room.TypeConverter
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

class Converter {
    @TypeConverter
    fun fromTimestamp(value: Long?): ZonedDateTime? {
        return if (value != null) ZonedDateTime.ofInstant(Instant.ofEpochSecond(value), ZoneId.systemDefault()) else null

    }

    @TypeConverter
    fun fromDateToTimestamp(date: ZonedDateTime?): Long? {
        return date?.toEpochSecond()
    }
}