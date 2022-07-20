package com.example.notification_trial02

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.example.notification_trial02.AdminSideActivity.AdminHomeActivity
import com.example.notification_trial02.ClientSideActivities.HomeActivity
import com.example.notification_trial02.ViewModal.PateintViewModel
import com.example.notification_trial02.authentication.AdminLoginActivity
import com.example.notification_trial02.authentication.HospitalDetailsActivity
import com.example.notification_trial02.authentication.LoginActivity
import com.example.notification_trial02.authentication.SignupActivity
import com.example.notification_trial02.databinding.ActivityMainBinding
import com.example.notification_trial02.modals.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

val topic = "/topics/oxygen"
val FORM = "PreTherapyForm"
val PENDING_PATIENT_LIST = "PendingPatient"
val PENDING = "PendingPatient"
val DONE = "DonePatient"
val PRESCRIPTION = "PatientPrescription"
val USER = "Users"

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivityTAG"
    private lateinit var binding: ActivityMainBinding
    val user = FirebaseAuth.getInstance().currentUser
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (user == null){
            Log.d("MainActivity", "User null")
            val intent = Intent (this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }else {
            val uid = user.uid
            Log.d(TAG, "onCreate: $uid")
            Log.d(TAG, "onCreate: else block")

            GlobalScope.launch(Dispatchers.IO) {

                Log.d(TAG, "onCreate: MainActivity $uid" )
                db.collection(USER).document(uid)
                    .get()
                    .addOnSuccessListener {
                        val data = it.toObject(User::class.java)
                        Log.d(TAG, "data from storage is " + data)

                        if (data == null){
                            Log.d(TAG, "data from db is null so this is admin")
                            Toast.makeText(this@MainActivity, "Please login through admin portal", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@MainActivity, AdminHomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        else{
                            Log.d(TAG, "login me data not null matlab this is client")
                            val intent =
                                Intent(this@MainActivity, HomeActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this@MainActivity, "Unable to fetch details.Check internet connectivity", Toast.LENGTH_SHORT).show()
//                        val intent =
//                            Intent(this@MainActivity, AdminHomeActivity::class.java)
//                        startActivity(intent)
//                        finish()
                    }
            }
        }
    }

    private fun addFragmentToActivity(supportFragmentManager: FragmentManager, fragment: Fragment? , frameId: Int) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        if (fragment != null) {
            transaction.add(frameId, fragment)
        }
        transaction.commit()
    }
}