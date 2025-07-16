package com.example.attendancetracker.ui.Viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.attendancetracker.database.AppDatabase
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val attendanceDao = AppDatabase.getDatabase(application).attendanceDao()
    private val employeeDao = AppDatabase.getDatabase(application).employeeDao()

    val totalEmployees: LiveData<Int> = employeeDao.getEmployeeCount()

    private val _todayAttendance = MutableLiveData<Pair<Int, Int>>() // (present, absent)
    val todayAttendance: LiveData<Pair<Int, Int>> get() = _todayAttendance

    fun loadTodayAttendance(date: String) {
        viewModelScope.launch {
            val present = attendanceDao.getPresentCount(date)
            val absent = attendanceDao.getAbsentCount(date)
            _todayAttendance.postValue(Pair(present, absent))
        }
    }
}
