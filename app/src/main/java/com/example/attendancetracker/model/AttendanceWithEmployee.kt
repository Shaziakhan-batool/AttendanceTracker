package com.example.attendancetracker.model

import androidx.room.Embedded
import androidx.room.Relation

data class AttendanceWithEmployee(
    @Embedded val attendance: Attendance,
    @Relation(
        parentColumn = "employeeId",
        entityColumn = "id"
    )
    val employee: Employee
)
