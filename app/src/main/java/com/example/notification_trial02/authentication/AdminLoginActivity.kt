package com.example.notification_trial02.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.notification_trial02.AdminSideActivity.AdminHomeActivity
import com.example.notification_trial02.ClientSideActivities.HomeActivity
import com.example.notification_trial02.R
import com.example.notification_trial02.databinding.ActivityAdminLoginBinding
import com.example.notification_trial02.modals.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AdminLoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAdminLoginBinding
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val TAG = "AdminLogIn"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.etAdminemail.addTextChangedListener{
            binding.etAdminemail.error = null
        }

        binding.etAdminpassword.addTextChangedListener{
            binding.etAdminpassword.error = null
        }

        binding.adminLoginBtn.setOnClickListener {
            binding.etAdminemail.error = null
            binding.etAdminpassword.error = null

            val email = binding.etAdminemail.text.toString()
            val password = binding.etAdminpassword.text.toString()

            if (validate(email, password)) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {

                        val uid = auth.currentUser?.uid.toString()

//                        var flag = false
                        GlobalScope.launch(Dispatchers.IO) {
                            db.collection("users").document(uid)
                                .get()
                                .addOnSuccessListener {
                                    val data = it.toObject(User::class.java)
//                                    val data = it
                                    Log.d(TAG, "data from storage is " + data)

//                                    flag = (data == null)
                                    if (data == null){
                                        val intent =
                                            Intent(this@AdminLoginActivity, AdminHomeActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }else{
                                        Log.d(TAG, "user is client")
                                        Toast.makeText(this@AdminLoginActivity, "Please login through client portal", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.d(TAG, "user is client : addOnFailureListener")
                                    val intent =
                                        Intent(this@AdminLoginActivity, AdminHomeActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }

//                            launch(Dispatchers.Main) {
//                                Log.d(TAG, "so flag is " + flag.toString())
//
//                                if (flag == true){
//                                    val intent =
//                                        Intent(this@AdminLoginActivity, AdminHomeActivity::class.java)
//                                    startActivity(intent)
//                                    finish()
//                                }
//                                else{
//                                    Toast.makeText(this@AdminLoginActivity, "Please login through client portal", Toast.LENGTH_SHORT).show()
//                                }
//                            }
                        }
                    }
                    .addOnFailureListener {
                        Log.e(TAG, "onCreate: ${it.message}")
                        Toast.makeText(this, "Please check your email or password correctly", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun validate(email: String, password: String): Boolean {
        var valid = true

        if (email.isBlank()){
            binding.etAdminemail.error = "Please enter the email"
            valid = false
        }
        if(!isValidEmail(binding.etAdminemail.text!!)){
            binding.etAdminemail.error = "Please enter a valid email address"
            valid = false
        }
        if (password.isBlank()){
            binding.etAdminpassword.error = "Please enter the password"
            valid = false
        }
        if(password.length < 6){
            binding.etAdminpassword.error = "Password length should be minimum of 6 characters"
            valid = false
        }
        return valid
    }

    private fun isValidEmail(email: CharSequence): Boolean {
        if (TextUtils.isEmpty(email)) {
            return false;
        }else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }
}