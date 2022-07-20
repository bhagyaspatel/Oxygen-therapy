package com.example.notification_trial02.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.notification_trial02.ClientSideActivities.HomeActivity
import com.example.notification_trial02.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private val TAG = "Login"
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth : FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.switchAdminBtn.setOnClickListener {
            val intent = Intent (this, AdminLoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnGoSignup.setOnClickListener {
            val intent = Intent (this, SignupActivity::class.java)
            startActivity(intent)
        }

        auth = FirebaseAuth.getInstance()
        binding.signupBtn.setOnClickListener {
            binding.etLoginemail.error = null
            binding.etLoginpassword.error = null

            val email = binding.etLoginemail.text.toString()
            val password = binding.etLoginpassword.text.toString()

            if (validate(email, password)) {
                Log.d(TAG, "Email and pass are correctly entered")

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) {
                        if (it.isSuccessful){
                            val uid = auth.currentUser?.uid.toString()
//                            val documentId = CLIENT + uid

                            val intent =
                                Intent(this@LoginActivity, HomeActivity::class.java)
                            startActivity(intent)
                            finish()

//                            GlobalScope.launch(Dispatchers.IO) {
//                                db.collection(USER).document(uid)
//                                    .get()
//                                    .addOnSuccessListener{
//                                        val data = it.toObject(User::class.java)
//                                        Log.d(TAG, "data from storage is " + data)
//
//                                        if (data == null && (uid.substring(0,5) == CLIENT)){
//                                            Log.d(TAG, "login me data null matlab hospital detail nahi de rakhi.")
//                                            Toast.makeText(this@LoginActivity, "Please provide hospital's detail", Toast.LENGTH_SHORT).show()
//                                            val intent =
//                                                Intent(this@LoginActivity, HospitalDetailsActivity::class.java)
//                                            startActivity(intent)
//                                            finish()
//                                        }
//                                        else if (data == null && (uid.substring(0,5) != CLIENT)){
//                                            Log.d(TAG, "this is the admin.")
//                                            Toast.makeText(this@LoginActivity, "Please login through admin portal", Toast.LENGTH_SHORT).show()
//                                        }
//                                        else{
//                                            Log.d(TAG, "login me data not null matlab hospital detail hai")
//                                            val intent =
//                                                Intent(this@LoginActivity, HomeActivity::class.java)
//                                            startActivity(intent)
//                                            finish()
//                                        }
//                                    }
//                                    .addOnFailureListener { exception ->
//                                        Toast.makeText(this@LoginActivity, "Log in failed", Toast.LENGTH_SHORT).show()
//                                    }
//                            }
                        }
                        else{
                            Toast.makeText(this, "Log in failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Log.e(TAG, "onCreate: ${it.message}",)
                        Toast.makeText(this, "Please check your email or password correctly", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun validate(email: String, password: String): Boolean {
        var valid = true

        if (email.isBlank()){
            binding.etLoginemail.error = "Please enter the email"
            valid = false
        }
        if(!isValidEmail(binding.etLoginemail.text!!)){
            binding.etLoginemail.error = "Please enter a valid email address"
            valid = false
        }
        if (password.isBlank()){
            binding.etLoginpassword.error = "Please enter the password"
            valid = false
        }
        if(password.length < 6){
            binding.etLoginpassword.error = "Password length should be minimum of 6 characters"
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