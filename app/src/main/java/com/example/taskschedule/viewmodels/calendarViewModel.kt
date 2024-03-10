package com.example.taskschedule.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskschedule.data.Actividad
import com.example.taskschedule.repositories.ActividadesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import java.time.LocalDate
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import androidx.compose.runtime.collectAsState as collectAsState
import kotlinx.coroutines.runBlocking


@HiltViewModel
class CalendarViewModel @Inject constructor(private val actividadesRepo: ActividadesRepository): ViewModel(){
    private val _fechaSelec = MutableStateFlow(LocalDate.now()) // Estado interno mutable
    val fechaSelec: StateFlow<LocalDate> = _fechaSelec
    //var _actividadesFecha = actividadesRepo.getActividadesPorFecha(fecha = fechaSelec.value)
    val actividadesFecha: Flow<List<Actividad>> = _fechaSelec.flatMapLatest { fecha ->
        actividadesRepo.getActividadesPorFecha(fecha)
    }
    fun cambioFecha(nuevaFecha: LocalDate) {
        _fechaSelec.value = nuevaFecha // Actualiza el estado
    }
    fun descargarActividadesJson(): String {
        val gson = GsonBuilder().setPrettyPrinting().create()
            return runBlocking {
                val actividadesFiltradas = actividadesFecha.first().map { actividad ->
                    mapOf(
                        "nombre" to actividad.nombre,
                        "tiempo" to actividad.tiempo,
                        "categoria" to actividad.categoria,
                        "fecha" to actividad.fecha.format(DateTimeFormatter.ISO_DATE)
                    )
                }


                val actividadesJson = gson.toJson(actividadesFiltradas)
                return@runBlocking actividadesJson


            }

        }

}