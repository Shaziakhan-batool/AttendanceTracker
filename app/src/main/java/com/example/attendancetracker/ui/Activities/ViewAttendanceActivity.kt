package com.example.attendancetracker.ui.Activities

import android.Manifest
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.attendancetracker.databinding.ActivityViewAttendanceBinding
import com.example.attendancetracker.ui.Adapters.ViewAttendanceAdapter
import com.example.attendancetracker.ui.Viewmodels.ViewAttendanceViewModel
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

class ViewAttendanceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewAttendanceBinding
    private val viewModel: ViewAttendanceViewModel by viewModels()
    private lateinit var adapter: ViewAttendanceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize adapter
        adapter = ViewAttendanceAdapter(emptyList())
        binding.rvAttendanceList.layoutManager = LinearLayoutManager(this)
        binding.rvAttendanceList.adapter = adapter

        // Observe filtered list
        viewModel.filteredAttendanceList.observe(this) { filteredList ->
            adapter.updateList(filteredList)
        }

        // 🌟 Show current date's attendance on launch
        val currentDate = getCurrentDate()
        binding.tvSelectedDate.text = currentDate
        viewModel.filterAttendanceByDate(currentDate)

        // Date Picker setup
        binding.btnSelectDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, y, m, d ->
                val selectedDate = String.format("%02d-%02d-%04d", d, m + 1, y)
                binding.tvSelectedDate.text = selectedDate
                viewModel.filterAttendanceByDate(selectedDate)
            }, year, month, day)

            datePicker.show()
        }

        // Export CSV
        binding.btnExportCsv.setOnClickListener {
            checkPermissionAndExportCSV()
        }
    }

    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

    private fun checkPermissionAndExportCSV() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(permission), 1001)
            } else {
                exportAttendanceToCSV()
            }
        } else {
            exportAttendanceToCSV()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            exportAttendanceToCSV()
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun exportAttendanceToCSV() {
        val list = viewModel.filteredAttendanceList.value
        if (list.isNullOrEmpty()) {
            Toast.makeText(this, "No attendance data to export", Toast.LENGTH_SHORT).show()
            return
        }

        val csvHeader = "Name,Employee ID,Date,Status"
        val csvBody = StringBuilder()
        csvBody.appendLine(csvHeader)

        for (record in list) {
            val name = record.employee.name
            val empId = record.employee.id.toString()
            val date = record.attendance.date
            val status = if (record.attendance.isPresent) "Present" else "Absent"
            csvBody.appendLine("$name,$empId,$date,$status")
        }

        try {
            val fileName = "Attendance_Report_${System.currentTimeMillis()}.csv"
            val downloadsDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, fileName)

            val writer = FileWriter(file)
            writer.write(csvBody.toString())
            writer.flush()
            writer.close()

            Toast.makeText(this, "CSV exported to ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Export failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}
