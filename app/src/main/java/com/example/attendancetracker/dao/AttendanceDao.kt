package com.example.attendancetracker.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.attendancetracker.model.Attendance
import com.example.attendancetracker.model.AttendanceWithEmployee

@Dao
interface AttendanceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(attendance: Attendance)

    @Query("SELECT * FROM attendance WHERE date = :date")
    suspend fun getAttendanceForDate(date: String): List<Attendance>

    @Query("SELECT * FROM attendance WHERE employeeId = :empId AND date = :date")
    suspend fun getAttendanceByEmployeeAndDate(empId: Int, date: String): Attendance?

    @Query("SELECT COUNT(*) FROM attendance WHERE date = :date AND isPresent = 1")
    suspend fun getPresentCount(date: String): Int

    @Query("SELECT COUNT(*) FROM attendance WHERE date = :date AND isPresent = 0")
    suspend fun getAbsentCount(date: String): Int

//    @Query("SELECT * FROM attendance WHERE date = :date")
//    suspend fun getAttendanceByDate(date: String): List<Attendance>
    @Query("SELECT * FROM attendance WHERE date = :selectedDate")
    suspend fun getAttendanceByDate(selectedDate: String): List<Attendance>

    @Transaction
    @Query("SELECT * FROM attendance")
    fun getAllAttendanceWithEmployees(): LiveData<List<AttendanceWithEmployee>>

    @Transaction
    @Query("SELECT * FROM attendance WHERE date = :selectedDate")
    suspend fun getAttendanceWithEmployeeByDate(selectedDate: String): List<AttendanceWithEmployee>






}
