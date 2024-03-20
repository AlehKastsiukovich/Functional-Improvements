package com.general.projectimprovements.preferences

import android.content.Context
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplates
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import com.intellimec.oneapp.data.preferences.AppPreference
import com.intellimec.oneapp.data.preferences.SessionManager
import com.intellimec.oneapp.data.preferences.SessionManagerImpl
import com.intellimec.oneapp.data.preferences.UserPreferenceProvider
import com.intellimec.oneapp.data.preferences.datastore.AppPreferenceDataSerializer
import com.intellimec.oneapp.data.preferences.datastore.DataStoreAppPreference
import com.intellimec.oneapp.data.preferences.datastore.SdKUserPreferenceDataMapperImpl
import com.intellimec.oneapp.data.preferences.datastore.SdkUserPreferenceDataMapper
import com.intellimec.oneapp.data.preferences.datastore.UserPreferenceDataSerializer
import com.intellimec.oneapp.data.preferences.datastore.UserPreferenceProviderImpl
import org.koin.dsl.module

private const val KEYSET_NAME = "master_keyset"
private const val PREFERENCE_FILE = "master_key_preference"
private const val MASTER_KEY_URI = "android-keystore://master_key"

val dataPreferencesModule = module {

    single<Aead> {
        AeadConfig.register()
        val context = get<Context>()
        AndroidKeysetManager.Builder()
            .withSharedPref(context, KEYSET_NAME, PREFERENCE_FILE)
            .withKeyTemplate(KeyTemplates.get("AES256_GCM"))
            .withMasterKeyUri(MASTER_KEY_URI)
            .build()
            .keysetHandle
            .getPrimitive(Aead::class.java)
    }
}
