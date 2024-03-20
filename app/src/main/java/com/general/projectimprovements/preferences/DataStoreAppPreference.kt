package com.general.projectimprovements.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.intellimec.oneapp.data.preferences.AppPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigInteger
import java.security.MessageDigest

internal class DataStoreAppPreference(
    private val context: Context,
    appPreferenceDataSerializer: AppPreferenceDataSerializer
) : AppPreference {

    private val scope by lazy { CoroutineScope(Dispatchers.IO + SupervisorJob()) }

    private val Context.dataStore: DataStore<AppPreferenceData> by dataStore(
        fileName = "app.pb",
        serializer = appPreferenceDataSerializer,
        scope = scope
    )

    override fun getAppPreferenceContent(): Flow<Any?> = getFlowFromDataStore { it }

    override suspend fun setCurrentAccountId(accountId: String?) = updateDataStore {
        if (accountId != null) {
            it.copy(
                userId = accountId,
                showLoginPage = true
            )
        } else {
            it.copy(userId = accountId)
        }
    }

    private suspend fun updateDataStore(update: (AppPreferenceData) -> AppPreferenceData) {
        context.dataStore.updateData { update(it) }
    }

    private fun <T : Any> getFlowFromDataStore(mapData: (AppPreferenceData) -> T?): Flow<T?> = context.dataStore.data.map { mapData(it) }
}

private suspend fun <T : Any> setUnsafeValueByKey(key: Preferences.Key<T>, value: T?) {
    context.unsafeDataStore.edit { preferences ->
        value?.let {
            preferences[key] = value
        } ?: let {
            preferences.remove(key)
        }
    }
}

@Suppress("SameParameterValue")
private fun <T : Any> getUnsafeFlowByKey(key: Preferences.Key<T>): Flow<T?> = context.unsafeDataStore.data
    .map { preferences ->
        preferences[key]
    }

private suspend fun updateDataStore(update: (UserPreferenceData) -> UserPreferenceData) {
    context.dataStore.updateData { update(it) }
}

private fun <T : Any> getFlowFromDataStore(mapData: (UserPreferenceData) -> T?): Flow<T?> = context.dataStore.data.map { mapData(it) }

private fun md5(input: String): String = runCatching {
    val messageDigest = MessageDigest.getInstance("MD5")
    BigInteger(1, messageDigest.digest(input.toByteArray()))
        .toString(16)
        .padStart(32, '0')
}.getOrElse {
    input
}

