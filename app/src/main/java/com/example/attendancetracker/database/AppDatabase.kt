package com.example.attendancetracker.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.attendancetracker.dao.AttendanceDao
import com.example.attendancetracker.dao.EmployeeDao
import com.example.attendancetracker.model.Attendance
import com.example.attendancetracker.model.Employee



@Database(entities = [Employee::class, Attendance::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun employeeDao(): EmployeeDao
    abstract fun attendanceDao(): AttendanceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "attendance_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
