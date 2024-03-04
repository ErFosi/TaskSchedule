package com.example.taskschedule;

import android.content.Context
import android.util.Log
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.layout.LookaheadScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskschedule.data.Actividad
import com.example.taskschedule.data.Idioma
import com.example.taskschedule.data.ProfilePreferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.taskschedule.utils.LanguageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first

@HiltViewModel
class ActivitiesViewModel @Inject constructor(
private val settings:ProfilePreferencesDataStore,
private val languageManager: LanguageManager
): ViewModel() {
    val oscuro =settings.settingsFlow.map{it.oscuro}
    val idioma = settings.settingsFlow.map {it.idioma}
    private val _actividades = getTasks().toMutableStateList()
    private var idCounter = 1
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
    val actividades: List<Actividad>
        get()=_actividades

    fun agregarActTest(actividad: Actividad){
        _actividades.add(actividad)
    }
    fun agregarActividad(nombre: String) {
        idCounter++
        val nuevaActividad = Actividad(idCounter,nombre, 0)
        _actividades.add(nuevaActividad)
    }

    fun togglePlay(actividad: Actividad) {
        // Encuentra el índice de la actividad en la lista
        val index = _actividades.indexOfFirst { it.id == actividad.id }
        if (index != -1) {
            val currentActividad = _actividades[index]

            if (currentActividad.isPlayingState) {
                // Calcula la duración desde el inicio hasta ahora y actualiza el tiempo
                val endTime = System.currentTimeMillis()
                val diff = (endTime - (currentActividad.startTimeMillis ?: endTime)) / 1000
                currentActividad.tiempo += diff.toInt()
                currentActividad.tiempostate = currentActividad.tiempo
                // Actualiza los estados para reflejar que la actividad ha sido detenida
                currentActividad.isPlayingState = false
                currentActividad.isPlaying = false
                currentActividad.startTimeMillis = null
            } else {
                // Marca el inicio de la actividad
                currentActividad.startTimeMillis = System.currentTimeMillis()
                // Actualiza los estados para reflejar que la actividad está en reproducción
                currentActividad.isPlayingState = true
                currentActividad.isPlaying = true
            }

            _actividades[index] = currentActividad
        }
    }

    // Método para actualizar la categoría de una actividad
    fun updateCategoria(id: Int, nuevaCategoria: String) {
        val index = _actividades.indexOfFirst { it.id == id }
        //_actividades[index].categoria=nuevaCategoria
        if (index != -1){_actividades[index].categoriaState=nuevaCategoria}

    }

    fun onRemoveClick(id: Int) {
        try {
            // Intenta eliminar el elemento por el índice dado
            _actividades.remove(_actividades.find { it.id==id })
        } catch (e: IndexOutOfBoundsException) {
            // Si ocurre un IndexOutOfBoundsException, simplemente ignóralo
            // Puedes imprimir un mensaje de error o manejarlo como consideres necesario
            // println("Intento de eliminar un elemento con un índice fuera de rango: $e")
            Log.d("e","El indice es")
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