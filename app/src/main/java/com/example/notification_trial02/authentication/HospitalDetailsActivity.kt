package com.example.notification_trial02.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.notification_trial02.ClientSideActivities.HomeActivity
import com.example.notification_trial02.ViewModal.PateintViewModel
import com.example.notification_trial02.databinding.ActivityHospitalDetailsBinding
import com.example.notification_trial02.modals.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HospitalDetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityHospitalDetailsBinding
    private val TAG = "Hospital_Detail"
    private var auth = FirebaseAuth.getInstance()

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(PateintViewModel::class.java)
    }

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

        binding.saveBtn.setOnClickListener{
            Log.d(TAG, "Save Btn pressed")
//            TODO() : ADD progress bar here
            submitDetail()
        }
    }

    private fun submitDetail() {
        val validName = binding.nameLabel.helperText == null
        val validArea = binding.areaLabel.helperText == null

        if (validName && validArea) {
            Toast.makeText(this, "Please wait", Toast.LENGTH_SHORT).show()
            binding.saveBtn.isClickable = false
            Log.d(TAG, "All details varified")

            val name = binding.etName.text.toString()
            val area = binding.etArea.text.toString()

            //TODO : Save this details to the db and navigate to HomeActivity
            val newUser = User(name,area)

            val email = intent.getStringExtra("email")
            val password = intent.getStringExtra("password")

            Log.d(TAG, "submitDetail: the pawd and email from intent is $password , $email")

            auth.createUserWithEmailAndPassword(email!!, password!!)
                .addOnSuccessListener {
                    Log.d(TAG, "signed in")
                    if (auth.currentUser == null){
                        Log.d(TAG, "current user is null");
                    }else{
                        Log.d(TAG, "current user not null");
                        viewModel.saveUser(newUser, auth.currentUser!!.uid)
                        viewModel.flag.observe(this, Observer{
                            Log.d(TAG, "submitDetail: live flag is $it")
                            if (it == true){
                                val intent = Intent(this, HomeActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        })
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "The email address is already in use by another account.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, SignupActivity::class.java)
                    startActivity(intent)
                    finish()
                    Log.e(TAG, "onCreate: ${it.message}",)
                }
        }
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