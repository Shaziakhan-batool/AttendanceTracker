package com.example.attendancetracker.ui.Adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.attendancetracker.R
import com.example.attendancetracker.model.AttendanceWithEmployee

class ViewAttendanceAdapter(
    private var attendanceList: List<AttendanceWithEmployee>
) : RecyclerView.Adapter<ViewAttendanceAdapter.AttendanceViewHolder>() {

    inner class AttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvEmployeeName)
        val tvStatus: TextView = itemView.findViewById(R.id.tvAttendanceStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_attendance, parent, false)
        return AttendanceViewHolder(view)
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val attendance = attendanceList[position]
        holder.tvName.text = attendance.employee.name
        holder.tvStatus.text = if (attendance.attendance.isPresent) "✅ Present" else "❌ Absent"
    }

    override fun getItemCount(): Int = attendanceList.size
    // ✅ Add this function to update the list
    fun updateList(newList: List<AttendanceWithEmployee>) {
        attendanceList = newList
        notifyDataSetChanged()
    }
}
