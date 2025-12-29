package com.example.myapplication.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.myapplication.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_profile")

class ProfileDataStore(private val context: Context) {
    private val NAME = stringPreferencesKey("full_name")
    private val URI = stringPreferencesKey("avatar_uri")
    private val URL = stringPreferencesKey("resume_url")
    private val POS = stringPreferencesKey("position")

    val profileFlow: Flow<UserProfile> = context.dataStore.data.map { pref ->
        UserProfile(
            fullName = pref[NAME] ?: "",
            avatarUri = pref[URI] ?: "",
            resumeUrl = pref[URL] ?: "",
            position = pref[POS] ?: ""
        )
    }

    suspend fun saveProfile(profile: UserProfile) {
        context.dataStore.edit { pref ->
            pref[NAME] = profile.fullName
            pref[URI] = profile.avatarUri
            pref[URL] = profile.resumeUrl
            pref[POS] = profile.position
        }
    }
}