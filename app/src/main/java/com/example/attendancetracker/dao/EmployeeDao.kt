package com.example.attendancetracker.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.attendancetracker.model.Employee


@Dao
interface EmployeeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployee(employee: Employee)

    @Query("SELECT * FROM employees")
    suspend fun getAllEmployees(): List<Employee>

    @Query("SELECT COUNT(*) FROM employees")
    fun getEmployeeCount(): LiveData<Int>

    @Delete
    suspend fun deleteEmployee(employee: Employee)
}
