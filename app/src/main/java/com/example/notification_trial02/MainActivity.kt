package com.example.notification_trial02

import android.app.Activity
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.notification_trial02.adminSideFragments.PretherapyFormDetails
import com.example.notification_trial02.authentication.LoginActivity
import com.example.notification_trial02.authentication.SignupActivity
import com.example.notification_trial02.clientSideFragments.SendNotification
import com.example.notification_trial02.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (user == null){
            val intent = Intent (this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            val intent = Intent (this, LoginActivity::class.java)
            startActivity(intent)
            finish()
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