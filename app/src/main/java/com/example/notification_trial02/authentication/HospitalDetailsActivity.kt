package com.example.notification_trial02.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.notification_trial02.ClientSideActivities.HomeActivity
import com.example.notification_trial02.USER
import com.example.notification_trial02.databinding.ActivityHospitalDetailsBinding
import com.example.notification_trial02.databinding.ActivityHospitalDetailsBinding.inflate
import com.example.notification_trial02.modals.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HospitalDetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityHospitalDetailsBinding
    private val TAG = "Hospital_Detail"
    private var auth = FirebaseAuth.getInstance()
    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHospitalDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.etName.setOnFocusChangeListener { view, focus ->
            if (!focus)
                binding.nameLabel.helperText = validName()
        }

        binding.etArea.setOnFocusChangeListener { view, focus ->
            if (!focus)
                binding.areaLabel.helperText = validArea()
        }

        binding.etNumber.setOnFocusChangeListener { view, focus ->
            if (!focus)
                binding.hospitalNumLabel.helperText = validNumber()
        }

        binding.saveBtn.setOnClickListener{
            Log.d(TAG, "Save Btn pressed")
            submitDetail()
        }
    }

    private fun submitDetail() {
        val validName = binding.nameLabel.helperText == null
        val validArea = binding.areaLabel.helperText == null
        val validNumber = binding.hospitalNumLabel.helperText == null

        if (validName && validArea && validNumber) {
            Log.d(TAG, "All details variifed")

            val name = binding.etName.text.toString()
            val area = binding.etArea.text.toString()
            val number = binding.etNumber.text.toString()

            //TODO : Save this details to the db and navigate to HomeActivity
            val newUser = User(name, number, area)

            auth.currentUser?.let { user->
                db.collection(USER).document(user.uid.toString())
                    .set(newUser)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Details saved successfully", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener{
                        Toast.makeText(this, "Error occurred while saving the details", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun validNumber(): CharSequence? {
        val num = binding.etNumber.text.toString()

        if (num.isEmpty())
            return "Please provide hospital number"
        return null
    }

    private fun validArea(): CharSequence? {
        val area = binding.etArea.text.toString()

        if(area.isEmpty())
            return "Please provide the area of locality"
        return null
    }

    private fun validName(): CharSequence? {
        val name = binding.etName.text.toString()

        if (name.isEmpty())
            return "Please provide name of the hospital"
        return null
    }
}