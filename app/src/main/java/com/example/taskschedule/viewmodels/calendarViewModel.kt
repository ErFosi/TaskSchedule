package com.example.taskschedule.viewmodels

import androidx.lifecycle.ViewModel
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
    fun agruparActividadesPorCategoria(actividades: List<Actividad>): Map<String, Int> {
        return actividades.groupBy { it.categoria }
            .mapValues { entry -> entry.value.sumOf { it.tiempo } }
    }

    fun testListaActividades(){

    }
}