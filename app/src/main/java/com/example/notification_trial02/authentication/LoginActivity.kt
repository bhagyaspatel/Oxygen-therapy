package com.example.notification_trial02.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.example.notification_trial02.MainActivity
import com.example.notification_trial02.R
import com.example.notification_trial02.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private val TAG = "Login"
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        auth = FirebaseAuth.getInstance()
        binding.signupBtn.setOnClickListener {
            binding.etEmail.error = null
            binding.etPassword.error = null

            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (validate(email, password)) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener {
                        Log.e(TAG, "onCreate: ${it.message}",)
                    }
            }
        }
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