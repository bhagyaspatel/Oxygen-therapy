package com.example.notification_trial02

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.notification_trial02.ClientSideActivities.HomeActivity
import com.example.notification_trial02.authentication.LoginActivity
import com.example.notification_trial02.authentication.SignupActivity
import com.example.notification_trial02.databinding.ActivityMainBinding
import com.example.notification_trial02.modals.UserType
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


val topic = "/topics/oxygen"

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
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
            var type: UserType? = null
            val uid = user.uid

//            GlobalScope.launch(Dispatchers.IO) {
//                db.collection("users").document(uid)
//                    .get()
//                    .addOnSuccessListener {
////                        val data = it.toObject(UserType::class.java)
//                        val data = it
//                        Log.d(TAG, "data from storage is " + data)
//
//                        if (data == null) {
//                            Log.d(TAG, "if data is null..")
//                            Toast.makeText(
//                                this@MainActivity,
//                                "Please login through admin portal",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        } else {
//                            Log.d(TAG, "if data is not null..")
//                            val intent =
//                                Intent(this@MainActivity, HomeActivity::class.java)
//                            startActivity(intent)
//                            finish()
//                        }
//                    }
//                    .addOnFailureListener { exception ->
//                        val intent =
//                            Intent(this@MainActivity, HomeActivity::class.java)
//                        startActivity(intent)
//                        finish()
//                    }
                Log.d("MainActivity", "User not null")
                val intent = Intent (this, LoginActivity::class.java)
                startActivity(intent)
                finish()
//            }
        }

//        val flag = getIntent().getStringExtra("Flag")
//
//        if (flag == "Admin"){
//            addFragmentToActivity(supportFragmentManager, PretherapyFormDetails(), binding.container.id)
//        }else{
//            addFragmentToActivity(supportFragmentManager, SendNotification(), binding.container.id)
//        }
    }

    private fun addFragmentToActivity(supportFragmentManager: FragmentManager, fragment: Fragment? , frameId: Int) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        if (fragment != null) {
            transaction.add(frameId, fragment)
        }
        transaction.commit()
    }
}