package com.example.myapplication.receiver

import android.app.*
import android.content.*
import androidx.core.app.NotificationCompat
import com.example.myapplication.MainActivity

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val name = intent.getStringExtra("USER_NAME") ?: "Student"
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "lesson_channel"

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            manager.createNotificationChannel(NotificationChannel(channelId, "Lessons", NotificationManager.IMPORTANCE_HIGH))
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("بداية المحاضرة")
            .setContentText("مرحباً $name، حان وقت محاضرة تطوير التطبيقات!")
            .setAutoCancel(true)
            .setContentIntent(PendingIntent.getActivity(context, 0, Intent(context, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE))
            .build()

        manager.notify(1, notification)
    }
}