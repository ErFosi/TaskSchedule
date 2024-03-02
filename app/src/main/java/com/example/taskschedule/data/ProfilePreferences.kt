package com.example.taskschedule.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

val Context.profilePreferences: DataStore<Preferences> by preferencesDataStore("settings")

enum class Idioma(val codigo: String){
    Castellano(codigo="es"),
    Euskera(codigo="eu"),
    Ingles(codigo="en")
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
            val oscuro = preferences[PreferencesKeys.OSCURO_KEY] ?: false
            val idioma=Idioma.valueOf(preferences[PreferencesKeys.IDIOMA_KEY] ?: Idioma.Castellano.name
            )
            Settings(oscuro,idioma)
        }
    suspend fun updateOscuro(oscuro: Boolean){
        profilePreferences.edit { settings ->
            settings[PreferencesKeys.OSCURO_KEY]= oscuro

        }
    }

    suspend fun updateIdioma(idioma:Idioma){
        profilePreferences.edit { settings ->
            settings[PreferencesKeys.IDIOMA_KEY]=idioma.name
        }
    }
}