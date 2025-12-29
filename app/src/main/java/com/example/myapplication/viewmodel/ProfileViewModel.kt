package com.example.myapplication.viewmodel

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.lifecycle.*
import com.example.myapplication.data.datastore.ProfileDataStore
import com.example.myapplication.domain.model.UserProfile
import com.example.myapplication.receiver.AlarmReceiver
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

class ProfileViewModel(private val dataStore: ProfileDataStore) : ViewModel() {

    // تحويل الـ Flow من DataStore إلى StateFlow لتستخدمه واجهة Compose
    val profile: StateFlow<UserProfile> = dataStore.profileFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfile()
        )

    /**
     * تحديث الملف الشخصي مع إضافة حقل وقت المحاضرة
     * [تعديل]: تم إضافة المعامل الخامس (lessonTime)
     */
    fun updateProfile(name: String, uri: String, url: String, pos: String, lessonTime: String) {
        viewModelScope.launch {
            dataStore.saveProfile(
                UserProfile(
                    fullName = name,
                    avatarUri = uri,
                    resumeUrl = url,
                    position = pos,
                    favoriteLessonTime = lessonTime // تمرير الوقت للحفظ
                )
            )
        }
    }

    /**
     * جدولة التنبيه الخاص بالمحاضرة
     */
    fun scheduleNotification(context: Context, time: String, userName: String) {
        if (time.isEmpty()) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("USER_NAME", userName)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val parts = time.split(":")
        if (parts.size != 2) return

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, parts[0].toInt())
            set(Calendar.MINUTE, parts[1].toInt())
            set(Calendar.SECOND, 0)
        }

        // إذا كان الوقت المختار قد مضى اليوم، نجدوله للغد
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1)
        }

        try {
            // استخدام المنبه الدقيق (Exact Alarm) لضمان العمل في الوقت المحدد
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    pendingIntent
                )
            }
        } catch (e: SecurityException) {
            // في حال عدم توفر صلاحية المنبه الدقيق في أندرويد 12+
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }
}

/**
 * المصنع (Factory) لإنشاء الـ ViewModel مع حقن الـ DataStore
 */
class ProfileViewModelFactory(private val dataStore: ProfileDataStore) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(dataStore) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}