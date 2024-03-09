package com.example.taskschedule.viewmodels;

import android.content.Context
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.layout.LookaheadScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskschedule.data.Actividad
import com.example.taskschedule.data.Idioma
import com.example.taskschedule.data.ProfilePreferencesDataStore
import com.example.taskschedule.repositories.ActividadesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.taskschedule.utils.LanguageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.time.LocalDate

@HiltViewModel
class ActivitiesViewModel @Inject constructor(
private val settings:ProfilePreferencesDataStore,
private val languageManager: LanguageManager,
    private val actividadesRepo: ActividadesRepository
): ViewModel() {
    val oscuro =settings.settingsFlow.map{it.oscuro}
    val idioma = settings.settingsFlow.map {it.idioma}
    private val _actividades = actividadesRepo.getActividadesPorFecha(LocalDate.now())
    init {
        this.settings.language()
        viewModelScope.launch {
            //changeLang(idioma.first())
            changeLang(Idioma.getFromCode(settings.language().first()))
            Log.d("I","Se inicia la app con el idioma:"+settings.language().first())
        }
    }
    fun cambiarOscuro(oscuro : Boolean){
        viewModelScope.launch{settings.updateOscuro(oscuro)}

    }


    fun updateIdioma(idioma:Idioma){
        viewModelScope.launch { settings.setLanguage(idioma.code) }
        this.changeLang(idioma)
        Log.d("t","Se actualiza el idioma a"+idioma.language)
    }
    val actividades: Flow<List<Actividad>>
        get()=_actividades

    fun agregarActTest(actividad: Actividad){
        viewModelScope.launch{
            actividadesRepo.insertActividad(actividad)
        }
    }
    fun agregarActividad(nombre: String) {

        val nuevaActividad = Actividad(
            nombre = nombre,id = 0) //el id es autogenerado ya que es autogenerate (esto se ve en data/data.kt donde está la entidad)
        viewModelScope.launch{
            actividadesRepo.insertActividad(nuevaActividad)
        }
    }

    fun togglePlay(actividad: Actividad) {
        // Encuentra el índice de la actividad en la lista

            val currentActividad = actividad.copy()

            if (currentActividad.isPlaying) {
                // Calcula la duración desde el inicio hasta ahora y actualiza el tiempo
                val endTime = System.currentTimeMillis()
                val diff = (endTime - (currentActividad.startTimeMillis ?: endTime)) / 1000
                currentActividad.tiempo += diff.toInt()
                //currentActividad.tiempostate = currentActividad.tiempo
                // Actualiza los estados para reflejar que la actividad ha sido detenida
                //currentActividad.isPlayingState = false
                currentActividad.isPlaying = false
                //currentActividad.startTimeMillis = null
            } else {
                // Marca el inicio de la actividad
                currentActividad.startTimeMillis = System.currentTimeMillis()
                // Actualiza los estados para reflejar que la actividad está en reproducción
                //currentActividad.isPlayingState = true
                currentActividad.isPlaying = true
            }

            viewModelScope.launch {
                actividadesRepo.updateActividad(currentActividad)
            }
    }


    // Método para actualizar la categoría de una actividad
    fun updateCategoria(act: Actividad, nuevaCategoria: String) {
       viewModelScope.launch {
           actividadesRepo.updateActividad(act)
       }

    }

    fun onRemoveClick(id: Int) {

            // Intenta eliminar el elemento por el índice dado
            viewModelScope.launch {
               var act=actividadesRepo.getActividadStream(id)
                act.collect { actividad ->
                    // act es ahora una instancia de Actividad y puede ser usada dentro de este bloque
                    if (actividad != null) {
                        actividadesRepo.deleteActividad(actividad)
                    }
                    // Considera detener la recolección si solo necesitas una instancia,
                    // ya que un Flow puede emitir múltiples elementos.
                    return@collect // Rompe el flujo después del primer elemento para evitar múltiples eliminaciones.
                }
            }

    }
    //apartado del idioma
    // Current app's language and preferred language (may not be the same at the beginning)
    val currentSetLang by languageManager::currentLang
    fun changeLang(idioma: Idioma) {
        languageManager.changeLang(idioma)
        //viewModelScope.launch(Dispatchers.IO) { settings.updateIdioma(idioma) }
    }

}

private fun getTasks(): List<Actividad> {
    return emptyList()
}