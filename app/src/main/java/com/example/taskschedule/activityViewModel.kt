package com.example.taskschedule;

import android.util.Log
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.layout.LookaheadScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskschedule.data.Actividad
import kotlinx.coroutines.delay

class ActivitiesViewModel : ViewModel() {
    private val _actividades = getTasks().toMutableStateList()
    private var idCounter = 20
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

}

private fun getTasks(): List<Actividad> {
    return emptyList()
}