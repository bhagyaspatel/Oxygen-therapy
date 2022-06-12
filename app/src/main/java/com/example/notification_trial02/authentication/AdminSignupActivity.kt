package com.example.notification_trial02.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.notification_trial02.AdminSideActivity.AdminHomeActivity
import com.example.notification_trial02.MainActivity
import com.example.notification_trial02.R
import com.example.notification_trial02.adminSideFragments.PretherapyFormDetails
import com.example.notification_trial02.databinding.ActivityAdminSignupBinding
import com.google.firebase.auth.FirebaseAuth

class AdminSignupActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAdminSignupBinding
    private val auth = FirebaseAuth.getInstance()
    private val TAG = "AdminSignUp"

    override fun onCreate(savedInstanceState: Bundle
    ?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminSignupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.etEmail.addTextChangedListener{
            binding.etEmail.error = null
        }

        binding.etPassword.addTextChangedListener{
            binding.etPassword.error = null
        }

        binding.adminLoginBtn.setOnClickListener {
            binding.etEmail.error = null
            binding.etPassword.error = null

            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (validate(email, password)) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) {
                        val intent = Intent(this, AdminHomeActivity::class.java)
//                        intent.putExtra("Flag", "Admin")
                        startActivity(intent)
                        finish()
//                        addFragmentToActivity(supportFragmentManager, PretherapyFormDetails(), binding.adminSignupContainer.id)
                    }
                    .addOnFailureListener {
                        Log.e(TAG, "onCreate: ${it.message}",)
                    }
            }
        }
    }

    private fun addFragmentToActivity(supportFragmentManager: FragmentManager, fragment: Fragment?, frameId: Int) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        if (fragment != null) {
            transaction.add(frameId, fragment)
        }
        transaction.commit()
    }

    private fun validate(email: String, password: String): Boolean {
        var valid = true

        if (email.isBlank()){
            binding.etEmail.error = "Please enter the email"
            valid = false
        }
        if(!isValidEmail(binding.etEmail.text!!)){
            binding.etEmail.error = "Please enter a valid email address"
            valid = false
        }
        if (password.isBlank()){
            binding.etPassword.error = "Please enter the password"
            valid = false
        }
        if(password.length < 6){
            binding.etPassword.error = "Password length should be minimum of 6 characters"
            valid = false
        }
        return valid
    }

    private fun isValidEmail(email: CharSequence): Boolean {
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }
}