package com.theinsuranceboss.app.data.session

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "boss_session")

class SessionStore(private val context: Context) {
    private val keyToken = stringPreferencesKey("session_token")
    private val keyUserJson = stringPreferencesKey("user_json")

    val tokenFlow: Flow<String?> = context.dataStore.data.map { it[keyToken] }
    val userJsonFlow: Flow<String?> = context.dataStore.data.map { it[keyUserJson] }

    suspend fun token(): String? = context.dataStore.data.first()[keyToken]
    suspend fun userJson(): String? = context.dataStore.data.first()[keyUserJson]

    suspend fun save(token: String, userJson: String) {
        context.dataStore.edit {
            it[keyToken] = token
            it[keyUserJson] = userJson
        }
    }

    suspend fun clear() {
        context.dataStore.edit {
            it.remove(keyToken)
            it.remove(keyUserJson)
        }
    }
}
