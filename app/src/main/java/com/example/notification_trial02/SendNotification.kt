package com.example.notification_trial02

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.notification_trial02.databinding.FragmentSendNotificationBinding
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONException
import org.json.JSONObject

class SendNotification : Fragment() {

    private lateinit var binding : FragmentSendNotificationBinding
    private val token = "eqn1jMYEQ1eVP2p9wSMi0w:APA91bFz5KpUeaEDyhdeFM3kvxyNNZkB8ceBkKOtiLhcrjUhvtEkcmMeG7LGLhO37koigo0HWdiezK9-Rc0OxqQEyqcqVQ82eZXlJP8g5BCdO03sWh5VKoLc3RyVjLjgvEEqZrohNW9_"
    private val TAG = "Send_notification_message"
    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey = "key=" + "AAAAn9D3lUI:APA91bFY77zjRgxg6xcVdMYezF2a2kn0LcCgPeQue_p7Vrk3IY5s1yuS3KXcqLp_t9sCLi3JrnxQr9T2dPdeEn8PFc8HCgE5gqT1sJUSeUmYLkt6TNXa1XE0VVWPJQgPcwvCrwtyfLwT"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSendNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        FirebaseMessaging.getInstance().subscribeToTopic("/topics/Enter_your_topic_name")

        val msg = binding.etMsg.text.toString()

        binding.btn.setOnClickListener {
            if ((msg.isEmpty())){
                createNotificationObject(msg)
            }else{
                Toast.makeText(requireContext(), "Please write message first", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun createNotificationObject(msg: String) {
        val notification = JSONObject()
        val notificationBody = JSONObject()

        try{
            notificationBody.put("title", "Oxygen therapy needed")
            notificationBody.put("message", msg)
            notification.put("to", token)
            notification.put("data", notificationBody)
            Log.d(TAG, "try")
        }catch (e: JSONException){
            Log.d(TAG, "onCreate: " + e.message)
        }

        sendNotification(notification)
    }

    private fun sendNotification(notification: JSONObject) {
        Log.d(TAG, "sendNotification")

        val jsonObjectRequest = object : JsonObjectRequest(FCM_API, notification,
            Response.Listener <JSONObject> { response ->
                Log.d("TAG", "onResponse: $response")
            },
            Response.ErrorListener {
                Toast.makeText(requireContext(), "Request error", Toast.LENGTH_LONG).show()
                Log.d(TAG, "onErrorResponse: Didn't work")
            }){

            override fun getHeaders(): MutableMap<String, String> {
                super.getHeaders()

                val params = mutableMapOf<String, String>()
                params["Authorization"] = serverKey
                params["Content-Type"] = "application/json"
                return params
            }
        }
        requestQueue.add(jsonObjectRequest)
    }

    private val requestQueue : RequestQueue by lazy {
        Volley.newRequestQueue(requireContext())
    }
}