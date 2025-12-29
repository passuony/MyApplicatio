package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.myapplication.di.AppContainer
import com.example.myapplication.ui.theme.MyApplicationTheme

// [هام جداً]: استيراد الدالة من الحزمة الصحيحة
import com.example.myapplication.ui.navigation.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // إنشاء الـ appContainer
        val appContainer = AppContainer(applicationContext)

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // استدعاء الدالة بعد استيرادها
                    MainScreen(appContainer = appContainer)
                }
            }
        }
    }
}