package sir.mazer.feture.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class SettingsDataStore(private val context: Context) : DataStoreAccessObject {

    companion object {
        private val Context.datastore by preferencesDataStore(name = "led_panel_datastore")
    }

    private object PreferenceKeys {
        val languageKey = stringPreferencesKey("language_key")
        val premiumKey = booleanPreferencesKey("premium_key")
    }

    override val getLanguage: Flow<String>
        get() = context.datastore.data
            .catch { e ->
                if (e is IOException) {
                    Log.d("DataStore", e.message.toString())
                    emit(emptyPreferences())
                } else {
                    throw e
                }
            }
            .map { prefs ->
                prefs[PreferenceKeys.languageKey] ?: "english"
            }

    override val getPremium: Flow<Boolean>
        get() = context.datastore.data
            .catch { e ->
                if (e is IOException) {
                    Log.d("DataStore", e.message.toString())
                    emit(emptyPreferences())
                } else {
                    throw e
                }
            }
            .map { prefs ->
                prefs[PreferenceKeys.premiumKey] ?: false
            }

    override suspend fun setLanguage(locale: String) {
        context.datastore.edit { prefs ->
            prefs[PreferenceKeys.languageKey] = locale
        }
    }

    override suspend fun setPremium(isPremium: Boolean) {
        context.datastore.edit { prefs ->
            prefs[PreferenceKeys.premiumKey] = isPremium
        }
    }
}