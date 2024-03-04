package com.example.taskschedule.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

import kotlinx.coroutines.flow.map
import java.io.IOException
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton


val Context.profilePreferences: DataStore<Preferences> by preferencesDataStore("settings")

enum class Idioma(val language: String, val code: String) {
    EN("English", "en"),
    EU("Euskera", "eu"),
    ES("Español", "es");
    companion object {
        /**
         * Get the [AppLanguage] from a language code.
         *
         * @param code Language's code as string
         * @return That code's corresponding App's language as an [AppLanguage]. Defaults to [EN].
         */
        fun getFromCode(code: String) = when (code) {
            EU.code -> EU
            EN.code -> EN
            ES.code -> ES
            else -> EN
        }
    }
}
data class Settings(val oscuro:Boolean,
val idioma:Idioma)

@Singleton
class ProfilePreferencesDataStore @Inject constructor(
    @ApplicationContext context: Context
){
private val profilePreferences=context.profilePreferences

    private object PreferencesKeys{
        val OSCURO_KEY= booleanPreferencesKey("oscuro")
        val IDIOMA_KEY= stringPreferencesKey("idioma")
    }

    val settingsFlow= profilePreferences.data
        .catch {  exception ->
            if (exception is IOException){
                emit(emptyPreferences())
            }else {
                throw exception
            }
        }.map { preferences->
            val oscuro = preferences[PreferencesKeys.OSCURO_KEY] ?: true
            val idioma:Idioma = Idioma.getFromCode(Locale.getDefault().language.lowercase())?:Idioma.EN

            Settings(oscuro,idioma)
        }
    suspend fun updateOscuro(oscuro: Boolean){
        profilePreferences.edit { settings ->
            settings[PreferencesKeys.OSCURO_KEY]= oscuro

        }
    }
    fun language(): Flow<String> = profilePreferences.data.map { preferences -> preferences[PreferencesKeys.IDIOMA_KEY]?: Locale.getDefault().language }
    suspend fun setLanguage(code: String) {
        profilePreferences.edit { settings ->  settings[PreferencesKeys.IDIOMA_KEY]=code}
        Log.d("P","Se esta cambiando el lenguaje en el dataStore"+code)
    }
    suspend fun updateIdioma(idioma:Idioma){
        profilePreferences.edit { settings ->
            settings[PreferencesKeys.IDIOMA_KEY]=idioma.name
        }
    }
}