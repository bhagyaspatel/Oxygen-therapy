package com.example.notification_trial02

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.notification_trial02.modals.PatientAndHospital
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val ADMIN_CHANNEL_ID = "admin_channel"
    private val TAG = "NotificationTAG"

    private var db = FirebaseFirestore.getInstance()

    override fun onNewToken(token: String){
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Log.d(TAG, "Message recieved")

        if (remoteMessage.notification != null){
            Log.d(TAG, "notification not null")
            val name = remoteMessage.data.get("name")
            val age = remoteMessage.data.get("age")
            val sex = remoteMessage.data.get("sex")
            val hospitalNumber = remoteMessage.data.get("hospitalNumber")
            val area = remoteMessage.data.get("area")
            generateNotifictaion(remoteMessage.notification!!.title!!, remoteMessage.notification!!.body!!,
                name!!, age!!, sex!!, hospitalNumber!!, area!!)
        }

        if (remoteMessage.data.size > 0){ //when sending to particular user
            val title = remoteMessage.data.get("title")
            val message = remoteMessage.data.get("message")
            val name = remoteMessage.data.get("name")
            val age = remoteMessage.data.get("age")
            val sex = remoteMessage.data.get("sex")
            val hospitalNumber = remoteMessage.data.get("hospitalNumber")
            val area = remoteMessage.data.get("area")

            Log.d(TAG, "title and message dispatched $title and $message")
            generateNotifictaion(title!!, message!!, name!!, age!!, sex!!, hospitalNumber!!, area!!)
        }
    }

    private fun generateNotifictaion(title : String, message : String, name : String?, age : String?,
                                     sex : String?, hospitalNumber : String?, area : String?){

        Log.d(TAG, "generate Notification() $title and $message")
        Log.d(TAG, "generate Notification() $name and $age and $sex")

        if (age != null && name != null && sex!= null) {
            storePatient(name,age,sex, hospitalNumber!!, area!!)
        }else{
            Log.d(TAG, "NAS is null")
        }

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        //this flag clears all the notificaton in the stack and put the notification in the top

        var pendingIntent : PendingIntent? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        }
        else{
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        }
        //FLAG_ONE_SHOT : bcz we need the pending intent only once, after user clicks the notification it gets destroyed

        val alarmSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notificationBuilder = NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000)) //viberates for 1 sec, holds for 1 sec,viberates for 1 sec, holds for 1 sec,
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .setSound(alarmSound)

        notificationBuilder.setContent(getRemoteView(title, message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random().nextInt(3000)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            setupChannels(notificationManager)

        notificationManager.notify(notificationID, notificationBuilder.build())
    }

    @SuppressLint("SimpleDateFormat")
    private fun storePatient(name: String, age: String, sex: String, hospitalNumber : String, area : String) {
        val currentTime = LocalDateTime.now().toString()
        val dateFormater = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val formatedDate = currentTime.format(dateFormater)

        val timeStamp : String = SimpleDateFormat ("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val id : String = UUID.randomUUID().toString().replace("-", "").uppercase(Locale.getDefault())

        val patient = PatientAndHospital(name, age.toInt(), sex, timeStamp,area, hospitalNumber, id)

        db.collection(PENDING).document(id).set(patient)
            .addOnSuccessListener {
                Log.d(TAG, "Patient Added to pending List")
                Toast.makeText(this, "Patient added to the pending list", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Log.d(TAG, it.message!!)
            }
    }

    private fun getRemoteView(title: String?, message: String?): RemoteViews? {

        Log.d(TAG, "remote Views ()")

        val remoteView = RemoteViews("com.example.notification_trial02", R.layout.notification)

        remoteView.setTextViewText(R.id.title, title)
        remoteView.setTextViewText(R.id.description, message)
        remoteView.setImageViewResource(R.id.notification_logo, R.drawable.ic_notifications)

        return remoteView
    }

    private fun setupChannels(notificationManager: NotificationManager) {

        Log.d(TAG, "setup channel ()")

        val adminChannelName : CharSequence = getString(R.string.app_name)
        val adminChannelDescription = "Device to Device notification"

        val adminChannel = NotificationChannel(
            ADMIN_CHANNEL_ID, //all diff channel requires diff channel id.. declared above
            adminChannelName,
            NotificationManager.IMPORTANCE_HIGH
        )

        //Configure the notification channel
        adminChannel.description = adminChannelDescription
        adminChannel.enableLights(true)
        adminChannel.lightColor = Color.RED
        adminChannel.enableVibration(true)
        adminChannel.setShowBadge(true) //when app has some notification, it will have a dot on app icon
        adminChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC

        notificationManager.createNotificationChannel(adminChannel)
    }
}