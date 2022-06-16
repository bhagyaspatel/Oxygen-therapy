package com.example.notification_trial02.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.notification_trial02.ClientSideActivities.HomeActivity
import com.example.notification_trial02.MainActivity
import com.example.notification_trial02.R
import com.example.notification_trial02.adminSideFragments.PretherapyFormDetails
import com.example.notification_trial02.clientSideFragments.SendNotification
import com.example.notification_trial02.databinding.ActivitySignupBinding
import com.example.notification_trial02.modals.User
import com.example.notification_trial02.modals.UserType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {

    private val TAG = "Signup"
    lateinit var binding: ActivitySignupBinding
    private var db = FirebaseFirestore.getInstance()
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.switchAdminBtn.setOnClickListener {
            val intent = Intent (this, AdminLoginActivity::class.java)
            startActivity(intent)
        }

        binding.etEmail.addTextChangedListener{
            binding.etEmail.error = null
        }

        binding.etPassword.addTextChangedListener{
            binding.etPassword.error = null
        }

        binding.etConfirmPassword.addTextChangedListener{
            binding.etConfirmPassword.error = null
        }

        binding.btnGoLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        auth = FirebaseAuth.getInstance()

        binding.signupBtn.setOnClickListener {

            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()

            if (validate(email, password, confirmPassword)) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
//                           auth.currentUser?.sendEmailVerification()
//                                ?.addOnCompleteListener { task ->
//                                    if (task.isSuccessful) {
//                                        Toast.makeText(
//                                            this,
//                                            "User registered successfully please verify your email-id",
//                                            Toast.LENGTH_SHORT
//                                        ).show()
//                                    } else {
//                                        Log.d(
//                                            TAG,
//                                            "task unsuccessful " + task.exception?.message.toString()
//                                        )
//                                    }
//                              }
                        Log.d(TAG, "signed in")
                        val newUser = User(UserType.CLIENT)

                        auth.currentUser?.let { user->
                            db.collection("users").document(user.uid.toString())
                                .set(newUser)
                        }

                        if (auth.currentUser == null){
                            Log.d(TAG, "current user is null");
                        }else{
                            Log.d(TAG, "current user not null");
                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "The email address is already in use by another account.", Toast.LENGTH_SHORT).show()
                        Log.e(TAG, "onCreate: ${it.message}",)
                    }
                }
//          }
        }

    }

    private fun addFragmentToActivity(supportFragmentManager: FragmentManager, fragment: Fragment?, frameId: Int) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        if (fragment != null) {
            transaction.add(frameId, fragment)
        }
        transaction.commit()
    }


    private fun validate(email: String, password: String, confirmPassword: String): Boolean {
        var valid = true

        if (email.isBlank()){
            binding.etEmail.error = "Please enter the email"
            return false
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
        if (confirmPassword.isBlank()){
            binding.etPassword.error = "Please enter the password again to confirm"
            valid = false
        }
        if (confirmPassword != password){
            binding.etPassword.error = "Password does not matched with the above password"
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