package com.example.attendancetracker.ui.Viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.attendancetracker.database.AppDatabase
import com.example.attendancetracker.model.AttendanceWithEmployee
import kotlinx.coroutines.launch

class ViewAttendanceViewModel(application: Application) : AndroidViewModel(application) {

    private val attendanceDao = AppDatabase.getDatabase(application).attendanceDao()

    // MutableLiveData for filtered data
    private val _filteredAttendanceList = MutableLiveData<List<AttendanceWithEmployee>>()
    val filteredAttendanceList: LiveData<List<AttendanceWithEmployee>> get() = _filteredAttendanceList

    // Function to load attendance filtered by selected date
    fun filterAttendanceByDate(selectedDate: String) {
        viewModelScope.launch {
            val list = attendanceDao.getAttendanceWithEmployeeByDate(selectedDate)
            _filteredAttendanceList.postValue(list)
        }
    }
}
