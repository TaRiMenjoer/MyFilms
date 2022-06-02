package com.example.myfilms

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessagingService: FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FIREBASE_TOKEN", "Refreshed token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        showMessage(message)
    }



    fun showMessage(message: RemoteMessage){
        val data: Map<String, String> = message.data
        val title  = data["title"]
        val content = data["content"]

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val NOTIFICATION_CHANEL_ID = "MyPetProject"

        val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANEL_ID,
            "FirebaseFirstApp",
            NotificationManager.IMPORTANCE_HIGH
        )

        notificationChannel.description = "MyPetProject channel for app test FCM"
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.YELLOW
        notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
        notificationChannel.enableVibration(true)
        notificationManager.createNotificationChannel(notificationChannel)

        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANEL_ID)
        val remoteViews = RemoteViews(applicationContext.packageName, R.layout.notification)
        notificationBuilder
            .setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomBigContentView(remoteViews)

        notificationManager.notify(1, notificationBuilder.build())
    }
}