package com.example.attendancetracker.ui.Activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.attendancetracker.databinding.ActivityDashboardBinding
import com.example.attendancetracker.ui.Viewmodels.DashboardViewModel
import java.text.SimpleDateFormat
import java.util.*

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var sharedPref: SharedPreferences
    private val dashboardViewModel: DashboardViewModel by viewModels()

    companion object {
        private const val PREFS_NAME = "settings"
        private const val DARK_MODE_KEY = "dark_mode"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Set theme before super.onCreate
        sharedPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val isDarkMode = sharedPref.getBoolean(DARK_MODE_KEY, false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupListeners()
        loadTodayAttendance()

        // Setup theme switch
        binding.switchTheme.isChecked = isDarkMode
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            sharedPref.edit().putBoolean(DARK_MODE_KEY, isChecked).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
            recreate() // Optional: smooth theme transition
        }
    }

    override fun onResume() {
        super.onResume()
        loadTodayAttendance()
    }

    private fun setupObservers() {
        dashboardViewModel.totalEmployees.observe(this) { total ->
            binding.tvTotalEmployees.text = "Total Employees: $total"
        }

        dashboardViewModel.todayAttendance.observe(this) { (present, absent) ->
            binding.tvPresentCount.text = "Present Today: $present"
            binding.tvAbsentCount.text = "Absent Today: $absent"
        }
    }

    private fun setupListeners() {
        binding.btnAddEmployee.setOnClickListener {
            startActivity(Intent(this, AddEmployeeActivity::class.java))
        }

        binding.btnMarkAttendance.setOnClickListener {
            startActivity(Intent(this, MarkAttendanceActivity::class.java))
        }

        binding.btnViewAttendance.setOnClickListener {
            startActivity(Intent(this, ViewAttendanceActivity::class.java))
        }
    }

    private fun loadTodayAttendance() {
        val todayDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        dashboardViewModel.loadTodayAttendance(todayDate)
    }
}
