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
    private var startTime = mutableMapOf<Actividad, Long>()
    private var idCounter = 20
    val actividades: List<Actividad>
        get()=_actividades


    fun agregarActividad(nombre: String) {
        idCounter++
        val nuevaActividad = Actividad(idCounter,nombre, 0)
        _actividades.add(nuevaActividad)
    }

    fun togglePlay(actividad: Actividad) {
        val index = _actividades.indexOfFirst { it.id == actividad.id }
        Log.e("ERROR",""+index)
        if (index != -1) {

            if (_actividades[index].isPlayingState){
                val endTime = System.currentTimeMillis()
                val diff = (endTime - (startTime[_actividades[index]] ?: endTime)) / 1000
                _actividades[index].tiempostate += diff.toInt()
                _actividades[index].tiempo=_actividades[index].tiempostate
                _actividades[index].isPlayingState = false
                _actividades[index].isPlaying= _actividades[index].isPlayingState
                startTime.remove(_actividades[index])
            } else {
                Log.e("ERROR",""+_actividades[index].isPlaying)
                startTime[_actividades[index]] = System.currentTimeMillis()
                _actividades[index].isPlayingState = true
                _actividades[index].isPlaying= _actividades[index].isPlayingState
            }
            val actividad=Actividad(-1,"nombre", 0)
            _actividades.add(actividad)
            _actividades.remove(actividad)
        }
    }



    fun onRemoveClick(actividad: Actividad) {
        _actividades.remove(actividad)
    }

}

private fun getTasks(): List<Actividad> {
    return emptyList()
}