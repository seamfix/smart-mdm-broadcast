package com.seamfix.testdeviceowner
import android.R
import android.app.*
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.Keep
import androidx.core.app.NotificationCompat

const val CHANNEL_ID = "com.seamfix.testdeviceowner"
const val CHANNEL_NAME = "CHANNEL"

@Keep
class BroadcastService: Service() {

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.e(Broadcaster::class.java.simpleName, "Service  onCreate called.")
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(Broadcaster::class.java.simpleName, "Service onStartCommand  called")

        val notificationIntent = Intent(this, BroadcastService::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("SmartMDM")
                .setContentText("Broadcast in progress...")
                .setSmallIcon(R.drawable.alert_dark_frame)
                .setContentIntent(pendingIntent)
                .build()
        startForeground(1, notification)


        val receiver = Broadcaster()
        val filter = IntentFilter(MDM_ACTION)
        registerReceiver(receiver, filter)
        Log.e(Broadcaster::class.java.simpleName, "Looping...")

        return START_STICKY
    }


    private fun createNotificationChannel() { //If you don't call this method, you notifications will only show on older versions of android phones.
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            channel.description = "SmartMDM broadcast in progress..."
            channel.enableVibration(true)
            channel.enableLights(true)
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}