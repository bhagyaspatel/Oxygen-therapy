package com.example.notification_trial02.clientSideFragments

import android.app.AlertDialog
import android.content.DialogInterface
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
import com.example.notification_trial02.topic
import org.json.JSONException
import org.json.JSONObject

class SendNotification : Fragment() {

    private lateinit var binding : FragmentSendNotificationBinding
    private val token = "eqn1jMYEQ1eVP2p9wSMi0w:APA91bFz5KpUeaEDyhdeFM3kvxyNNZkB8ceBkKOtiLhcrjUhvtEkcmMeG7LGLhO37koigo0HWdiezK9-Rc0OxqQEyqcqVQ82eZXlJP8g5BCdO03sWh5VKoLc3RyVjLjgvEEqZrohNW9_"
    private val token2 = "doBcozDpSPWEEjKI9zQNd7:APA91bHRLGtmzoC-H_9qh7U_ciJpIdo3bqD1XlNCKYMvX_kZvsPpqzBkYtZILcejm1RWpClV27-SssTgLhNPgfzzPiUu-QiWUBTLQ8P4X4nmIPcLxV0YgfT9dkkX2OH6KkE-iZUeu3lH"
    private val token3 = "cMLrdC6ZT2CVf6pWN7kzBO:APA91bGga1e0FQmnDpp8SRcKfcGELhpRry6iJUwTKkfIJS6fAmNJ9SMEPndv3KQyyPn85sCQigKrMtR4JN-QLwHAlRBNs8oWSm7BNaGYVhY4dK1bjilpOiPlIRcrehUFKWevFTRDA7og"
    //token specifies the device to whome we are sending the notification : admin side token


    private val TAG = "Send_notification_message"
    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey = "key=" + "AAAAn9D3lUI:APA91bFY77zjRgxg6xcVdMYezF2a2kn0LcCgPeQue_p7Vrk3IY5s1yuS3KXcqLp_t9sCLi3JrnxQr9T2dPdeEn8PFc8HCgE5gqT1sJUSeUmYLkt6TNXa1XE0VVWPJQgPcwvCrwtyfLwT"
    //server key is going to be the same for all hospitals

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

        binding.etName.setOnFocusChangeListener { view, focus ->
            if (!focus)
                binding.nameLabel.helperText = validName()
        }

        binding.etAge.setOnFocusChangeListener { view, focus ->
            if (!focus)
                binding.ageLabel.helperText = validAge()
        }

        binding.etSex.setOnFocusChangeListener { view, focus ->
            if (!focus)
                binding.sexLabel.helperText = validSex()
        }

        binding.btn.setOnClickListener {
            Log.d(TAG, "Send Btn pressed")
            submitDetail()
        }
    }

    private fun submitDetail() {
        val validName = binding.nameLabel.helperText == null
        val validAge = binding.ageLabel.helperText == null
        val validPhone = binding.sexLabel.helperText == null

        if (validName && validAge && validPhone){
            Log.d(TAG, "All details variifed")

            val sex = binding.etSex.text.toString()
            val age = binding.etAge.text.toString()
            val name = binding.etName.text.toString()

            AlertDialog.Builder(requireContext())
                .setTitle("Are you sure?")
                .setMessage("Click OK to send the alert for oxygen therapy")
                .setPositiveButton("OK"){ dialog, _ ->
                    createNotificationObject(name, age, sex)
                }
                .show()
        }else{
            Toast.makeText(requireContext(), "First provide all the required details of the patient", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validSex(): CharSequence? {
        val sex = binding.etSex.text.toString()

        if (sex.isNullOrEmpty())
            return "Please provide patient's sex"
        return null
    }

    private fun validAge(): CharSequence? {
        val age = binding.etAge.text.toString()

        if(age.isNullOrEmpty() || !age.matches(".*[0-9].*".toRegex()))
            return "Age should be a number"
        return null
    }

    private fun validName(): CharSequence? {
        val name = binding.etName.text.toString()

        if (name.isNullOrEmpty())
            return "Please provide patient's name"
        return null
    }

    private fun createNotificationObject(name: String, age : String, sex : String) {
        val notification = JSONObject()
        val notificationBody = JSONObject()
        val msg = "Urgent Oxygen Therapy Needed"

        try{
            notificationBody.put("title", "Oxygen therapy needed")
            notificationBody.put("message", msg)
            notificationBody.put("name", name)
            notificationBody.put("age", age)
            notificationBody.put("sex", sex)

            notification.put("to", topic) ///####
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