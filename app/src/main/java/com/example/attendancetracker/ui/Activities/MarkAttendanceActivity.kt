package com.example.attendancetracker.ui.Activities


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendancetracker.database.AppDatabase
import com.example.attendancetracker.databinding.ActivityMarkAttendanceBinding
import com.example.attendancetracker.model.Attendance
import com.example.attendancetracker.ui.Adapters.AttendanceAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class MarkAttendanceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMarkAttendanceBinding
    private lateinit var adapter: AttendanceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMarkAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = AppDatabase.getDatabase(this)
        val employeeDao = db.employeeDao()
        val attendanceDao = db.attendanceDao()

        // Date format
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val todayDate = dateFormat.format(Date())

        // Load employees
        lifecycleScope.launch {
            val employeeList = withContext(Dispatchers.IO) {
                employeeDao.getAllEmployees()
            }

            adapter = AttendanceAdapter(employeeList)
            binding.rvEmployees.layoutManager = LinearLayoutManager(this@MarkAttendanceActivity)
            binding.rvEmployees.adapter = adapter
        }

        // Save Attendance
        binding.btnSubmitAttendance.setOnClickListener {
            val markedAttendance = adapter.getMarkedAttendance()

            lifecycleScope.launch {
                var duplicateFound = false

                for ((empId, isPresent) in markedAttendance) {
                    val alreadyMarked = withContext(Dispatchers.IO) {
                        attendanceDao.getAttendanceByEmployeeAndDate(empId, todayDate)
                    }

                    if (alreadyMarked != null) {
                        duplicateFound = true
                        continue
                    }

                    val record = Attendance(
                        employeeId = empId,
                        date = todayDate,
                        isPresent = isPresent
                    )

                    withContext(Dispatchers.IO) {
                        attendanceDao.insertAttendance(record)
                    }
                }

                withContext(Dispatchers.Main) {
                    if (duplicateFound) {
                        Toast.makeText(
                            this@MarkAttendanceActivity,
                            "Some employees were already marked for today.",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this@MarkAttendanceActivity,
                            "Attendance saved successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    finish()
                }
            }
        }
    }
}
