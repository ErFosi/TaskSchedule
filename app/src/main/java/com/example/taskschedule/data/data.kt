package com.example.taskschedule.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.compose.runtime.Immutable


@Immutable
@Entity(tableName = "actividades")
data class Actividad(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nombre: String,
    var tiempo: Int = 0,
    var categoria: String = "Otros",
    var startTimeMillis: Long = 0,
    var isPlaying: Boolean = false,
    val idUsuario: Int=0, //Modificar en la siguiente entrega con el login
)

