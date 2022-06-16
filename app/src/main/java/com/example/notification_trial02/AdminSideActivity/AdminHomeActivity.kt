package com.example.notification_trial02.AdminSideActivity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.notification_trial02.R
import com.example.notification_trial02.topic
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

class AdminHomeActivity : AppCompatActivity() {
    private val TAG = "AdminHomeActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        Firebase.messaging.subscribeToTopic(topic)
            .addOnCompleteListener{ task ->
                var msg = "Subscribed"
                if(!task.isSuccessful){
                    msg = "Subscribe fail"
                }
                Log.d(TAG, msg)
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(name, context, attrs)

    }
}