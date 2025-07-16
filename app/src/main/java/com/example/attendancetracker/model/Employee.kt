package com.example.attendancetracker.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employees")
data class Employee(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val designation: String,
    val phoneNumber: String,
    val profileUri: String? = null
)
