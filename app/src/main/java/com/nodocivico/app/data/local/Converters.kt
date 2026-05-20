package com.nodocivico.app.data.local

import androidx.room.TypeConverter
import com.nodocivico.app.data.model.Priority
import com.nodocivico.app.data.model.ReportStatus

class Converters {

    @TypeConverter
    fun fromPriority(value: Priority): String = value.name

    @TypeConverter
    fun toPriority(value: String): Priority = Priority.valueOf(value)

    @TypeConverter
    fun fromStatus(value: ReportStatus): String = value.name

    @TypeConverter
    fun toStatus(value: String): ReportStatus = ReportStatus.valueOf(value)
}