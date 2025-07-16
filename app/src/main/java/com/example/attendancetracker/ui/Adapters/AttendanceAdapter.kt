package com.example.attendancetracker.ui.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.attendancetracker.databinding.ItemMarkAttendanceBinding
import com.example.attendancetracker.model.Employee

class AttendanceAdapter(
    private val employeeList: List<Employee>
) : RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder>() {

    // Map to track present/absent status
    private val attendanceMap = mutableMapOf<Int, Boolean>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val binding = ItemMarkAttendanceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AttendanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val employee = employeeList[position]
        holder.bind(employee)
    }

    override fun getItemCount(): Int = employeeList.size

    inner class AttendanceViewHolder(
        private val binding: ItemMarkAttendanceBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(employee: Employee) {
//            binding.tvEmployeeName.text = employee.name
            binding.tvEmployeeName.text = "${employee.name} (ID: ${employee.id})"

            // Checkbox listener
            binding.cbPresent.setOnCheckedChangeListener(null)
            binding.cbPresent.isChecked = attendanceMap[employee.id] ?: false

            binding.cbPresent.setOnCheckedChangeListener { _, isChecked ->
                attendanceMap[employee.id] = isChecked
            }
        }
    }

    // Function to return attendance result
    fun getMarkedAttendance(): Map<Int, Boolean> {
        return attendanceMap
    }
}
