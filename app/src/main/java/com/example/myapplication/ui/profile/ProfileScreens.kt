package com.example.myapplication.ui.profile

import android.Manifest
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.myapplication.viewmodel.ProfileViewModel
import java.io.File

@Composable
fun ProfileTabScreen(viewModel: ProfileViewModel, onEditClick: () -> Unit) {
    val profile by viewModel.profile.collectAsState()
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            IconButton(onClick = onEditClick) {
                Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
            }
        }

        AsyncImage(
            model = if (profile.avatarUri.isEmpty()) "https://cdn-icons-png.flaticon.com/512/149/149071.png" else profile.avatarUri,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(16.dp))
        Text(
            text = profile.fullName.ifEmpty { "User Name" },
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = profile.position.ifEmpty { "Job Position" },
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray
        )

        Spacer(Modifier.height(24.dp))

        // Resume Link Button
        if (profile.resumeUrl.isNotEmpty()) {
            Button(
                onClick = { uriHandler.openUri(profile.resumeUrl) },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Icon(Icons.Default.Link, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("View Resume / Portfolio")
            }
        }

        // Lesson Time Card
        if (profile.favoriteLessonTime.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
            Card(modifier = Modifier.fillMaxWidth()) {
                ListItem(
                    headlineContent = { Text("Favorite Lesson Time") },
                    supportingContent = { Text(profile.favoriteLessonTime) },
                    leadingContent = { Icon(Icons.Default.AccessTime, null) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(viewModel: ProfileViewModel, onDone: () -> Unit, onCancel: () -> Unit) {
    val currentProfile by viewModel.profile.collectAsState()
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }
    var resumeUrl by remember { mutableStateOf("") }
    var selectedUri by remember { mutableStateOf("") }
    var lessonTime by remember { mutableStateOf("") }

    LaunchedEffect(currentProfile) {
        name = currentProfile.fullName
        position = currentProfile.position
        resumeUrl = currentProfile.resumeUrl
        selectedUri = currentProfile.avatarUri
        lessonTime = currentProfile.favoriteLessonTime
    }

    val timeRegex = Regex("^([01]?[0-9]|2[0-3]):[0-5][0-9]$")
    val isTimeValid = lessonTime.isEmpty() || timeRegex.matches(lessonTime)
    var showSheet by remember { mutableStateOf(false) }

    val tempUri = remember {
        val file = File(context.cacheDir, "temp_image.jpg")
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) selectedUri = tempUri.toString()
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let { selectedUri = it.toString() }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) cameraLauncher.launch(tempUri)
    }

    val timePicker = TimePickerDialog(context, { _, h, m ->
        lessonTime = String.format("%02d:%02d", h, m)
    }, 12, 0, true)

    Column(Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Text("Edit Profile", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(16.dp))

        // Avatar Picker Section
        Box(Modifier.align(Alignment.CenterHorizontally).size(110.dp).clickable { showSheet = true }) {
            AsyncImage(
                model = if (selectedUri.isEmpty()) "https://cdn-icons-png.flaticon.com/512/149/149071.png" else selectedUri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                contentScale = ContentScale.Crop
            )
            Icon(
                Icons.Default.PhotoCamera,
                null,
                Modifier
                    .align(Alignment.BottomEnd)
                    .background(MaterialTheme.colorScheme.primary, CircleShape)
                    .padding(6.dp),
                tint = Color.White
            )
        }

        Spacer(Modifier.height(24.dp))

        // Input Fields
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            isError = name.isEmpty()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = position,
            onValueChange = { position = it },
            label = { Text("Job Position") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = resumeUrl,
            onValueChange = { resumeUrl = it },
            label = { Text("Resume/Portfolio URL") },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("https://example.com") },
            leadingIcon = { Icon(Icons.Default.Link, null) }
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = lessonTime,
            onValueChange = { lessonTime = it },
            label = { Text("Favorite Lesson Time (HH:mm)") },
            modifier = Modifier.fillMaxWidth(),
            isError = !isTimeValid,
            supportingText = { if(!isTimeValid) Text("Use format 14:00") },
            trailingIcon = {
                IconButton(onClick = { timePicker.show() }) {
                    Icon(Icons.Default.AccessTime, null)
                }
            }
        )

        Spacer(Modifier.height(32.dp))

        // Action Buttons
        Button(
            onClick = {
                if (name.isNotEmpty() && lessonTime.isNotEmpty() && isTimeValid) {
                    viewModel.updateProfile(name, selectedUri, resumeUrl, position, lessonTime)
                    viewModel.scheduleNotification(context, lessonTime, name)
                    onDone()
                } else {
                    Toast.makeText(context, "Please fill in Name and Time correctly", Toast.LENGTH_LONG).show()
                }
            },
            enabled = name.isNotEmpty() && lessonTime.isNotEmpty(),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Save Changes", fontWeight = FontWeight.Bold)
        }

        TextButton(onClick = onCancel, modifier = Modifier.fillMaxWidth()) {
            Text("Cancel", color = Color.Gray)
        }
    }

    // Modal Bottom Sheet for Image Source
    if (showSheet) {
        ModalBottomSheet(onDismissRequest = { showSheet = false }) {
            Column(Modifier.padding(bottom = 32.dp)) {
                ListItem(
                    headlineContent = { Text("Take a Photo") },
                    leadingContent = { Icon(Icons.Default.CameraAlt, null) },
                    modifier = Modifier.clickable {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                        showSheet = false
                    }
                )
                ListItem(
                    headlineContent = { Text("Choose from Gallery") },
                    leadingContent = { Icon(Icons.Default.PhotoLibrary, null) },
                    modifier = Modifier.clickable {
                        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        showSheet = false
                    }
                )
            }
        }
    }
}