//package com.example.notification_trial02.authentication
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.text.TextUtils
//import android.util.Log
//import android.widget.Toast
//import androidx.core.widget.addTextChangedListener
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentManager
//import androidx.fragment.app.FragmentTransaction
//import com.example.notification_trial02.AdminSideActivity.AdminHomeActivity
//import com.example.notification_trial02.ClientSideActivities.HomeActivity
//import com.example.notification_trial02.MainActivity
//import com.example.notification_trial02.R
//import com.example.notification_trial02.adminSideFragments.PretherapyFormDetails
//import com.example.notification_trial02.databinding.ActivityAdminSignupBinding
//import com.example.notification_trial02.modals.User
//import com.example.notification_trial02.modals.UserType
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//
//class AdminSignupActivity : AppCompatActivity() {
//
//    private lateinit var binding : ActivityAdminSignupBinding
//    private val auth = FirebaseAuth.getInstance()
//    private val db = FirebaseFirestore.getInstance()
//    private val TAG = "AdminSignUp"
//
//    override fun onCreate(savedInstanceState: Bundle
//    ?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityAdminSignupBinding.inflate(layoutInflater)
//        val view = binding.root
//        setContentView(view)
//
//        binding.etEmail.addTextChangedListener{
//            binding.etEmail.error = null
//        }
//
//        binding.etPassword.addTextChangedListener{
//            binding.etPassword.error = null
//        }
//
//        binding.btnGoLogin.setOnClickListener {
//            val intent = Intent(this, AdminLoginActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//
//        binding.signupBtn.setOnClickListener {
//            binding.etEmail.error = null
//            binding.etPassword.error = null
//
//            val email = binding.etEmail.text.toString()
//            val password = binding.etPassword.text.toString()
//
//            if (validate(email, password)) {
//                auth.createUserWithEmailAndPassword(email, password)
//                    .addOnSuccessListener {
//
//                        val newUser = User(UserType.ADMIN)
//
//                        auth.currentUser?.let { user->
//                            db.collection("users").document(user.uid.toString())
//                                .set(newUser)
//                        }
//
//                        if (auth.currentUser == null){
//                            Log.d(TAG, "current user is null");
//                        }else{
//                            Log.d(TAG, "current user not null");
//                            val intent = Intent(this, HomeActivity::class.java)
//                            startActivity(intent)
//                            finish()
//                        }
//
//                        val uid = auth.currentUser?.uid.toString()
//                        var type : UserType ? = null
//                        db.collection("users").document(uid)
//                            .get()
//                            .addOnSuccessListener{ data ->
//                                type = data.toObject(UserType::class.java)
//                                Log.d(TAG, type.toString())
//                            }
//                            .addOnFailureListener { exception ->
//                                Log.d(TAG, "Error getting documents: ", exception)
//                            }
//
//                        if (type == UserType.CLIENT){
//                            Toast.makeText(this, "Please login through client portal", Toast.LENGTH_SHORT).show()
//                            Log.d(TAG, "user type is client : ")
//                        }
//                        else{
//                            Log.d(TAG, "Email and pass varified, user is admin")
//                            val intent = Intent(this, AdminHomeActivity::class.java)
//                            startActivity(intent)
//                            finish()
//                        }
//                    }
//                    .addOnFailureListener {
//                        Log.e(TAG, "onCreate: ${it.message}",)
//                    }
//            }
//        }
//    }
//
//    private fun validate(email: String, password: String): Boolean {
//        var valid = true
//
//        if (email.isBlank()){
//            binding.etEmail.error = "Please enter the email"
//            valid = false
//        }
//        if(!isValidEmail(binding.etEmail.text!!)){
//            binding.etEmail.error = "Please enter a valid email address"
//            valid = false
//        }
//        if (password.isBlank()){
//            binding.etPassword.error = "Please enter the password"
//            valid = false
//        }
//        if(password.length < 6){
//            binding.etPassword.error = "Password length should be minimum of 6 characters"
//            valid = false
//        }
//        return valid
//    }
//
//    private fun isValidEmail(email: CharSequence): Boolean {
//        if (TextUtils.isEmpty(email)) {
//            return false;
//        }else {
//            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
//        }
//    }
//}