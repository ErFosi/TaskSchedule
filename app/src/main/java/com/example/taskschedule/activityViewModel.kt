package com.example.taskschedule;

import android.util.Log
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.layout.LookaheadScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskschedule.data.Actividad

class ActivitiesViewModel : ViewModel() {
    private val _actividades = getTasks().toMutableStateList()
    private var idCounter = 20
    val actividades: List<Actividad>
        get()=_actividades


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



    fun onRemoveClick(actividad: Actividad) {
        _actividades.remove(actividad)
    }

}

private fun getTasks(): List<Actividad> {
    return emptyList()
}