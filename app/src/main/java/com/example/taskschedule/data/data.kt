package com.example.taskschedule.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class Actividad(
    val id: Int,
    val nombre: String,
    var tiempo: Int,
    var categoria: String = "Otros",
    var isPlaying: Boolean = false,
    var onPlayClick: () -> Unit = {},
    var onStopClick: () -> Unit = {}
) {
    var categoriaState by mutableStateOf(categoria)
    var isPlayingState by mutableStateOf(isPlaying)
    var tiempostate by mutableStateOf(tiempo)
    var startTimeMillis: Long? = null
}
