package com.example.attendancetracker.ui.Viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.attendancetracker.database.AppDatabase


class EmployeeViewModel(application: Application) : AndroidViewModel(application) {

    val totalEmployees: LiveData<Int>

    init {
        val employeeDao = AppDatabase.getDatabase(application).employeeDao()
        totalEmployees = employeeDao.getEmployeeCount()
    }
}
