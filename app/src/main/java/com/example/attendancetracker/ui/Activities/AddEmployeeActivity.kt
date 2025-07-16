package com.example.attendancetracker.ui.Activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.attendancetracker.database.AppDatabase
import com.example.attendancetracker.databinding.ActivityAddEmployeeBinding
import com.example.attendancetracker.model.Employee
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddEmployeeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEmployeeBinding
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Image Picker
        binding.imgProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 101)
        }

        // Save Button
        binding.btnSave.setOnClickListener {
            val name = binding.etEmpName.text.toString()
            val designation = binding.etDesignation.text.toString()
            val phone = binding.etPhone.text.toString()

            if (name.isEmpty() || designation.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val employee = Employee(
                name = name,
                designation = designation,
                phoneNumber = phone,
                profileUri = selectedImageUri?.toString()
            )

            CoroutineScope(Dispatchers.IO).launch {
                AppDatabase.getDatabase(this@AddEmployeeActivity)
                    .employeeDao()
                    .insertEmployee(employee)

                runOnUiThread {
                    Toast.makeText(this@AddEmployeeActivity, "Employee Saved", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            binding.imgProfile.setImageURI(selectedImageUri)
        }
    }
}
