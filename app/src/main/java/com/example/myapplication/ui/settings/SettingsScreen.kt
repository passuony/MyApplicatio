package com.example.myapplication.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.SettingsViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onSaveAndNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = viewModel()
) {
    val settings by viewModel.settingsFlow.collectAsState()

    // شاشة بسيطة لعرض الإعدادات
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Settings") },
                actions = {
                    Button(onClick = {
                        viewModel.saveSettings()
                        onSaveAndNavigateBack()
                    }) {
                        Text("Save")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {

            // 1. إعداد Query (نص البحث)
            OutlinedTextField(
                value = settings.query,
                onValueChange = viewModel::updateQuery,
                label = { Text("Search Term") }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 2. إعداد الحد الأدنى للتقييم
            Text("Minimum Rating: ${settings.minRating}", style = MaterialTheme.typography.titleMedium)
            Slider(
                value = settings.minRating.toFloat(),
                onValueChange = { viewModel.updateMinRating(it.toInt()) },
                valueRange = 0f..5f,
                steps = 4 // لـ 0, 1, 2, 3, 4, 5
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 3. فلتر الكتب المجانية
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = settings.onlyFreeBooks,
                    onCheckedChange = viewModel::toggleFreeBooks
                )
                Text("Only Free Books")
            }
        }
    }
}