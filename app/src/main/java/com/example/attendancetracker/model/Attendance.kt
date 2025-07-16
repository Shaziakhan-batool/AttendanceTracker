package com.example.attendancetracker.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attendance")
data class Attendance(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val employeeId: Int,
    val date: String,
    val isPresent: Boolean
)
